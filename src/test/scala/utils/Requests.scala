package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import utils.Headers._

object Requests {


  def getToken: HttpRequestBuilder = http("getToken")
    .post("/api/appv2/spar-auth/token/create")
    .body(ElFileBody("bodies/login.json"))
    .check(status.is(200))
    .headers(authHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jsonPath("$.data.tokenInfo.token").saveAs("token"))

  def getTokenTSD: HttpRequestBuilder = http("getTokenTSD")
    .post("/api/token/get/")
    .body(ElFileBody("bodies/loginTSD.json"))
    .check(status.is(200))
    .headers(tsdAuthHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jsonPath("$.data.access_token").saveAs("tsd_token"))


  //Получение списка пуш-токенов
  val getPushToken: HttpRequestBuilder = http("getPushToken")
    .get("/api/appv2/spar-auth/device/push-tokens")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


  //Работа с корзиной
  val addToBasket: HttpRequestBuilder = http("addToBasket")
    .post("/api/basket/")
    .body(ElFileBody("bodies/addToBasket.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


  val getEmptyBasket: HttpRequestBuilder = http("getEmptyBasket")
    .post("/api/basket/order/")
    .body(ElFileBody("bodies/emptyBasket.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getPlaceBasket: HttpRequestBuilder = http("getPlaceBasket")
    .post("/api/basket/order/")
    .body(ElFileBody("bodies/emptyBasket.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  //Отрефачить и доделать параметры
//  val createOrder: HttpRequestBuilder = http("createOrder")
//    .post("/api/basket/order/")
//    .body(ElFileBody("bodies/createOrder.json"))
//    .check(status.is(200))
//    .headers(commonHeaders)
//    .check(jmesPath("result").ofType[Boolean].is(true))
//    .check(jmesPath("data[0].id")
//      .exists
//      .saveAs("idOrderNew")
//    )


  //Работа с заказами и покупками
  val getOrderStatus: HttpRequestBuilder = http("getOrderStatus")
    .post("/api/orders/status/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val orderCancel: HttpRequestBuilder = http("orderCancel")
    .post("/api/orders/cancel/")
    .body(ElFileBody("bodies/orderCancel.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getOrdersList: HttpRequestBuilder = http("getOrdersList ")
    .post("/api/orders/list/")
    .body(ElFileBody("bodies/orderList.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.items[0].id").optional.saveAs("idOrder")
    )

  val getOrderDetail: HttpRequestBuilder = http("getOrderDetail")
    .post("/api/orders/detail/")
    .body(ElFileBody("bodies/orderDetail.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val notRatedOrders: HttpRequestBuilder = http("notRatedOrders")
    .post("/api/orders/notrated/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getPurchasesList: HttpRequestBuilder = http("getPurchasesList")
    .post("/api/purchases/list/")
    .body(StringBody("""{"page":1}""")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.items[0].id").optional
      .saveAs("idPurchases")
    )

//  val choicePurchase: HttpRequestBuilder = http("choicePurchase")
//    .post("/api/purchases/detail/")
//    .body(StringBody("""{"id":"#{idPurchases}"}""")).asJson
//    .check(status.is(200))
//    .headers(commonHeaders)
//    .check(jmesPath("result").ofType[Boolean].is(true))



  //Гео, адреса и интервалы
  val getCitiesList: HttpRequestBuilder = http("getCitiesList")
    .get("/api/geo/cities/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.items[].id")
      .saveAs("idCity")
    )

  val geoCity: HttpRequestBuilder = http("geoCity")
    .post("/api/geo/city/")
    .body(ElFileBody("bodies/geoCity.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val choiseIntervals: HttpRequestBuilder = http("choiseIntervals")
    .post("/api/geo/intervals/")
    .body(ElFileBody("bodies/choiseIntervals.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getUserAddresses: HttpRequestBuilder = http("getUserAddresses")
    .post("/api/user/address/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.items.list[].id")
      .optional
      .saveAs("idAddress")
    )

  val createAddress: HttpRequestBuilder = http("createAddress")
    .post("/api/user/address/")
    .body(ElFileBody("bodies/createAddress.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.id")
      .saveAs("idAddress")
    )

  val choiceAddress: HttpRequestBuilder = http("choiceAddress")
    .post("/api/user/address/")
    .body(ElFileBody("bodies/choiceAddress.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))
    .check(jmesPath("data.types[0].delivery_type").saveAs("deliveryType"))
    .check(jmesPath("data.types[0].city_id").saveAs("cityId"))
    .check(jmesPath("data.types[0].days[1].date").saveAs("date"))
    .check(jmesPath("data.types[0].days[1].intervals[*].value").saveAs("intervals"))
    .check(jmesPath("data.types[0].shop_id").saveAs("idShop")
    )

  val getShopsList: HttpRequestBuilder = http("getShopsList")
    .post("/api/shops/list/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


// Работа с каталогом
val searchCatalog: HttpRequestBuilder = http("searchCatalog")
  .post("/api/catalog/list/")
  .body(ElFileBody("bodies/catalogList.json"))
  .check(status.is(200))
  .headers(commonHeaders)
  .check(jmesPath("result").ofType[Boolean].is(true))

  val getCombo: HttpRequestBuilder = http("getCombo")
    .post("/api/catalog/combo/")
    .body(ElFileBody("bodies/catalogCombo.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getBrands: HttpRequestBuilder = http("getBrands")
    .post("/api/catalog/brands/")
    .body(ElFileBody("bodies/catalogBrands.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getIndex: HttpRequestBuilder = http("getIndex")
    .post("/api/catalog/index/")
    .body(ElFileBody("bodies/catalogIndex.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getItemsFeed: HttpRequestBuilder = http("getItemsFeed")
    .post("/api/catalog/items_feed/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getCatalogBanners: HttpRequestBuilder = http("getCatalogBanners")
    .post("/api/banners/catalog/")
    .body(StringBody("""{"shop": "#{idShop}", "section": "62"}""")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

//Карточка товара
  val getProduct: HttpRequestBuilder = http("getProduct")
    .post("/api/catalog/product/")
    .body(ElFileBody("bodies/catalogProduct.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getRecommendations: HttpRequestBuilder = http("getRecommendations")
    .post("/api/catalog/recommendations/")
    .body(ElFileBody("bodies/catalogRecommendations.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getReviews: HttpRequestBuilder = http("getReviews")
    .post("/api/reviews/list/")
    .body(ElFileBody("bodies/getReviews.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val addSearchHistory: HttpRequestBuilder = http("addSearchHistory")
    .post("/api/search/history/add/")
    .body(ElFileBody("bodies/addSearchHistory.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getSearchHistory: HttpRequestBuilder = http("getSearchHistory")
    .post("/api/search/history/list/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


//Работа с данными юзера
val getPersonalData: HttpRequestBuilder = http("getPersonalData")
  .get("/api/appv2/spar-customer/customers/personal-data")
  .check(status.is(200))
  .headers(commonHeaders)
  .check(jmesPath("result").ofType[Boolean].is(true))

  val getSettings: HttpRequestBuilder = http("getSettings")
    .get("/api/appv2/spar-customer/customers/settings")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val updateSettings: HttpRequestBuilder = http("updateSettings")
    .put("/api/appv2/spar-customer/customers/settings")
    .body(ElFileBody("bodies/updateSettings.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val updateEreceipt: HttpRequestBuilder = http("updateEreceipt")
    .patch("/api/appv2/spar-customer/customers/ereceipt")
    .body(ElFileBody("bodies/updateEreceipt.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getNoticesCount: HttpRequestBuilder = http("getNoticesCount")
    .get("/api/appv2/spar-customer/customers/notices-count")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val checkCardActivation: HttpRequestBuilder = http("checkCardActivation")
    .get("/api/appv2/spar-customer/cards/activation")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getCardInfo: HttpRequestBuilder = http("getCardInfo")
    .get("/api/appv2/spar-customer/cards/info")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getCardBalance: HttpRequestBuilder = http("getCardBalance")
    .get("/api/appv2/spar-customer/cards/balance")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


  val testFunction: HttpRequestBuilder = http("testFunction")
    .get("/api/appv2/spar-customer/customers/test-function")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


  //Banners and Popups
  val getBanners: HttpRequestBuilder = http("getBanners")
    .get("/api/other/banners/")
//    .body(ElFileBody("bodies/getBannersPopups.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getPopups: HttpRequestBuilder = http("getPopups")
    .get("/api/other/popups/")
//    .body(ElFileBody("bodies/getBannersPopups.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getOtherBanner: HttpRequestBuilder = http("getOtherBanner")
    .post("/api/other/banner/")
    .body(ElFileBody("bodies/getOtherBanner.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

    //Уведомления и скидки
    val getPushDiscounts: HttpRequestBuilder = http("getPushDiscounts")
      .get("/api/push/discounts/")
      .check(status.is(200))
      .headers(commonHeaders)
      .check(jmesPath("result").ofType[Boolean].is(true))

  val getPushList: HttpRequestBuilder = http("getPushList")
    .get("/api/push/list/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


  //Главный экран
  val getLoyaltyInfo: HttpRequestBuilder = http("getLoyaltyInfo")
    .post("/api/loyalty/info/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getLoyaltyCategories: HttpRequestBuilder = http("getLoyaltyCategories")
    .get("/api/loyalty/categories/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getStories: HttpRequestBuilder = http("getStories")
    .post("/api/stories/list/")
    .body(StringBody("""{"region": "Пензенская область", "city": "Пенза"}""")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getQr: HttpRequestBuilder = http("getQr")
    .get("/api/appv2/spar-customer/qr")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getReviewPoints: HttpRequestBuilder = http("getReviewPoints")
    .post("/api/other/review_points_main/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val subscriptionList: HttpRequestBuilder = http("subscriptionList")
    .get("/api/subscription/list/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))


//Прочее
  val getPartnersList: HttpRequestBuilder = http("getPartnersList")
    .post("/api/partners/list/")
    .body(StringBody("""{"type": "restaurants", "city_id": "#{city_id}"}""")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val scangoCart: HttpRequestBuilder = http("scangoCart")
    .post("/api/scango/cart/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .headers(commonHeaders)

  val handlePopup: HttpRequestBuilder = http("handlePopup")
    .post("/api/appv2/spar-banner/popup")
    .body(StringBody("""{"action": "item", "specifics": "123456"}""")).asJson
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getSupportService: HttpRequestBuilder = http("getSupportService")
    .get("/api/support_service/")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val setEvent: HttpRequestBuilder = http("setEvent")
    .post("/api/events/set/")
    .body(ElFileBody("bodies/setEvent.json"))
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getTsdInfo: HttpRequestBuilder = http("getTsdInfo")
    .post("/api/tsd/getinfo/")
    .headers(tsdHeaders)
    .body(StringBody(
      """{
        "checks": [
          {"check_type": "active_orders"},
          {"check_type": "food_cooking"}
        ]
      }""")).asJson

  val getLoyaltyCustomer: HttpRequestBuilder = http("getLoyaltyCustomer")
    .get("/api/appv2/spar-customer/customers/counters/f5307f30-9176-4528-9fce-5c46c4bbde00")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val getAttributesCustomer: HttpRequestBuilder = http("getAttributesCustomer")
    .get("/api/appv2/spar-customer/customers/attributes")
    .check(status.is(200))
    .headers(commonHeaders)
    .check(jmesPath("result").ofType[Boolean].is(true))

  val patchAttributesCustomer: HttpRequestBuilder = http("patchAttributesCustomer")
    .patch("/api/appv2/spar-customer/customers/attributes")
    .check(status.is(200))
    .headers(commonHeaders)
    .body(StringBody(
      """{
  "attributes": [
      {
        "templateId": "da468132-7f08-4681-b943-9aad26f01ee0",
        "type": "MULTIPLE_STRING",
        "values": ["1b28c070-3851-4da2-93c8-4ee73f17320d"]
      }
  ],
  "source": "MOBILE_APP"
}""")).asJson

}


