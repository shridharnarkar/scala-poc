package com.atos.worldline.utp.dao

import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.AttributeAuditDBRecord
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class AttributeAuditDAO {
  
}

/**
 * @author a579482
 */
object AttributeAuditDaoObject {
   val logger = LoggerFactory.getLogger(classOf[AttributeAuditDAO])
  val attributeAuditQuery: TableQuery[AttributeAudit] = TableQuery[AttributeAudit]
  val attributeAuditList = new ListBuffer[AttributeAuditDBRecord]
  val utpDao  = UtpDaoObject

  def getAttributeAudit(): List[AttributeAuditDBRecord] = {
    val db = utpDao.getDBConnection()
    logger.debug("AttributeAuditDAO :: getAttributeAudit >>")
    Await.result(
      db.run(attributeAuditQuery.result).map { x =>
        x.foreach { attributeAuditsRec =>
          attributeAuditList.append(attributeAuditsRec)
          logger.debug(s"AttributeAuditDAO :: applicationParametersRec >> $attributeAuditsRec")
          logger.debug(s"AttributeAuditDAO :: attributeAuditList >> $attributeAuditList")
        }
      }, Duration.Inf)

    /*db.close */
    attributeAuditList.toList
  }
 
}