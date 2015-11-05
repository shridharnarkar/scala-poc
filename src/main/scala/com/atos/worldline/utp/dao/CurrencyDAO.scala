package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

import org.slf4j.LoggerFactory

import com.atos.worldline.utp.record.CodeBookDBRecord
import com.atos.worldline.utp.record.CurrencyDBRecord

import slick.driver.MySQLDriver.api._
import slick.lifted.{ Aliases }

/**
 * @author a162012
 */
class CurrencyDAO {
}

/**
 * Dao class Object
 */
object CurrencyDaoObject {

  // The query interface for the calendar_currency table
  val calendarCurrencyQuery: TableQuery[CalendarCurrency] = TableQuery[CalendarCurrency]
  // The query interface for the type table
  val typeTableQuery: TableQuery[TypeMaster] = TableQuery[TypeMaster]
  // The query interface for the code_book table
  val codeBookQuery: TableQuery[CodeBook] = TableQuery[CodeBook]
  val currencyList = new ListBuffer[CurrencyDBRecord]
  val logger = LoggerFactory.getLogger(classOf[CurrencyDBRecord])

  /**
   *
   */
  var typeCodeBookJoin = for {
    t <- typeTableQuery
    cb <- codeBookQuery if (t.typeCol === "Currency" && t.typeId === cb.typeId)
  } yield cb //(cb.value, cb.cobId)

  /**
   *
   */
  def getCurrencyMaster(): List[CurrencyDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("CurrencyDAO :: getCurrencyMaster >>")
    try {
      Await.result(
        db.run(calendarCurrencyQuery.result).map { x =>
          x.foreach { calCurr =>
            currencyList.append(calCurr)
            logger.debug(s"CurrencyDAO :: calCurr >>: $calCurr")
            logger.debug(s"CurrencyDAO :: currencyList >> $currencyList")
          }}
        , Duration.Inf)
    } finally { /*db.close */ }
    logger.debug(s"CurrencyDAO :: currencyList.toList >> ${currencyList.toList}")
    currencyList match {
      case currencyList if (currencyList != null && currencyList.length > 0) => currencyList.toList
      case _ => null
    }
  }

  /**
   *
   */
  def getCurrencyCode(): scala.collection.mutable.Map[String, Int] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("CurrencyDAO ::  getCurrencyCode >>")
    val currencyMap: scala.collection.mutable.Map[String, Int] = scala.collection.mutable.Map[String, Int]()
    try {
      Await.result(
        db.run(typeCodeBookJoin.result).map { x => x.foreach { cb => currencyMap += cb.value.get -> cb.cobId } } //.onSuccess { case Seq(cb) => currencyMap += cb.value.get -> cb.cobId }
        , Duration.Inf)
      logger.debug("CurrencyDAO ::  getCurrencyCode currencyMap >> " + currencyMap)
    } finally { /*db.close */ }
    currencyMap
  }

}
