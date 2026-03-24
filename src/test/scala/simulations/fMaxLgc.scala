package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.{LegacyTask, UseScenarios}
import utils.config

import scala.concurrent.duration._

class fMaxLgc extends Simulation {

  // ============ ОПРЕДЕЛЕНИЕ HTTP КОНФИГА ============
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(config.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .disableAutoReferer

  // СЦЕНАРИЙ
  val scn: ScenarioBuilder = LegacyTask.scn

  // SETUP
  setUp(
    scn.inject(
      incrementUsersPerSec(1.0)       // Увеличиваем на 1 юзера/сек на каждой ступени
        .times(10)                    // 10 ступеней
        .eachLevelLasting(200.seconds)  // сколько длится каждая ступень
        .separatedByRampsLasting(50.seconds) // Плавный переход
        .startingFrom(1.0)            // Начинаем с 0.5 юзера/сек
    )
  ).protocols(httpProtocol)
  //    .maxDuration(3.minutes)
  .assertions(
    global.responseTime.percentile(99).lt(3000), // Если 95% ответов дольше 3 сек — fail
    global.successfulRequests.percent.gt(99)     // Если ошибок > 1% — fail
  )
}
