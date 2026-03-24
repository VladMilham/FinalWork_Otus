package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import utils.{Feeders, Requests, config}

import scala.concurrent.duration._
import scala.util.Random

object UseScenarios {

  // Вспомогательная функция для выбора случайного интервала
  val pickRandomInterval: ChainBuilder = exec(session => {
    val random = new Random()

    def pickRandomFromArray(key: String): String = {
      try {
        val arr = session(key).as[Vector[String]]
        if (arr.nonEmpty) arr(random.nextInt(arr.length)) else ""
      } catch {
        case _: Throwable =>
          try {
            val jsonStr = session(key).as[String]
            val cleaned = jsonStr.trim.stripPrefix("[").stripSuffix("]")
            val items = cleaned.split("\",\"").map(s => s.replaceAll("\"", ""))
            if (items.nonEmpty) items(random.nextInt(items.length)) else ""
          } catch {
            case _: Throwable => ""
          }
      }
    }

    session.set("interval", pickRandomFromArray("intervals"))
  })

  //  val pickRandomCity: ChainBuilder = exec(session => {
  //    val random = new Random()
  //
  //    def pickRandomFromArray(key: String): String = {
  //      try {
  //        val arr = session(key).as[Vector[String]]
  //        if (arr.nonEmpty) arr(random.nextInt(arr.length)) else ""
  //      } catch {
  //        case _: Throwable =>
  //          try {
  //            val jsonStr = session(key).as[String]
  //            val cleaned = jsonStr.trim.stripPrefix("[").stripSuffix("]")
  //            val items = cleaned.split("\",\"").map(s => s.replaceAll("\"", ""))
  //            if (items.nonEmpty) items(random.nextInt(items.length)) else ""
  //          } catch {
  //            case _: Throwable => ""
  //          }
  //      }
  //    }
  //
  //    session.set("idCity", pickRandomFromArray("cities"))
  //  })

  println(s"BASE URL: ${config.baseUrl}")

  //  def safeRequest(request: ChainBuilder): ChainBuilder = {
  //    tryMax(2) {
  //      request
  //        .exec(
  //          doIf(session => {
  //            try {
  //              val errorCode = session("errorCode").as[String]
  //              errorCode == "invalid_token"
  //            } catch {
  //              case _: Throwable => false
  //            }
  //          }) {
  //            exec(reauthorizeChain)
  //              .exec(request)
  //          }
  //        )
  //    }
  //  }


  val authChain: ChainBuilder =
    exec(Requests.getToken)

  // ЦЕПОЧКА 2: АДРЕСА (выполняются один раз и сохраняют данные)
  val addressChain: ChainBuilder =
    exec(Requests.getUserAddresses)
      // СЛУЧАЙ 1: НЕТ АДРЕСОВ - СОЗДАЁМ
      .doIf(session =>
        !session.contains("idAddress") ||
          session("idAddress").asOption[Any].isEmpty
      ) {
        exec(Requests.createAddress)
          .exec(Requests.choiceAddress)
      }
      // СЛУЧАЙ 2: ЕСТЬ АДРЕСА - ВЫБИРАЕМ ОДИН
      .doIf(session =>
        session.contains("idAddress") &&
          session("idAddress").asOption[Any].isDefined
      ) {
        exec(Requests.choiceAddress)
      }


  // Цепочка: TSD авторизация (опциональна, выполняется один раз)
  val tsdAuthChain: ChainBuilder =
    exec(Requests.getTokenTSD)



  // Сценарий
  val scn: ScenarioBuilder = scenario("UseScenarios - preprod MSA Load")
    .feed(Feeders.user)
    .feed(Feeders.device)
    .feed(Feeders.shop)
    .feed(Feeders.shopNN)
    .feed(Feeders.city)

    // ШАГ 1: ОСНОВНАЯ АВТОРИЗАЦИЯ
    .exec(tsdAuthChain).pause(0.3.seconds)

    .exec(authChain)
    .pause(0.2.seconds)

    // ШАГ 2: ВЫБОР АДРЕСА И ИНТЕРВАЛА

    .exec(addressChain)
    .pause(0.3.seconds)

