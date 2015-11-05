package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import slick.driver.MySQLDriver.api._
import com.atos.worldline.utp.record.CodeBookDBRecord
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Scala Object represent DB Operation of CodeBook database table
 *
 * @author a574891
 */
class CodeBookDAO {
}
/**
 * Dao class Object
 */
object CodeBookDaoObject {

  // The query interface for the code_book table
  val codeBookQuery: TableQuery[CodeBook] = TableQuery[CodeBook]
  val logger = LoggerFactory.getLogger(classOf[CodeBook])

  /**
   * get CodeBook by Value column
   */
  def getCodeBookByValue(value: String): CodeBookDBRecord = {

    logger.debug("CodeBookDAO :: getCodeBookByValue >>")
    val db = UtpDaoObject.getDBConnection()

    var codeBookList = new scala.collection.mutable.ListBuffer[CodeBookDBRecord]
    // Filter Query Upon value column
    val codeBookFilterQuery = codeBookQuery.filter(_.value === value)
    // db.run(codeBookFilterQuery.result).foreach(x => codeBookList ++= x)

    try {
      Await.result(
        db.run(codeBookFilterQuery.result).map { x =>
          x.foreach { codeBookElement =>
            codeBookList.append(codeBookElement)
            logger.debug(s"CurrencyDAO :: calCurr >>: $codeBookElement")
            logger.debug(s"CurrencyDAO :: codeBookList >> codeBookList")
          }
        }, Duration.Inf)
    } finally { /*db.close */ }
    codeBookList match {
      case codeBookList if (codeBookList != null && codeBookList.length > 0) => codeBookList.apply(0)
      case _ => null
    }
  }
  /**
   *  Get the ID of the linked parent product which has a product type of Primary Product Group (PPG)
   */
  def getCodeBookById(id: Int): CodeBookDBRecord = {
    logger.debug("CodeBookDAO :: getCodeBookById >>")
    val db = UtpDaoObject.getDBConnection()

    var codeBookList = new scala.collection.mutable.ListBuffer[CodeBookDBRecord]
    // Filter Query Upon value column
    val codeBookFilterQuery = codeBookQuery.filter(_.cobId === id)
    // db.run(codeBookFilterQuery.result).foreach(x => codeBookList ++= x)
    try {
      Await.result(
        db.run(codeBookFilterQuery.result).map { x =>
          x.foreach { codeBookElement =>
            codeBookList.append(codeBookElement)
            logger.debug(s"CurrencyDAO :: codeBookElement >>: $codeBookElement")
            logger.debug(s"CurrencyDAO :: codeBookList >> codeBookList")
          }
        }, Duration.Inf)
    } finally { /*db.close */ }
    codeBookList match {
      case codeBookList if (codeBookList != null && codeBookList.length > 0) => codeBookList.apply(0)
      case _ => null
    }
  }
}

