package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.atos.worldline.utp.record.FareDBRecord
import com.atos.worldline.utp.record.FaresCheckingExclusionsDBRecord
import slick.driver.MySQLDriver.api._
import java.sql.Date

/**
 * This Scala Object represent DB Operation of FaresCheckingExclusions database table
 *
 * @author a574891
 */
class FaresCheckingExclusionsDAO {
}
/**
 * Dao class Object
 */
object FaresCheckingExclusionsDaoObject {

  // The query interface for the code_book table
  val fCheckExcQuery: TableQuery[FaresCheckingExclusions] = TableQuery[FaresCheckingExclusions]
  val logger = LoggerFactory.getLogger(classOf[FaresCheckingExclusionsDAO])

  /**
   *  fecth result for LN4350S_V_IF_EXCLUDE_FARE
   */
  val fareCheckingExclusionQuery = for {
    //(fchId, issuingLocation, originLocation, destinationLoction, route, product, ticketStatus, fare, withEffectFrom, withEffectUntil, nullFare, sellingLocation) 
    (locIssueDate, issuingLocation, originLocation, destinationLocation, route, product, ticketStatus, fare, nullFare, sellingLocation) <- Parameters[(Date, Option[String], Option[String], Option[String], Option[String], Option[String], Option[String], Option[BigDecimal], String, Option[String])]
    fce <- fCheckExcQuery if (fce.issuingLocation.get === issuingLocation && fce.originLocation.get === originLocation && fce.destinationLocation.get === destinationLocation && fce.route.get === route && fce.product.get === product && fce.ticketStatus.get === ticketStatus && fce.sellingLocation.get === sellingLocation && (fce.withEffectFrom <= locIssueDate && fce.withEffectUntil <= locIssueDate) && fce.nullFare == 'Y' && fce.fare.get === fare)
  } yield fce

  /**
   * get FaresCheckingExclusions by Value column
   */
  def getFaresCheckingExclusionsById(fchId: BigDecimal): FaresCheckingExclusionsDBRecord = {

    logger.debug("FaresCheckingExclusionsDAO :: getFaresCheckingExclusionsDBRecordById >>")
    val db = UtpDaoObject.getDBConnection()

    var fCheckExcDBRecordList = new scala.collection.mutable.ListBuffer[FaresCheckingExclusionsDBRecord]
    // Filter Query Upon value column
    val fCheckExcFilterQuery = fCheckExcQuery.filter(_.fchId === fchId)

    try {
      Await.result(
        db.run(fCheckExcFilterQuery.result).map { x =>
          x.foreach { fcheckExcElement =>
            fCheckExcDBRecordList.append(fcheckExcElement)
          }
        }, Duration.Inf)
    } finally { /*db.close */ }
    fCheckExcDBRecordList match {
      case fCheckExcDBRecordList if (fCheckExcDBRecordList != null && fCheckExcDBRecordList.length > 0) => fCheckExcDBRecordList.apply(0)
      case _ => null
    }
  }

  /**
   *  find the FaresCheckingExclusions Id
   */
  def getFaresCheckingExclusionsId(locIssueDate: Date, fareRec: FareDBRecord): Boolean = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("FaresCheckingExclusionsDAO ::  getFaresCheckingExclusionsId >>")
    var faresCheckingExclusions: FaresCheckingExclusionsDBRecord = null
    try {
      Await.result(
        db.run(
          fareCheckingExclusionQuery(locIssueDate,
            Some(fareRec.issuingLocation.getOrElse("")),
            Some(fareRec.originLocation.getOrElse("")),
            Some(fareRec.destinationLocation.getOrElse("")),
            Some(fareRec.route.getOrElse("")),
            Some(fareRec.product.getOrElse("")),
            Some(fareRec.ticketStatus.getOrElse("")),
            Some(fareRec.fare.get),
            fareRec.nullFare,
            Some(fareRec.sellingLocation.getOrElse(""))).result).map {
            x => x.foreach { y => faresCheckingExclusions = y }
          }, Duration.Inf)
      logger.debug("FaresCheckingExclusionsDAO ::  getFaresCheckingExclusionsId  >> " + faresCheckingExclusions)
    } finally { /*db.close */ }

    faresCheckingExclusions match {
      case faresCheckingExclusions if (faresCheckingExclusions != null) => return true
      case _ => return false

    }

  }

}

