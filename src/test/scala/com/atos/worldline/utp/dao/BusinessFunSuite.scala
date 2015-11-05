package com.atos.worldline.utp.dao

import java.sql.Date
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import com.atos.worldline.utp.record.BusinessDBRecord
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.{Test, Before}
import org.mockito.runners.MockitoJUnitRunner


/**
 * @author a579482
 */
@RunWith(classOf[MockitoJUnitRunner])
class BusinessFunSuite extends FunSuite with BeforeAndAfter with MockitoSugar{
    
  before {
    
  }

  test("test buiness service for getBuisness functionality") {
    val businessDAOService =  BusinessDaoObject
    val testBusinessDBRecord = List(BusinessDBRecord(3,Some("Lennon"), Some(new Date(java.util.Calendar.getInstance.getTime().getTime)), Some(1)))
    when(businessDAOService.getBusiness()).thenReturn(testBusinessDBRecord)    
    assertResult(testBusinessDBRecord)(businessDAOService.getBusiness())
    assertResult(Some("Lennon"))(businessDAOService.getBusiness().apply(0).busName)
    
  }  
}