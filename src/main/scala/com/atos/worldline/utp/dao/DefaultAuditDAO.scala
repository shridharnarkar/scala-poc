package com.atos.worldline.utp.dao

import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.DefaultAuditDBRecord
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class DefaultAuditDAO {
  
}
/**
 * @author a579482
 */
object DefaultAuditDaoObject {
  val logger = LoggerFactory.getLogger(classOf[DefaultAuditDAO])
  val defaultAuditQuery: TableQuery[DefaultAudit] = TableQuery[DefaultAudit]
  val defaultAuditList = new ListBuffer[DefaultAuditDBRecord]
  val utpDao = UtpDaoObject

  def getDefaultAudit(): List[DefaultAuditDBRecord] = {
    val db = utpDao.getDBConnection()
    logger.debug("DefaultAuditDAO :: getDefaultAudit >>")
    Await.result(
      db.run(defaultAuditQuery.result).map { x =>
        x.foreach { defaultAuditRec =>
          defaultAuditList.append(defaultAuditRec)
          logger.debug(s"DefaultAuditDAO :: defaultAuditsRec >> defaultAuditRec")
          logger.debug(s"DefaultAuditDAO :: applicationParametersList >> $defaultAuditList")
        }
      }, Duration.Inf)

    /*db.close */
    defaultAuditList.toList
  }

}