package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future
import com.atos.worldline.utp.record.RouteDBRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * @author a162012
 */
class RouteDAO {
  
}

/**
 * Dao class Object
 */
object RouteDaoObject {
  val logger = LoggerFactory.getLogger(classOf[RouteDAO])
  // The query interface for the product table
  val routeQuery: TableQuery[RouteTable] = TableQuery[RouteTable]
  //route list containing all route records
  val routeList = new ListBuffer[RouteDBRecord]

  def getRouteList(): List[RouteDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("RouteDAO :: getRouteList >>")
    routeList.clear()
    Await.result(
      db.run(routeQuery.result).map { x =>
        x.foreach { routeRec =>
          routeList.append(routeRec)
          logger.debug(s"RouteDAO :: routeRec >> $routeRec")
          logger.debug(s"RouteDAO :: routeList >> $routeList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"RouteDAO :: routeList.toList >> ${routeList.toList}")
    routeList match {
      case routeList if (routeList != null && routeList.length > 0) => routeList.toList
      case _ => null
    }

  } //getRouteList

  def getRouteById(routeId: Int): RouteDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("RouteDAO :: getRouteById >>")
    routeList.clear()
    val routeFilterQuery = routeQuery.filter(_.routeId === routeId)
    Await.result(
      db.run(routeFilterQuery.result).map { x =>
        x.foreach { routeRec =>
          routeList.append(routeRec)
          logger.debug(s"RouteDAO :: routeRec >> $routeRec")
          logger.debug(s"RouteDAO :: routeList >> $routeList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"RouteDAO :: routeList.toList >> ${routeList.toList}")
    routeList match {
      case routeList if (routeList != null && routeList.length > 0) => routeList.apply(0)
      case _ => null
    }

  } //getRouteById

}

