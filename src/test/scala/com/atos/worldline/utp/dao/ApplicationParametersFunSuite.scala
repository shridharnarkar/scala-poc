package com.atos.worldline.utp.dao

import java.sql.Date
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import com.atos.worldline.utp.rules.ReferenceDataObject
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.{Test, Before}
import org.mockito.runners.MockitoJUnitRunner
import com.atos.worldline.utp.record.ApplicationParametersDBRecord


/**
 * @author a579482
 */
@RunWith(classOf[JUnitRunner])
class ApplicationParametersFunSuite extends FunSuite with BeforeAndAfter with MockitoSugar{
    
  before {
    
  }

  test("test application parameters service for getApplicationParameters functionality") {
    //val applicationParamaterDAOService =  mock[ApplicationParamatersDAO]
    val testApplicationParametersDBRecord = List(
        ApplicationParametersDBRecord(1, "Maximum Non Season Fare", 10 , Some("expired"), Some(new Date(2015-10-29)), Some(new Date(2015-10-30))), 
        ApplicationParametersDBRecord(2, "Maximum Season Fare", 20 , Some("expired"), Some(new Date(2015-11-29)), Some(new Date(2015-11-30))))
    //when(applicationParamaterDAOService.getApplicationParameters()).thenReturn(testApplicationParametersDBRecord)    
    assertResult(testApplicationParametersDBRecord)(ApplicationParametersDaoObject.getApplicationParameters())
    
  }  
  
  test("test application parameters service for getApplicationParametersByName functionality") {
    val parameterName = "Maximum Non Season Fare"
    //val applicationParamaterDAOService =  mock[ApplicationParamatersDAO]
    val testApplicationParametersDBRecord = new ApplicationParametersDBRecord(
        1, "Maximum Non Season Fare", 10 , Some("expired"), Some(new Date(2015-10-29)), Some(new Date(2015-10-30))
        )
    
    //when(applicationParamaterDAOService.getApplicationParametersByName(parameterName)).thenReturn(testApplicationParametersDBRecord)    
    assertResult(testApplicationParametersDBRecord)(ApplicationParametersDaoObject.getApplicationParametersByName(parameterName))
    
  }
}