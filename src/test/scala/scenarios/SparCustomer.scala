package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import utils.{Feeders, Requests, config}

import scala.concurrent.duration._
import scala.util.Random

object SparCustomer {

  // Сценарий
  val scn: ScenarioBuilder = scenario("UseScenarios - Production Load")
    .feed(Feeders.user)
    .exec(Requests.getToken)

    .doIf(session => session.contains("token")) {
      repeat(10) { // ← можно менять число на желаемое (30, 60, 120 и т.д.)
        randomSwitch(
          // Веса пересчитаны в проценты (сумма ~100%)


          2.87 -> exec(Requests.getLoyaltyCustomer).pause(0.2.seconds),

          // ===== ПРОФИЛЬ И КАРТОЧКА (3.5%) =====
          1.28 -> exec(Requests.getPersonalData).pause(0.3.seconds),
          0.64 -> exec(Requests.getNoticesCount).pause(0.2.seconds),
          0.64 -> exec(Requests.getCardInfo).pause(0.2.seconds),
          0.64 -> exec(Requests.getCardBalance).pause(0.2.seconds),
          0.53 -> exec(Requests.getSettings).pause(0.2.seconds),
          0.53 -> exec(Requests.checkCardActivation).pause(0.2.seconds),

          // ===== QR И РАЗНОЕ (1.5%) =====
          1.80 -> exec(Requests.getQr).pause(0.3.seconds),

          // ===== КАТЕГОРИИ И ПОДДЕРЖКА (0.3%) =====
          0.16 -> exec(Requests.getAttributesCustomer).pause(0.2.seconds),
          0.16 -> exec(Requests.patchAttributesCustomer).pause(0.2.seconds),

          // ===== ОБНОВЛЕНИЯ И ТЕСТЫ (1.2%) =====
          0.53 -> exec(Requests.updateSettings).pause(0.2.seconds),
          0.53 -> exec(Requests.updateEreceipt).pause(0.2.seconds),
          0.53 -> exec(Requests.testFunction).pause(0.2.seconds),
          // Пауза между итерациями (обязательна!)
        ).pause(0.5.seconds)
      }
    }
}
