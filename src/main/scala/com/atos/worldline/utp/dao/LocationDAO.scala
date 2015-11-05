package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import slick.driver.MySQLDriver.api._
import com.atos.worldline.utp.record.LocationDBRecord
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Scala Object represent DB Operation of location database table
 *
 * @author a162012
 */
class LocationDAO {
}

/**
 * Dao class Object
 */
object LocationDaoObject {
  // The query interface for the location table
  val locationQuery: TableQuery[LocationTable] = TableQuery[LocationTable]
  val logger = LoggerFactory.getLogger(classOf[LocationTable])
  //product list containing all product records
  val locationList = new ListBuffer[LocationDBRecord]
  /**
   * Get All Location List
   */
  def getLocationList(): List[LocationDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("LocationDAO :: getLocationList >>")
    locationList.clear()
    Await.result(
      db.run(locationQuery.result).map { x =>
        x.foreach { locRec =>
          locationList.append(locRec)
          logger.debug(s"LocationDAO :: locRec >> $locRec")
          logger.debug(s"LocationDAO :: locationList >> $locationList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"ProductDAO :: locationList.toList >> ${locationList.toList}")
    locationList match {
      case locationList if (locationList != null && locationList.length > 0) => locationList.toList
      case _ => null
    }
  } //getLocationList

  /**
   * get Location by Location Id
   */
  def getLocationById(locationId: Int): LocationDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    // Filter Query Upon prodId column
    logger.debug("LocationDAO :: getLocationById >>")
    val locationFilterQuery = locationQuery.filter(_.locationId === locationId)
    locationList.clear()
    Await.result(
      db.run(locationFilterQuery.result).map { x =>
        x.foreach { locRec =>
          locationList.append(locRec)
          logger.debug(s"LocationDAO :: locRec >> $locRec")
          logger.debug(s"LocationDAO :: locationList >> $locationList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"LocationDAO :: locationList.apply(0) >> ${locationList.apply(0)}")
    locationList match {
      case locationList if (locationList != null && locationList.length > 0) => locationList.apply(0)
      case _ => null
    }
  }
}
