package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import utils.{Feeders, config, lgcRequests}

import scala.concurrent.duration._
import scala.util.Random

object LegacyTask {

//  val reauthorizeChain: ChainBuilder = exec(session => {
//    println(s"[REAUTH] Токен истёк или невалиден. Переавторизация...")
//    session.remove("token")
//  }).exec(lgcRequests.getToken)

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
    exec(lgcRequests.getToken)

  // ============ ЦЕПОЧКА 2: АДРЕСА (выполняются один раз и сохраняют данные) ============
  val addressChain: ChainBuilder =
    exec(lgcRequests.getUserAddresses)
      // ===== СЛУЧАЙ 1: НЕТ АДРЕСОВ - СОЗДАЁМ =====
      .doIf(session =>
        !session.contains("idAddress") ||
          session("idAddress").asOption[Any].isEmpty
      ) {
        exec(lgcRequests.createAddress)
          .exec(lgcRequests.choiceAddress)
      }
      // ===== СЛУЧАЙ 2: ЕСТЬ АДРЕСА - ВЫБИРАЕМ ОДИН =====
      .doIf(session =>
        session.contains("idAddress") &&
          session("idAddress").asOption[Any].isDefined
      ) {
        exec(lgcRequests.choiceAddress)
      }


  // Цепочка: TSD авторизация (опциональна, выполняется один раз)
  val tsdAuthChain: ChainBuilder =
    exec(lgcRequests.getTokenTSD)



  // Сценарий
  val scn: ScenarioBuilder = scenario("LegacyTask - preprod legacy Load")
    .feed(Feeders.userLgc)
    .feed(Feeders.device)
    .feed(Feeders.shop)
    .feed(Feeders.shopNN)

  // ============ ШАГ 1: ОСНОВНАЯ АВТОРИЗАЦИЯ ============
    .exec(tsdAuthChain).pause(0.3.seconds)

    .exec(authChain)
    .pause(0.2.seconds)

  // ============ ШАГ 2: ВЫБОР АДРЕСА И ИНТЕРВАЛА ============

    .exec(addressChain)
    .pause(0.3.seconds)

    .exec(pickRandomInterval)
    .pause(0.2.seconds)

    // ============ ШАГ : TSD ============
//    .doIf(session => session.contains("tsd_token")) {
//      exec(tsdAuthChain).pause(0.3.seconds)
//    }

