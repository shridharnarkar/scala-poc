package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.atos.worldline.utp.record.TicketStatusLinkDBRecord

import slick.driver.MySQLDriver.api._

/**
 * This Scala Object represent DB Operation of ticket_status_link database table
 * @author a162012
 */
class TicketStatusLinkDAO {
}

object TicketStatusLinkDaoObject {
  val logger = LoggerFactory.getLogger(classOf[TicketStatusLinkDAO])
  // The query interface for the ticket status table
  val ticketStatusLinkQuery: TableQuery[TicketStatusLinkTable] = TableQuery[TicketStatusLinkTable]
  //ticket status link list containing all ticket status link records
  val ticketStatusLinkList: ListBuffer[TicketStatusLinkDBRecord] = new ListBuffer[TicketStatusLinkDBRecord]

  /**
   * get all ticket status list
   */
  def getTicketStatusLinkList(): List[TicketStatusLinkDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusLinkList.clear()
    logger.debug("TicketStatusLinkDAO :: getTicketStatusLinkList >>")
    Await.result(
      db.run(ticketStatusLinkQuery.result).map { x =>
        x.foreach { ticketStatusRec =>
          ticketStatusLinkList.append(ticketStatusRec)
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusRec >> $ticketStatusRec")
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList >> $ticketStatusLinkList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList.toList >> ${ticketStatusLinkList.toList}")
    ticketStatusLinkList match {
      case ticketStatusLinkList if (ticketStatusLinkList != null && ticketStatusLinkList.length > 0) => ticketStatusLinkList.toList
      case _ => null
    }
  } //getTicketStatusLinkList

  def getTicketStatusLinkById(ticketStatusLinkId: Int): TicketStatusLinkDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusLinkList.clear()
    val ticketStatusFilterQuery = ticketStatusLinkQuery.filter(_.tslId === ticketStatusLinkId)
    logger.debug("TicketStatusLinkDAO :: getTicketStatusById >>")
    Await.result(
      db.run(ticketStatusFilterQuery.result).map { x =>
        x.foreach { ticketStatusLinkRec =>
          ticketStatusLinkList.append(ticketStatusLinkRec)
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusRec >> $ticketStatusLinkRec")
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList >> $ticketStatusLinkList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList.apply(0) >> ${ticketStatusLinkList.apply(0)}")
    ticketStatusLinkList match {
      case ticketStatusLinkList if (ticketStatusLinkList != null && ticketStatusLinkList.length > 0) => ticketStatusLinkList.apply(0)
      case _ => null
    }
  } //getTicketStatusLinkById

  /**
   *
   */
  def getTicketStatusLinkByLinkedById(ticketStatusId: Int): TicketStatusLinkDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusLinkList.clear()
    val ticketStatusFilterQuery = ticketStatusLinkQuery.filter(_.tisIdLinkedBy === ticketStatusId)
    logger.debug("TicketStatusLinkDAO :: getTicketStatusLinkByLinkedById >>")
    Await.result(
      db.run(ticketStatusFilterQuery.result).map { x =>
        x.foreach { ticketStatusLinkRec =>
          ticketStatusLinkList.append(ticketStatusLinkRec)
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusRec >> $ticketStatusLinkRec")
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList >> $ticketStatusLinkList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList.apply(0) >> ${ticketStatusLinkList.apply(0)}")
    ticketStatusLinkList match {
      case ticketStatusLinkList if (ticketStatusLinkList != null && ticketStatusLinkList.length > 0) => ticketStatusLinkList.apply(0)
      case _ => null
    }
  } //getTicketStatusLinkByLinkedById

  /**
   *
   */
  def getTicketStatusLinkByLinksId(ticketStatusId: Int): TicketStatusLinkDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusLinkList.clear()
    val ticketStatusFilterQuery = ticketStatusLinkQuery.filter(_.tisIdLinks === ticketStatusId)
    logger.debug("TicketStatusLinkDAO :: getTicketStatusLinkByLinksId >>")
    Await.result(
      db.run(ticketStatusFilterQuery.result).map { x =>
        x.foreach { ticketStatusLinkRec =>
          ticketStatusLinkList.append(ticketStatusLinkRec)
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusRec >> $ticketStatusLinkRec")
          logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList >> $ticketStatusLinkList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusLinkDAO :: ticketStatusLinkList.apply(0) >> ${ticketStatusLinkList.apply(0)}")
    ticketStatusLinkList match {
      case ticketStatusLinkList if (ticketStatusLinkList != null && ticketStatusLinkList.length > 0) => ticketStatusLinkList.apply(0)
      case _ => null
    }
  } //getTicketStatusLinkByLinkedById

}
