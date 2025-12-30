package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.UseScenarios
import utils.config

import scala.concurrent.duration._

class fMax extends Simulation {

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
      incrementUsersPerSec(1)       // Увеличиваем на 1 юзера/сек на каждой ступени
        .times(6)                    // 10 ступеней (дойдем до 10 юзеров/сек)
        .eachLevelLasting(300.seconds)  // Каждая ступень длится 2 минуты (чтобы успеть увидеть плато)
        .separatedByRampsLasting(100.seconds) // Плавный переход
        .startingFrom(1)            // Начинаем с 1 юзера/сек
    )
  ).protocols(httpProtocol)
  //    .maxDuration(3.minutes)
  .assertions(
    global.responseTime.percentile(95).lt(3000), // Если 95% ответов дольше 3 сек — fail
    global.successfulRequests.percent.gt(99)     // Если ошибок > 1% — fail
  )

  //  setUp(
  //    scn.inject(
  //      // Разогрев: от 0 до 5 юзеров за 1 минуту
  //      rampConcurrentUsers(0).to(5).during(1.minutes),
  //      // Полка: держим 5 юзеров ровно 5 минут
  //      constantConcurrentUsers(5).during(5.minutes)
  //    )
  //  ).protocols(httpProtocol)
}
