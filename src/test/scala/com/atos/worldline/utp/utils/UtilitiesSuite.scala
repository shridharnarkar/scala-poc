package com.atos.worldline.utp.utils

import scala.collection.mutable.ListMap

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import com.atos.worldline.utp.constants.RecordAttribute
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.rules.RuleFunctionsObject
import com.atos.worldline.utp.rules.ReferenceDataObject

@RunWith(classOf[JUnitRunner])
class UtilitiesSuite extends FunSuite with BeforeAndAfter {

  val targetCurrency = "Euros"

  before {
  }

  test("test left padding String function") {
    val spaceChar = " "
    assertResult("OutputString")(Utilities.leftPad("OutputString", 10, spaceChar))
    assertResult("      test")(Utilities.leftPad("test", 10, spaceChar))
  }

  test("test roundAt function") {
    assertResult(123456.23)(Utilities.roundAt(123456.2315, 2))
  }
  
  test("test getInteger function") {
    assertResult(123456)(Utilities.getInteger(123456.2315))
  }
  
  

}