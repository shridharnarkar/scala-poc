package com.atos.worldline.utp.dao

import java.sql.Date

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.slf4j.LoggerFactory

import com.atos.worldline.utp.record.CalendarDBRecord

import slick.driver.MySQLDriver.api._
import slick.lifted.Aliases

/**
 * @author a579482
 */
class CalendarDAO {
}

/**
 *
 */
object CalendarDaoObject {
  val logger = LoggerFactory.getLogger(classOf[CalendarDAO])
  val calendarQuery: TableQuery[Calendar] = TableQuery[Calendar]
  val calendarList = new ListBuffer[CalendarDBRecord]
  val nowDate = new Date(java.util.Calendar.getInstance().getTimeInMillis);

  /**
   *
   */
  def getCalendar(): List[CalendarDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("CalendarDAO :: getBusiness >>")
    try {
      Await.result(
        db.run(calendarQuery.result).map { x =>
          x.foreach { calRec =>
            calendarList.append(calRec)
            logger.debug(s"CalendarDAO :: calRec >> $calRec")
            logger.debug(s"CalendarDAO :: calendarList >> $calendarList")
          }
        }, Duration.Inf)

    } finally { /*db.close */ }
    calendarList match {
      case calendarList if (calendarList != null && calendarList.length > 0) => calendarList.toList
      case _ => null
    }
  }

  /**
   *
   */
  def getCalendarPeriodByDate(): CalendarDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    // Filter Query Upon prodId column
    logger.debug("CalendarDAO :: getProductById >>")
    val calendarFilterQuery = calendarQuery.filter(_.calendarDate === nowDate)
    calendarList.clear()
    try {
      Await.result(
        db.run(calendarQuery.result).map { x =>
          x.foreach { calFilterRec =>
            calendarList.append(calFilterRec)
            logger.debug(s"CalendarDAO :: calFilterRec >> $calFilterRec")
            logger.debug(s"CalendarDAO :: calendarList >> $calendarList")
          }
        }, Duration.Inf)
    } finally { /*db.close */ }
    logger.debug(s"ProductDAO :: productList.apply(0) >> ${calendarList.apply(0)}")
    calendarList match {
      case calendarList if (calendarList != null && calendarList.length > 0) => calendarList.apply(0)
      case _ => null
    }
  }

}
