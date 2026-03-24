package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.UseScenarios
import utils.config

import scala.concurrent.duration._

class stab extends Simulation {

  // ============ ОПРЕДЕЛЕНИЕ HTTP КОНФИГА ============
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(config.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .disableAutoReferer

  // ============ СЦЕНАРИЙ ============
    val scn: ScenarioBuilder = UseScenarios.scn

  // ============ SETUP ============
  setUp(
    scn.inject(
      // Разогрев: плавно выходим на рабочую нагрузку за N минут
      rampUsersPerSec(0).to(0.5).during(200.seconds),

      // Основная часть: держим нагрузку ровной полкой долгое время
      constantUsersPerSec(0.5).during(60.minutes)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.percentile(95).lt(3000), // Если 95% ответов дольше 3 сек — fail
      global.successfulRequests.percent.gt(99)     // Если ошибок > 1% — fail
    )
}
