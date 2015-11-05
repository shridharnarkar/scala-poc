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

import org.junit.{Test, Before}
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
    val mockLocGrpFboList: List[FareDBRecord] = getDummyFareRecords() 
    when(fareDAOService.getFares()).thenReturn(mockLocGrpFboList)
    
    assertResult(4)(fareDAOService.getFares().length)

  }
  
  def getDummyFareRecords() : List[FareDBRecord] ={
    
   val sqlCurrentDate = new Date(java.util.Calendar.getInstance.getTime().getTime())
   val locGrpFboList: List[FareDBRecord] = List(
         new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "A", "B", "001", 7.65, 7, new Timestamp(new DateTime(2015, 1, 1, 0, 0).getMillis()), new Timestamp(new DateTime(2015, 10, 31, 23, 59).getMillis()), Some("expired"), Some(sqlCurrentDate),Some(sqlCurrentDate)),
         new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "C", "D", "001", 7.65, 7, new Timestamp(new DateTime(2015, 1, 4, 0, 0).getMillis()), new Timestamp(new DateTime(2015, 6, 30, 23, 59).getMillis()), Some("expired"), Some(sqlCurrentDate),Some(sqlCurrentDate)),
         new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "E", "F", "001", 7.65, 7, new Timestamp(new DateTime(2015, 1, 7, 0, 0).getMillis()), new Timestamp(new DateTime(2015, 9, 30, 23, 59).getMillis()), Some("expired"), Some(sqlCurrentDate),Some(sqlCurrentDate)),
         new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "G", "H", "001", 7.65, 7, new Timestamp(new DateTime(2015, 1, 10, 0, 0).getMillis()), new Timestamp(new DateTime(2015, 12, 31, 23, 59).getMillis()), Some("expired"), Some(sqlCurrentDate),Some(sqlCurrentDate)))
  
         locGrpFboList.toList
  }     
}