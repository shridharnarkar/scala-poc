package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import org.slf4j.LoggerFactory

import com.atos.worldline.utp.record.BusinessDBRecord

import slick.driver.MySQLDriver.api._
import slick.lifted.{ Aliases }
/**
 * @author a579482
 */
class BusinessDAO {
}

/**
 * DAo Class Object
 */
object BusinessDaoObject {
  val logger = LoggerFactory.getLogger(classOf[BusinessDBRecord])
  val businessQuery: TableQuery[Business] = TableQuery[Business]
  val businessList = new ListBuffer[BusinessDBRecord]

  /**
   *  Get all Business Object
   */
  def getBusiness(): List[BusinessDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("BusinessDAO :: getBusiness >>")
    try {
      Await.result(
        db.run(businessQuery.result).map { x =>
          x.foreach { bussRec =>
            businessList.append(bussRec)
            logger.debug(s"BusinessDAO :: bussRec >> $bussRec")
            logger.debug(s"BusinessDAO :: businessList >> $businessList")
          }
        }, Duration.Inf)
    } finally { /*db.close */ }
    businessList match {
      case businessList if (businessList != null && businessList.length > 0) => businessList.toList
      case _ => null
    }
  }

  /**
   *  Fetch the get business by Id
   */
  def getBusinessById(id: Int): BusinessDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("BusinessDAO :: getBusinessById >>")
    var BusinessDBRecordList = new scala.collection.mutable.ListBuffer[BusinessDBRecord]
    // Filter Query Upon value column
    val businessFilterQuery = businessQuery.filter(_.busId === id)
    try {
      Await.result(
        db.run(businessQuery.result).map { x =>
          x.foreach { bussRec =>
            businessList.append(bussRec)
            logger.debug(s"BusinessDAO :: bussRec >> $bussRec")
            logger.debug(s"BusinessDAO :: businessList >> $businessList")
          }
        }, Duration.Inf)

    } finally { /*db.close */ }

    businessList match {
      case businessList if (businessList != null && businessList.length > 0) => businessList.apply(0)
      case _ => null
    }
  }

}
