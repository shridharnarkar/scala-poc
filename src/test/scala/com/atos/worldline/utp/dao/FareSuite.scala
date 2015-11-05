package com.atos.worldline.utp.dao

import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import java.sql.Date
import java.sql.Timestamp
import org.joda.time.DateTime
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.record.FareDBRecord
import org.mockito.Matchers._

import org.junit.{ Test, Before }
import org.mockito.runners.MockitoJUnitRunner
/**
 * @author a574891
 */

@RunWith(classOf[MockitoJUnitRunner])
class FareSuite extends FunSuite with BeforeAndAfter with MockitoSugar {

  before {
    //TODO do nothing
  }

  test("test getFares DB call functionality ") {
    val fareDAOService = FareDaoObject
     assertResult(1)(fareDAOService.getFares().length)
  }

}