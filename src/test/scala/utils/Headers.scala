package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Headers {

  // Общие заголовки для всех запросов MSA (используется токен из сессии)
  val commonHeaders = Map(
    "Content-Type" -> "application/json; charset=utf-8",
    "Accept-Charset" -> "utf-8",
    "Accept"        -> "application/json",
    "X-API-KEY"     -> "098f6bcd4621d373cade4e832627b4f6",
    "X-APP-VERSION" -> "2",
    "Version"       -> "2025-05-22_01",
    "Authorization" -> "Bearer #{token}"
  )

  // Общие заголовки для всех запросов Legacy (используется токен из сессии)
  val commonHeadersLgc = Map(
    "Content-Type" -> "application/json; charset=utf-8",
    "Accept-Charset" -> "utf-8",
    "Accept"        -> "application/json",
    "Version"       -> "2025-05-22_01",
    "Authorization" -> "Bearer #{token}"
  )

  // Заголовки для авторизации (первичный запрос токена)
  val authHeaders = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json",
    "X-AUTH-TOKEN"  -> "xW$5V8jAVcdF5O9ihuxsp%oNOOj!Q"
  )

  val authHeadersLgc = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json"
  )

  // Заголовки для TSD авторизации
  val tsdAuthHeaders = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json",
    "X-AUTH-TOKEN"  -> "xW$5V8jAVcdF5O9ihuxsp%oNOOj!Q"
  )

  // Заголовки для TSD запросов (с TSD токеном)
  val tsdHeaders = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json",
    "X-API-KEY"     -> "098f6bcd4621d373cade4e832627b4f6",
    "X-APP-VERSION" -> "2",
    "Version"       -> "2025-05-22_01",
    "Authorization" -> "Bearer #{tsd_token}"
  )

  // Заголовки для купонов (с jwt-payload)
  val couponsHeaders = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json",
    "X-API-KEY"     -> "098f6bcd4621d373cade4e832627b4f6",
    "X-APP-VERSION" -> "2",
    "Version"       -> "2025-05-22_01",
    "Authorization" -> "Bearer #{token}",
    "x-jwt-payload" -> """{"identifier": "79234050510", "bitrixId": 18633269, "externalId": "cce64d4f-1188-4485-9498-8568335fd088", "isActive": true}"""
  )

  // GET запросы с пустым телом (стандартные headers)
  val getHeaders = commonHeaders

  // POST/PUT/PATCH запросы (стандартные headers)
  val postHeaders = commonHeaders

  // Для запросов которые НЕ требуют авторизацию (если такие есть)
  val publicHeaders = Map(
    "Content-Type"  -> "application/json",
    "Accept"        -> "application/json",
    "X-API-KEY"     -> "098f6bcd4621d373cade4e832627b4f6",
    "X-APP-VERSION" -> "2",
    "Version"       -> "2025-05-22_01"
  )

}