    // ============ ШАГ 3: СЛУЧАЙНЫЙ ТРАФИК ============
    .doIf(session => session.contains("token")) {
      during(60.seconds) { // ← можно менять число на желаемое (30, 60, 120 и т.д.)
        randomSwitch(
          // Веса пересчитаны в проценты (сумма ~100%)
//          0.63 -> exec(addressChain).pause(0.3.seconds),
          1.29 -> exec(lgcRequests.getUserAddresses).pause(0.3.seconds),

          // ===== TSD ИНФОРМАЦИЯ (9.76%, только если есть tsd_token) =====
//          9.76 -> doIf(session => session.contains("tsd_token")) {
//            exec(lgcRequests.getTsdInfo).pause(0.1.seconds)
//          },
          9.79 -> exec(lgcRequests.getTsdInfo).pause(0.1.seconds),

          // ===== КРИТИЧНЫЕ ОПЕРАЦИИ (38%) =====
          10.92 -> exec(lgcRequests.getPushTokenLgc).pause(0.2.seconds),
          1.95 -> exec(lgcRequests.addToBasket).pause(0.2.seconds),
          5.35 -> exec(lgcRequests.getPlaceBasket).pause(0.3.seconds),
          6.54 -> exec(lgcRequests.searchCatalog).pause(0.2.seconds),
          2.92 -> exec(lgcRequests.getEmptyBasket).pause(0.2.seconds),
          // ===== КАТАЛОГ И КОМБО (12%) =====
          5.16 -> exec(lgcRequests.getCombo).pause(0.3.seconds),
          3.02 -> exec(lgcRequests.getBrands).pause(0.2.seconds),
          3.11 -> exec(lgcRequests.getIndex).pause(0.3.seconds),
          1.04 -> exec(lgcRequests.getRecommendations).pause(0.3.seconds),
          1.09 -> exec(lgcRequests.getProduct).pause(0.2.seconds),
          1.58 -> exec(lgcRequests.getCatalogBanners).pause(0.2.seconds),

          // ===== ИНФОРМАЦИЯ О ГОРОДЕ И СТАТУС (11%) =====
          0.89 -> exec(lgcRequests.getCitiesList).pause(0.2.seconds),
          6.44 -> exec(lgcRequests.geoCity).pause(0.3.seconds),
          2.87 -> exec(lgcRequests.getOrderStatus).pause(0.2.seconds),
          2.87 -> exec(lgcRequests.getLoyaltyInfo).pause(0.2.seconds),

          // ===== БАННЕРЫ, СТОРИС, ПОПАПЫ (8%) =====
          3.10 -> exec(lgcRequests.getStories).pause(0.2.seconds),
          2.96 -> exec(lgcRequests.getBanners).pause(0.1.seconds),
          2.27 -> exec(lgcRequests.getPopups).pause(0.3.seconds),

          // ===== ПРОФИЛЬ И КАРТОЧКА (3.5%) =====
          1.28 -> exec(lgcRequests.getUser).pause(0.3.seconds),
          0.64 -> exec(lgcRequests.getUser).pause(0.2.seconds),
          0.64 -> exec(lgcRequests.updateSettingsLgc).pause(0.2.seconds),
          0.64 -> exec(lgcRequests.getUser).pause(0.2.seconds),
          0.53 -> exec(lgcRequests.getUser).pause(0.2.seconds),
          0.53 -> exec(lgcRequests.getUser).pause(0.2.seconds),

          // ===== ОТЗЫВЫ И РЕЙТИНГИ (2%) =====
          2.05 -> exec(lgcRequests.notRatedOrders).pause(0.3.seconds),
          1.31 -> exec(lgcRequests.getReviews).pause(0.2.seconds),
          1.49 -> exec(lgcRequests.getReviewPoints).pause(0.3.seconds),

          // ===== ПОИСК И ИСТОРИЯ (1.5%) =====
          1.99 -> exec(lgcRequests.addSearchHistory).pause(0.2.seconds),
          0.49 -> exec(lgcRequests.getSearchHistory).pause(0.2.seconds),

          // ===== КОРЗИНА И ЗАКАЗЫ (1.8%) =====
          1.02 -> exec(lgcRequests.getOrderDetail).pause(0.3.seconds),
          0.46 -> exec(lgcRequests.getOrdersList).pause(0.3.seconds),
          0.27 -> exec(lgcRequests.getPurchasesList).pause(0.2.seconds),

          // ===== СОБЫТИЯ И БАННЕРЫ (2%) =====
          1.65 -> exec(lgcRequests.setEvent).pause(0.2.seconds),
          0.26 -> exec(lgcRequests.getOtherBanner).pause(0.2.seconds),

          // ===== QR И РАЗНОЕ (1.5%) =====
          1.80 -> exec(lgcRequests.getQrlgc).pause(0.3.seconds),
          1.38 -> exec(lgcRequests.getUserAddresses).pause(0.3.seconds),
          0.98 -> exec(lgcRequests.getItemsFeed).pause(0.2.seconds),

          // ===== УВЕДОМЛЕНИЯ И СКИДКИ (1%) =====
          0.59 -> exec(lgcRequests.getPushDiscounts).pause(0.2.seconds),
          0.18 -> exec(lgcRequests.getPushList).pause(0.2.seconds),

          // ===== ДОСТАВКА И МАГАЗИНЫ (0.7%) =====
          0.14 -> exec(lgcRequests.choiseIntervals).pause(0.2.seconds),
          0.11 -> exec(lgcRequests.getShopsList).pause(0.2.seconds),
          0.28 -> exec(lgcRequests.getPartnersList).pause(0.3.seconds),

          // ===== КАТЕГОРИИ И ПОДДЕРЖКА (0.3%) =====
          0.16 -> exec(lgcRequests.getLoyaltyCategories).pause(0.2.seconds),
          0.16 -> exec(lgcRequests.getSupportService).pause(0.2.seconds),

          // ===== ОБНОВЛЕНИЯ И ТЕСТЫ (1.2%) =====
          0.53 -> exec(lgcRequests.getUser).pause(0.2.seconds),
          0.53 -> exec(lgcRequests.updateSettingsLgc).pause(0.2.seconds),
          0.53 -> exec(lgcRequests.getUser).pause(0.2.seconds),
          0.53 -> exec(lgcRequests.scangoCart).pause(0.2.seconds)
          // Пауза между итерациями (обязательна!)
        ).pause(0.5.seconds)
      }
    }
}
