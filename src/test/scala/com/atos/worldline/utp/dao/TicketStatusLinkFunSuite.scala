package com.atos.worldline.utp.dao

import java.sql.Date
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.{ Test, Before }
import com.atos.worldline.utp.record.TicketStatusLinkDBRecord
import org.joda.time.DateTime
import java.sql.Timestamp

/**
 * @author a162012
 */
@RunWith(classOf[JUnitRunner])
class TicketStatusLinkFunSuite extends FunSuite with BeforeAndAfter {

  val ticketStatusLinkList = List(TicketStatusLinkDBRecord(1040, Some("N"), Some(new java.sql.Date(new DateTime(2015, 11, 29, 0, 0).getMillis())), Some(new java.sql.Date(new DateTime(2015, 11, 30, 0, 0).getMillis())), 1010, 1000))
  val ticketStatusLinkObj = ticketStatusLinkList.apply(0)
  val daoObj = TicketStatusLinkDaoObject

  before {
    //TODO: do nothing
  }

  test("test getTicketStatusLinkList") {
    assertResult(ticketStatusLinkList)(daoObj.getTicketStatusLinkList())
  }

  test("test getTicketStatusLinkById") {
    assertResult(ticketStatusLinkObj)(daoObj.getTicketStatusLinkById(1040))
  }
  
  test("test getTicketStatusLinkByLinkedById") {
    assertResult(ticketStatusLinkObj)(daoObj.getTicketStatusLinkByLinkedById(1000))
  }
  
  test("test getTicketStatusLinkByLinksId") {
    assertResult(ticketStatusLinkObj)(daoObj.getTicketStatusLinkByLinksId(1010))
  }

}//class TicketStatusLinkFunSuite