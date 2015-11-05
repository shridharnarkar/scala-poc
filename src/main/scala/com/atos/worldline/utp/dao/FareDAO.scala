package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.slf4j.LoggerFactory
import slick.driver.MySQLDriver.api._
import slick.lifted.{ Aliases }
import com.atos.worldline.utp.record.FareDBRecord

/**
 * This Scala Object represent DB Operation of Fare database table
 *
 * @author a574891
 */
class FareDAO {
}

/**
 * DAo Class Object
 */
object FareDaoObject {
  val logger = LoggerFactory.getLogger(classOf[FareDAO])
  val fareQuery: TableQuery[Fare] = TableQuery[Fare]
  val fareList = new ListBuffer[FareDBRecord]

  /**
   * 
   */
  def getFares(): List[FareDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("FareDAO :: getFares >>")
    Await.result(
      db.run(fareQuery.result).map { x =>
        x.foreach { fareRec =>
          fareList.append(fareRec)
          logger.debug(s"FareDAO :: fare >> $fareRec")
          logger.debug(s"FareDAO :: fareList >> $fareList")
        }
      }, Duration.Inf)

    /*db.close */
    fareList match {
      case fareList if (fareList != null && fareList.length > 0) => fareList.toList
      case _ => null
    }
  }

}

