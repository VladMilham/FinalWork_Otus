package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.UseScenarios
import utils.config

import scala.concurrent.duration._

class Debug extends Simulation {

  // ОПРЕДЕЛЕНИЕ HTTP КОНФИГА
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(config.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .disableAutoReferer

  // СЦЕНАРИЙ
  val scn: ScenarioBuilder = UseScenarios.scn

  setUp(
    scn.inject(
      atOnceUsers(1),              // Запускаем 1 пользователя сразу
      // или
      rampUsers(5).during(10.seconds) // Запускаем 5 пользователей плавно за 10 сек
    )
  ).protocols(httpProtocol)
}
