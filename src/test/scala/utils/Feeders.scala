package utils

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.http.Predef._

import scala.io.Source
import scala.util.Using

object Feeders {

//  def loadCsv(filename: String): Vector[Map[String, String]] = {
//    Using(Source.fromFile(filename)) { source =>
//      val lines = source.getLines().toList
//      if (lines.isEmpty) Vector()
//      else {
//        val headers = lines.head.split(",").map(_.trim)
//        lines.tail
//          .filter(_.trim.nonEmpty)
//          .map { line =>
//            val values = line.split(",").map(_.trim)
//            (headers zip values).toMap
//          }
//          .toVector
//      }
//    }.get
//  }
//
//  lazy val users: Vector[Map[String, String]] = loadCsv(config.csvPaths("users"))
//  lazy val shops: Vector[Map[String, String]] = loadCsv(config.csvPaths("shops"))
//  lazy val cities: Vector[Map[String, String]] = loadCsv(config.csvPaths("cities"))
//  val shopsNN: Vector[Map[String, String]] = loadCsv(config.csvPaths("shopsNN"))
//  lazy val externalIds: Vector[Map[String, String]] = loadCsv(config.csvPaths("externalIds"))
//  lazy val cards: Vector[Map[String, String]] = loadCsv(config.csvPaths("cards"))
//
//  def getUsers: Vector[Map[String, String]] = users
//  def getShops: Vector[Map[String, String]] = shops
//  def getCities: Vector[Map[String, String]] = cities
//  def getShopsNN: Vector[Map[String, String]] = shopsNN
//  def getExternalIds: Vector[Map[String, String]] = externalIds
//  def getCards: Vector[Map[String, String]] = cards

  val user: BatchableFeederBuilder[String] = csv("users.csv").circular
  val userLgc: BatchableFeederBuilder[String] = csv("users_lgc.csv").circular
  val shop: BatchableFeederBuilder[String] = csv("shops.csv").random
  val city: BatchableFeederBuilder[String] = csv("cities.csv").random
  val card: BatchableFeederBuilder[String] = csv("cards.csv").random
  val shopNN: BatchableFeederBuilder[String] = csv("shops_NN.csv").random
  val device: BatchableFeederBuilder[String] = csv("devices.csv").random




}
