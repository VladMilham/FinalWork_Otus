package utils

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.http.Predef._

import scala.io.Source
import scala.util.Using

object Feeders {


  val user: BatchableFeederBuilder[String] = csv("users.csv").circular
  val userLgc: BatchableFeederBuilder[String] = csv("users_lgc.csv").circular
  val shop: BatchableFeederBuilder[String] = csv("shops.csv").random
  val city: BatchableFeederBuilder[String] = csv("cities.csv").random
  val card: BatchableFeederBuilder[String] = csv("cards.csv").random
  val shopNN: BatchableFeederBuilder[String] = csv("shops_NN.csv").random
  val device: BatchableFeederBuilder[String] = csv("devices.csv").random




}
