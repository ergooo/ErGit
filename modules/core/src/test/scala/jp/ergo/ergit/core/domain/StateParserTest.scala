package jp.ergo.ergit.core.domain

import better.files.File
import jp.ergo.ergit.core.domain.Position.{Both, Index, WorkingTree}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, Matchers, FlatSpec}

class StateParserTest extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  "StateParser" should "return workingTree Modified" in {
    val testData = " M ReadMe.md"
    val actual = StateParser.parse(testData)

    actual should be(Modified(File("ReadMe.md"), WorkingTree))
  }

  "StateParser" should "return indexed Modified" in {
    val testData = "M  ReadMe.md"
    val actual = StateParser.parse(testData)

    actual should be(Modified(File("ReadMe.md"), Index))
  }

  "StateParser" should "return both Modified" in {
    val testData = "MM ReadMe.md"
    val actual = StateParser.parse(testData)

    actual should be(Modified(File("ReadMe.md"), Both))
  }


}
