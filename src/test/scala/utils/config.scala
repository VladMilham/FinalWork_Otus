package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object config {
  // URLs
  val httpProtocol: HttpProtocolBuilder = http
  val baseUrl: String = "https://preprod.myspar.ru"
  val authUrl: String = "$baseUrl/api/appv2/spar-auth/token/create"
  val tsdAuthUrl: String = "$baseUrl/api/token/get/"

  // API Keys
  val authToken: String = "xW$5V8jAVcdF5O9ihuxsp%oNOOj!Q"
  val apiKey: String = "098f6bcd4621d373cade4e832627b4f6"
  val appVersion: String = "2"
  val versionDate: String = "2025-05-22_01"

  // Timeouts & Intervals
  val tokenRefreshInterval: Int = 600000  // 10 minutes in milliseconds
//  val requestTimeout: Int = 10000         // 10 seconds
//  val pacing: Int = 100                   // 100ms between requests

  // Default Data
  val defaultCityId: Int = 312
  val defaultCity: String = "Нижний Новгород"
  val defaultRegion: String = "Нижегородская область"
  val defaultPhone: String = "70391111111"

  // CSV Files
  val csvPaths: Map[String, String] = Map(
    "users"        -> "users.csv",
    "shops"        -> "shops.csv",
    "cities"       -> "cities.csv",
    "shopsNN"      -> "shops_NN.csv",
    "externalIds"  -> "external_ids_test.csv",
    "cards"        -> "cards.csv"
  )
}
