package com.atos.worldline.utp.dao

import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.ApplicationParametersDBRecord
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


/**
 * @author a579482
 */
class ApplicationParamatersDAO {

}

/**
 * 
 */
object ApplicationParametersDaoObject {
  val logger = LoggerFactory.getLogger(classOf[ApplicationParametersDBRecord])
  val applicationParametersQuery: TableQuery[ApplicationParameters] = TableQuery[ApplicationParameters]
  val applicationParametersList = new ListBuffer[ApplicationParametersDBRecord]

  def getApplicationParameters(): List[ApplicationParametersDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("ApplicationParametersDAO :: getApplicationParameters >>")
    Await.result(
      db.run(applicationParametersQuery.result).map { x =>
        x.foreach { applicationParametersRec =>
          applicationParametersList.append(applicationParametersRec)
          logger.debug(s"ApplicationParametersDAO :: applicationParametersRec >> $applicationParametersRec")
          logger.debug(s"ApplicationParametersDAO :: applicationParametersList >> $applicationParametersList")
        }
      }, Duration.Inf)

    /*db.close */
    applicationParametersList.toList
  }

  def getApplicationParametersByName(parameterName: String): ApplicationParametersDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    // Filter Query Upon prodId column
    logger.debug("ApplicationParamatersDAO :: getApplicationParametersByName >>")
    val applicationParametersQueryFiltered = applicationParametersQuery.filter(_.name === parameterName)
    applicationParametersList.clear()
    Await.result(
      db.run(applicationParametersQueryFiltered.result).map { x =>
        x.foreach { applicationParametersByNameRec =>
          applicationParametersList.append(applicationParametersByNameRec)
          logger.debug(s"ApplicationParametersDAO :: applicationParametersByNameRec >> $applicationParametersByNameRec")
          logger.debug(s"ApplicationParametersDAO :: applicationParametersList >> $applicationParametersList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"ApplicationParametersDAO :: applicationParametersList.apply(0) >> ${applicationParametersList.apply(0)}")
    applicationParametersList.apply(0)
  }

}