    .exec(pickRandomInterval)
    .pause(0.2.seconds)

    
    // ШАГ 3: СЛУЧАЙНЫЙ ТРАФИК
    .doIf(session => session.contains("token")) {
      during(30.seconds) {
        randomSwitch(
          1.29 -> exec(Requests.getUserAddresses).pause(0.3.seconds),

          // TSD ИНФОРМАЦИЯ (9.76%, только если есть tsd_token)
          9.79 -> exec(Requests.getTsdInfo).pause(0.1.seconds),

          // КРИТИЧНЫЕ ОПЕРАЦИИ (38%)
          10.92 -> exec(Requests.getPushToken).pause(0.2.seconds),
          5.35 -> exec(Requests.getPlaceBasket).pause(0.3.seconds),
          7.14 -> exec(Requests.searchCatalog).pause(0.2.seconds),
          1.92 -> exec(Requests.getEmptyBasket).pause(0.2.seconds),

          // КАТАЛОГ И КОМБО (12%)
          5.01 -> exec(Requests.getCombo).pause(0.3.seconds),
          3.02 -> exec(Requests.getBrands).pause(0.2.seconds),
          3.11 -> exec(Requests.getIndex).pause(0.3.seconds),
          1.04 -> exec(Requests.getRecommendations).pause(0.3.seconds),
          1.09 -> exec(Requests.getProduct).pause(0.2.seconds),
          1.58 -> exec(Requests.getCatalogBanners).pause(0.2.seconds),

          // ИНФОРМАЦИЯ О ГОРОДЕ И СТАТУС (11%)
          0.89 -> exec(Requests.getCitiesList).pause(0.2.seconds),
          6.44 -> exec(Requests.geoCity).pause(0.3.seconds),
          2.87 -> exec(Requests.getOrderStatus).pause(0.2.seconds),
          2.87 -> exec(Requests.getLoyaltyInfo).pause(0.2.seconds),

          // БАННЕРЫ, СТОРИС, ПОПАПЫ (8%)
          3.10 -> exec(Requests.getStories).pause(0.2.seconds),
          2.96 -> exec(Requests.getBanners).pause(0.1.seconds),
          2.27 -> exec(Requests.getPopups).pause(0.3.seconds),
//          1.72 -> exec(Requests.handlePopup).pause(0.2.seconds),

          // ПРОФИЛЬ И КАРТОЧКА (3.5%)
          1.28 -> exec(Requests.getPersonalData).pause(0.3.seconds),
          0.64 -> exec(Requests.getNoticesCount).pause(0.2.seconds),
          0.64 -> exec(Requests.getCardInfo).pause(0.2.seconds),
          0.64 -> exec(Requests.getCardBalance).pause(0.2.seconds),
          0.53 -> exec(Requests.getSettings).pause(0.2.seconds),
          0.53 -> exec(Requests.checkCardActivation).pause(0.2.seconds),

          // ОТЗЫВЫ И РЕЙТИНГИ (2%)
          2.05 -> exec(Requests.notRatedOrders).pause(0.3.seconds),
          1.31 -> exec(Requests.getReviews).pause(0.2.seconds),
          1.49 -> exec(Requests.getReviewPoints).pause(0.3.seconds),

          // ПОИСК И ИСТОРИЯ (1.5%)
          1.99 -> exec(Requests.addSearchHistory).pause(0.2.seconds),
          0.49 -> exec(Requests.getSearchHistory).pause(0.2.seconds),

          // КОРЗИНА И ЗАКАЗЫ (1.8%)
          1.95 -> exec(Requests.addToBasket).pause(0.2.seconds),
          1.02 -> exec(Requests.getOrderDetail).pause(0.3.seconds),
          0.46 -> exec(Requests.getOrdersList).pause(0.3.seconds),
          0.27 -> exec(Requests.getPurchasesList).pause(0.2.seconds),

          // СОБЫТИЯ И БАННЕРЫ (2%)
          1.65 -> exec(Requests.setEvent).pause(0.2.seconds),
          0.26 -> exec(Requests.getOtherBanner).pause(0.2.seconds),

          // QR И РАЗНОЕ (1.5%)
          1.80 -> exec(Requests.getQr).pause(0.3.seconds),
          1.38 -> exec(Requests.getUserAddresses).pause(0.3.seconds),
          0.98 -> exec(Requests.getItemsFeed).pause(0.2.seconds),

          // УВЕДОМЛЕНИЯ И СКИДКИ (1%)
          0.59 -> exec(Requests.getPushDiscounts).pause(0.2.seconds),
          0.18 -> exec(Requests.getPushList).pause(0.2.seconds),

          // ДОСТАВКА И МАГАЗИНЫ (0.7%)
          0.14 -> exec(Requests.choiseIntervals).pause(0.2.seconds),
          0.11 -> exec(Requests.getShopsList).pause(0.2.seconds),
          0.28 -> exec(Requests.getPartnersList).pause(0.3.seconds),

          // КАТЕГОРИИ И ПОДДЕРЖКА (0.3%)
          0.16 -> exec(Requests.getLoyaltyCategories).pause(0.2.seconds),
          0.16 -> exec(Requests.getSupportService).pause(0.2.seconds),

          // ОБНОВЛЕНИЯ И ТЕСТЫ (1.2%)
          0.53 -> exec(Requests.updateSettings).pause(0.2.seconds),
          0.53 -> exec(Requests.updateEreceipt).pause(0.2.seconds),
          0.53 -> exec(Requests.testFunction).pause(0.2.seconds),
          0.53 -> exec(Requests.scangoCart).pause(0.2.seconds)
          // Пауза между итерациями (обязательна!)
        ).pause(0.5.seconds)
      }
    }
}
