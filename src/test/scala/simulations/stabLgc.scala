package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.{LegacyTask, UseScenarios}
import utils.config

import scala.concurrent.duration._

class stabLgc extends Simulation {

  // ============ ОПРЕДЕЛЕНИЕ HTTP КОНФИГА ============
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(config.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .disableAutoReferer

  // ============ СЦЕНАРИЙ ============
    val scn: ScenarioBuilder = LegacyTask.scn

  // ============ SETUP ============
  setUp(
    scn.inject(
      // Разогрев: плавно выходим на рабочую нагрузку за 5 минут
      rampUsersPerSec(0).to(4.2).during(200.seconds),
//            rampUsers(150).during(500.seconds),

      // Основная часть: держим нагрузку ровной полкой долгое время
      constantUsersPerSec(4.2).during(60.minutes)
    )
  ).protocols(httpProtocol)
}
