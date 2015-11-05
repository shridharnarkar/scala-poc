package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future
import com.atos.worldline.utp.record.TicketStatusDBRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * This Scala Object represent DB Operation of ticket_status database table
 *
 * @author a162012
 */
class TicketStatusDAO {
  
}
/**
 * Dao class Object
 */
object TicketStatusDaoObject {

  val logger = LoggerFactory.getLogger(classOf[TicketStatusDAO])
  // The query interface for the ticket status table
  val ticketStatusQuery: TableQuery[TicketStatusTable] = TableQuery[TicketStatusTable]
  //ticket status list containing all ticket status records
  val ticketStatusList = new ListBuffer[TicketStatusDBRecord]

  /**
   * get all ticket status list
   */
  def getTicketStatusList(): List[TicketStatusDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusList.clear()
    logger.debug("TicketStatusDAO :: getTicketStatusList >>")
    Await.result(
      db.run(ticketStatusQuery.result).map { x =>
        x.foreach { ticketStatusRec =>
          ticketStatusList.append(ticketStatusRec)
          logger.debug(s"TicketStatusDAO :: ticketStatusRec >> $ticketStatusRec")
          logger.debug(s"TicketStatusDAO :: ticketStatusList >> $ticketStatusList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusDAO :: ticketStatusList.toList >> ${ticketStatusList.toList}")
    ticketStatusList match {
      case ticketStatusList if (ticketStatusList != null && ticketStatusList.length > 0) => ticketStatusList.toList
      case _ => null
    }
  } //getTicketStatusList

  /**
   *
   */
  def getTicketStatusById(ticketStatusId: Int): TicketStatusDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusList.clear()
    val ticketStatusFilterQuery = ticketStatusQuery.filter(_.tisId === ticketStatusId)
    logger.debug("TicketStatusDAO :: getTicketStatusById >>")
    Await.result(
      db.run(ticketStatusFilterQuery.result).map { x =>
        x.foreach { ticketStatusRec =>
          ticketStatusList.append(ticketStatusRec)
          logger.debug(s"TicketStatusDAO :: ticketStatusRec >> $ticketStatusRec")
          logger.debug(s"TicketStatusDAO :: ticketStatusList >> $ticketStatusList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusDAO :: ticketStatusList.apply(0) >> ${ticketStatusList.apply(0)}")
    ticketStatusList match {
      case ticketStatusList if (ticketStatusList != null && ticketStatusList.length > 0) => ticketStatusList.apply(0)
      case _ => null
    }
  } //getTicketStatusById

  /**
   *
   */
  def getTicketStatusByCode(ticketStatusCode: String): TicketStatusDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    ticketStatusList.clear()
    val ticketStatusFilterQuery = ticketStatusQuery.filter(_.ticketStatusCode === ticketStatusCode)
    logger.debug("TicketStatusDAO :: getTicketStatusByCode >>")
    Await.result(
      db.run(ticketStatusFilterQuery.result).map { x =>
        x.foreach { ticketStatusRec =>
          ticketStatusList.append(ticketStatusRec)
          logger.debug(s"TicketStatusDAO :: ticketStatusRec >> $ticketStatusRec")
          logger.debug(s"TicketStatusDAO :: ticketStatusList >> $ticketStatusList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"TicketStatusDAO :: ticketStatusList.apply(0) >> ${ticketStatusList.apply(0)}")
    ticketStatusList match {
      case ticketStatusList if (ticketStatusList != null && ticketStatusList.length > 0) => ticketStatusList.apply(0)
      case _ => null
    }
  }

}
