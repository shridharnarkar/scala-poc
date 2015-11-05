package com.atos.worldline.utp.dao

import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.ImportAttributeDBRecord
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ImportAttributeDAO {
  
}
/**
 * @author a579482
 */
object ImportAttributeDaoObject {
  val logger = LoggerFactory.getLogger(classOf[ImportAttributeDAO])
  val importAttributeQuery: TableQuery[ImportAttribute] = TableQuery[ImportAttribute]
  val importAttributeList = new ListBuffer[ImportAttributeDBRecord]
  val utpDao = UtpDaoObject

  def getImportAttribute(): List[ImportAttributeDBRecord] = {
    val db = utpDao.getDBConnection()
    logger.debug("ImportAttributeDAO :: getImportAttribute >>")
    Await.result(
      db.run(importAttributeQuery.result).map { x =>
        x.foreach { importAttributeRec =>
          importAttributeList.append(importAttributeRec)
          logger.debug(s"ImportAttributeDAO :: importAttributeRec >> $importAttributeRec")
          logger.debug(s"ImportAttributeDAO :: importAttributeList >> $importAttributeList")
        }
      }, Duration.Inf)

    /*db.close */
    importAttributeList.toList
  }

}