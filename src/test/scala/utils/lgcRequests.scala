package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import utils.Headers._
import utils.config

object lgcRequests {


  def getToken: HttpRequestBuilder = http("getToken")
    .post("/api/token/get/")
    .body(ElFileBody("bodies/lgc_login.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(authHeadersLgc)
    .check(jsonPath("$.data.access_token").saveAs("token"))

  val getUser: HttpRequestBuilder = http("getUser")
    .get("/api/user/profile/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val updateSettingsLgc: HttpRequestBuilder = http("updateSettingsLgc")
    .put("/api/user/params/")
    .body(ElFileBody("bodies/userParams.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  //Получение списка пуш-токенов
  val getPushTokenLgc: HttpRequestBuilder = http("getPushTokenLgc")
    .get("/api/user/pushtokens/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getQrlgc: HttpRequestBuilder = http("getQrlgc")
    .get("/api/user/qr")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  def getTokenTSD: HttpRequestBuilder = http("getTokenTSD")
    .post("https://tsd.myspar.ru/api/token/get/")
    .body(ElFileBody("bodies/loginTSD.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(tsdAuthHeaders)
    .check(jsonPath("$.data.access_token").saveAs("tsd_token"))


  //Получение списка пуш-токенов
  val getPushToken: HttpRequestBuilder = http("getPushToken")
    .get("/api/appv2/spar-auth/device/push-tokens")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  //Работа с корзиной
  val addToBasket: HttpRequestBuilder = http("addToBasket")
    .post("/api/basket/")
    .body(ElFileBody("bodies/addToBasket.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  val getEmptyBasket: HttpRequestBuilder = http("getEmptyBasket")
    .post("/api/basket/order/")
    .body(ElFileBody("bodies/emptyBasket.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getPlaceBasket: HttpRequestBuilder = http("getPlaceBasket")
    .post("/api/basket/order/")
    .body(ElFileBody("bodies/emptyBasket.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  //Отрефачить и доделать параметры
  //  val createOrder: HttpRequestBuilder = http("createOrder")
  //    .post("/api/basket/order/")
  //    .body(ElFileBody("bodies/createOrder.json"))
  //    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
  //    .headers(commonHeadersLgc)
  //    .check(bodyString.saveAs("lastResponse"))
  //    .check(jmesPath("data[0].id")
  //      .exists
  //      .saveAs("idOrderNew")
  //    )


  //Работа с заказами и покупками
  val getOrderStatus: HttpRequestBuilder = http("getOrderStatus")
    .post("/api/orders/status/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val orderCancel: HttpRequestBuilder = http("orderCancel")
    .post("/api/orders/cancel/")
    .body(ElFileBody("bodies/orderCancel.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getOrdersList: HttpRequestBuilder = http("getOrdersList ")
    .post("/api/orders/list/")
    .body(ElFileBody("bodies/orderList.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
    .check(jmesPath("data.items[0].id").optional.saveAs("idOrder")
    )

  val getOrderDetail: HttpRequestBuilder = http("getOrderDetail")
    .post("/api/orders/detail/")
    .body(ElFileBody("bodies/orderDetail.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val notRatedOrders: HttpRequestBuilder = http("notRatedOrders")
    .post("/api/orders/notrated/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getPurchasesList: HttpRequestBuilder = http("getPurchasesList")
    .post("/api/purchases/list/")
    .body(StringBody("""{"page":1}""")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
    .check(jmesPath("data.items[0].id").optional
      .saveAs("idPurchases")
    )

  //  val choicePurchase: HttpRequestBuilder = http("choicePurchase")
  //    .post("/api/purchases/detail/")
  //    .body(StringBody("""{"id":"#{idPurchases}"}""")).asJson
  //    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
  //    .headers(commonHeadersLgc)
  //    .check(bodyString.saveAs("lastResponse"))



  //Гео, адреса и интервалы
  val getCitiesList: HttpRequestBuilder = http("getCitiesList")
    .get("/api/geo/cities/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
    .check(jmesPath("data.items[].id")
      .saveAs("idCity")
    )

  val geoCity: HttpRequestBuilder = http("geoCity")
    .post("/api/geo/city/")
    .body(ElFileBody("bodies/geoCity.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val choiseIntervals: HttpRequestBuilder = http("choiseIntervals")
    .post("/api/geo/intervals/")
    .body(ElFileBody("bodies/choiseIntervals.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getUserAddresses: HttpRequestBuilder = http("getUserAddresses")
    .post("/api/user/address/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
    .check(jmesPath("data.list[0].id")
      .optional
      .saveAs("idAddress")
    )

  val createAddress: HttpRequestBuilder = http("createAddress")
    .post("/api/user/address/")
    .body(ElFileBody("bodies/createAddress.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
    .check(jmesPath("data[0].id")
      .saveAs("idAddress")
    )

  val choiceAddress: HttpRequestBuilder = http("choiceAddress")
    .post("/api/user/address/")
    .body(ElFileBody("bodies/choiceAddress.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)
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
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  // Работа с каталогом
  val searchCatalog: HttpRequestBuilder = http("searchCatalog")
    .post("/api/catalog/list/")
    .body(ElFileBody("bodies/catalogList.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getCombo: HttpRequestBuilder = http("getCombo")
    .post("/api/catalog/combo/")
    .body(ElFileBody("bodies/catalogCombo.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getBrands: HttpRequestBuilder = http("getBrands")
    .post("/api/catalog/brands/")
    .body(ElFileBody("bodies/catalogBrands.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getIndex: HttpRequestBuilder = http("getIndex")
    .post("/api/catalog/index/")
    .body(ElFileBody("bodies/catalogIndex.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getItemsFeed: HttpRequestBuilder = http("getItemsFeed")
    .post("/api/catalog/items_feed/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getCatalogBanners: HttpRequestBuilder = http("getCatalogBanners")
    .post("/api/banners/catalog/")
    .body(StringBody("""{"shop": "#{idShop}", "section": "62"}""")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  //Карточка товара
  val getProduct: HttpRequestBuilder = http("getProduct")
    .post("/api/catalog/product/")
    .body(ElFileBody("bodies/catalogProduct.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getRecommendations: HttpRequestBuilder = http("getRecommendations")
    .post("/api/catalog/recommendations/")
    .body(ElFileBody("bodies/catalogRecommendations.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getReviews: HttpRequestBuilder = http("getReviews")
    .post("/api/reviews/list/")
    .body(ElFileBody("bodies/getReviews.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val addSearchHistory: HttpRequestBuilder = http("addSearchHistory")
    .post("/api/search/history/add/")
    .body(ElFileBody("bodies/addSearchHistory.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getSearchHistory: HttpRequestBuilder = http("getSearchHistory")
    .post("/api/search/history/list/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  //Banners and Popups
  val getBanners: HttpRequestBuilder = http("getBanners")
    .get("/api/other/banners/")
    //    .body(ElFileBody("bodies/getBannersPopups.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getPopups: HttpRequestBuilder = http("getPopups")
    .get("/api/other/popups/")
    //    .body(ElFileBody("bodies/getBannersPopups.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getOtherBanner: HttpRequestBuilder = http("getOtherBanner")
    .post("/api/other/banner/")
    .body(ElFileBody("bodies/getOtherBanner.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  //Уведомления и скидки
  val getPushDiscounts: HttpRequestBuilder = http("getPushDiscounts")
    .get("/api/push/discounts/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getPushList: HttpRequestBuilder = http("getPushList")
    .get("/api/push/list/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  //Главный экран
  val getLoyaltyInfo: HttpRequestBuilder = http("getLoyaltyInfo")
    .post("/api/loyalty/info/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getLoyaltyCategories: HttpRequestBuilder = http("getLoyaltyCategories")
    .get("/api/loyalty/categories/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getStories: HttpRequestBuilder = http("getStories")
    .post("/api/stories/list/")
    .body(StringBody("""{"region": "Пензенская область", "city": "Пенза"}""")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getReviewPoints: HttpRequestBuilder = http("getReviewPoints")
    .post("/api/other/review_points_main/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val subscriptionList: HttpRequestBuilder = http("subscriptionList")
    .get("/api/subscription/list/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)


  //Прочее
  val getPartnersList: HttpRequestBuilder = http("getPartnersList")
    .post("/api/partners/list/")
    .body(StringBody("""{"type": "restaurants", "city_id": "#{cityId}"}""")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val scangoCart: HttpRequestBuilder = http("scangoCart")
    .post("/api/scango/cart/")
    .body(StringBody("{}")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val handlePopup: HttpRequestBuilder = http("handlePopup")
    .post("/api/appv2/spar-banner/popup")
    .body(StringBody("""{"action": "item", "specifics": "123456"}""")).asJson
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getSupportService: HttpRequestBuilder = http("getSupportService")
    .get("/api/support_service/")
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val setEvent: HttpRequestBuilder = http("setEvent")
    .post("/api/events/set/")
    .body(ElFileBody("bodies/setEvent.json"))
    .check(status.is(200))
    .check(jsonPath("$.error.code").optional.saveAs("errorCode"))
    .headers(commonHeadersLgc)

  val getTsdInfo: HttpRequestBuilder = http("getTsdInfo")
    .post("https://tsd.myspar.ru/api/tsd/getinfo/")
    .headers(tsdHeaders)
    .body(StringBody(
      """{
        "checks": [
          {"check_type": "active_orders"},
          {"check_type": "food_cooking"}
        ]
      }""")).asJson
  
}
