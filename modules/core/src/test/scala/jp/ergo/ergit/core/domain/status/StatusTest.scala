package jp.ergo.ergit.core.domain.status

import better.files.File
import org.scalatest._

class StatusTest extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  "State" should "create a State instance from status lines" in {
    val testData = "MM ReadMe.md\nM  hoge.txt\n M mage.txt"
    val actual = Status(testData, "")

    actual.states.size should be(3)
    actual.states(0) should be(Modified(File("ReadMe.md"), Position.Both))
    actual.states(1) should be(Modified(File("hoge.txt"), Position.Index))
    actual.states(2) should be(Modified(File("mage.txt"), Position.WorkingTree))
  }

  "State" should "create an Empty instance from empty status" in {
    val testData = ""
    val actual = Status(testData, "")

    actual.hasNoChange should be(true)
  }
}
