package com.atos.worldline.utp.dao

import java.sql.Date
import scala.math.BigDecimal.double2bigDecimal
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Mockito
import com.atos.worldline.utp.record.CurrencyDBRecord
import com.atos.worldline.utp.record.BusinessDBRecord

/**
 * @author a162012
 */
@RunWith(classOf[JUnitRunner])
class CurrencyFunSuite extends FunSuite with BeforeAndAfter with MockitoSugar{
  val date1 = (new DateTime()).withYear(2015).withMonthOfYear(9).withDayOfMonth(8)
  val date2 = (new DateTime()).withYear(2015).withMonthOfYear(9).withDayOfMonth(9)
  val currencyList: List[CurrencyDBRecord] = List(CurrencyDBRecord(math.BigDecimal(10.20).setScale(2), 5, new Date(date1.toDate.getTime)),
    CurrencyDBRecord(math.BigDecimal(10.25).setScale(2), 4, new Date(date2.toDate.getTime)))
  val currencyMap = scala.collection.mutable.Map("Sterling" -> 300, "Euro" -> 310)
  val currencyDAOservice = CurrencyDaoObject
  
  before {
    //TODO: Do nothing
  }

  test("get Calendar Currency List") {
    when(currencyDAOservice.getCurrencyMaster()).thenReturn(currencyList)
    assertResult(currencyList)(currencyDAOservice.getCurrencyMaster())
  }

  test("get Currency Codes Map") {
    Mockito.doReturn(currencyMap).when(currencyDAOservice).getCurrencyCode()
    assertResult(Some(300))(currencyDAOservice.getCurrencyCode().get("Sterling"))
  }
  
  test("call the Currency Master function"){
    val testCurrencyDBRecord = List(CurrencyDBRecord(10.3:BigDecimal, 5, new Date(java.util.Calendar.getInstance.getTime().getTime)))
    when(currencyDAOservice.getCurrencyMaster()).thenReturn(testCurrencyDBRecord)
    assertResult(10.3:BigDecimal)(currencyDAOservice.getCurrencyMaster().apply(0).currencyRate)
    assertResult(5)(currencyDAOservice.getCurrencyMaster().apply(0).conversionPower)
    
  }
}