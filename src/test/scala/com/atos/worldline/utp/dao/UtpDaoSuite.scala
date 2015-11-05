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
import com.atos.worldline.utp.record.ProductDBRecord
/**
 * @author a574891
 */

@RunWith(classOf[MockitoJUnitRunner])
class UtpDaoSuite extends FunSuite with BeforeAndAfter with MockitoSugar {

  val utpDao = UtpDaoObject
  val penalisedBusId: Int = 1
  val correctionTypeCobId: Int = 650
   val correctionTypeCobId1: Int = 400
  val resultProduct: ProductDBRecord = new ProductDBRecord(1, Some("DFCP"), Some("Default Fares Checking Penalty"), None, None, None, None, None, None, None, None, None, None, Some(1), None, 1, 650, 650, 650, 650, 1, 1)
  before {
    //TODO do nothing
  }

  test("test getBusinessIdBySellingLocation DB call functionality ") {
    assertResult(1)(utpDao.getBusinessIdBySellingLocation(UtpConstants.GROUP_FUNCTION_TYPE_OWNS))
  }

  test("test getCorrectProductForCurrentBussiness DB call functionality ") {
    assertResult(resultProduct)(utpDao.getCorrectProductForCurrentBussiness(penalisedBusId, correctionTypeCobId))
  }
  
  test("test getCorrectProductForCurrentBussiness case 2 DB call functionality ") {
    assertResult(null)(utpDao.getCorrectProductForCurrentBussiness(penalisedBusId, correctionTypeCobId1))
  }

}