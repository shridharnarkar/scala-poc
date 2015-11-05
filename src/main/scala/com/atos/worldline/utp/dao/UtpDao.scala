package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.slf4j.LoggerFactory
import com.atos.worldline.utp.record.BusinessDBRecord
import slick.driver.MySQLDriver.api._
import slick.lifted.{ Aliases }
import com.atos.worldline.utp.rules.RuleFunctionsObject
import com.atos.worldline.utp.rules.ReferenceDataObject
import com.atos.worldline.utp.record.ProductDBRecord

/**
 * @author a574891
 */

/**
 * This scala class Initialize the Utp DB configuration
 */
class UtpDAO {

}

/**
 * Dao class Object
 */
object UtpDaoObject {
  val logger = LoggerFactory.getLogger(classOf[UtpDAO])

  // The query interface for the Business table
  val businessQuery: TableQuery[Business] = TableQuery[Business]
  // The query interface for the locationLink table
  val locationLinkQuery: TableQuery[LocationLink] = TableQuery[LocationLink]
  // The query interface for the GroupFunctionType table
  val groupFunctionTypeQuery: TableQuery[GroupFunctionType] = TableQuery[GroupFunctionType]
  // The query interface for the ProductTable table
  val productTableTypeQuery: TableQuery[ProductTable] = TableQuery[ProductTable]

  val dbUrl = ReferenceDataObject.getPropertyValue("DB_URL")
  val dbDriver = ReferenceDataObject.getPropertyValue("DB_DRIVER")
  val dbUser = ReferenceDataObject.getPropertyValue("DB_USER")
  val dbPassword = ReferenceDataObject.getPropertyValue("DB_PASSWORD")
  var dbConnection: slick.driver.MySQLDriver.backend.DatabaseDef = null

  /**
   *  Join query on LocationLink, Business and GroupfunctionType Table to desired business id
   */
  val LocLinkBusinessGroupFunctionTypeJoin = for {
    groupFunTypeDesc <- Parameters[String]
    ll <- locationLinkQuery
    b <- businessQuery if (ll.busId === b.busId)
    gft <- groupFunctionTypeQuery if (ll.gftId === gft.gftId && gft.groupFunctionTypeDesc === groupFunTypeDesc)
  } yield ll.busId

  /**
   *  Join query on ProductTable and Business  Table to Current business has correcting product
   */
  val productBusinessTypeJoin = for {
    (penalisedBusId, correctionTypecobId) <- Parameters[(Int, Int)]
    pt <- productTableTypeQuery if (pt.prtId === correctionTypecobId)
    b <- businessQuery if (pt.busIdCorrective === b.busId)
  } yield pt

  /**
   * fetch businessid with location loc_id = in_selling location loc_id and groupFunTypeDesc
   */
  def getBusinessIdBySellingLocation(groupFunTypeDesc: String): Int = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("UtpDAO ::  UtpDAO >>")

    var busId: Int = 0
    try {
      Await.result(
        db.run(LocLinkBusinessGroupFunctionTypeJoin(groupFunTypeDesc).result).map { x => x.foreach { y => busId = y } }, Duration.Inf)
      logger.debug("UtpDAO ::  getBusinessIdBySellingLocation BussinessId >> " + busId)
    } finally { /*db.close */ }
    busId
  }

  /**
   *  find the Current business has correcting product
   */
  def getCorrectProductForCurrentBussiness(penalisedBusId: Int, correctionTypecobId: Int): ProductDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("UtpDAO ::  getCorrectProductForCurrentBussiness >>")
    var correctingProduct: ProductDBRecord = null
    try {
      Await.result(
        db.run(productBusinessTypeJoin(penalisedBusId, correctionTypecobId).result).map { x => x.foreach { y => correctingProduct = y } }, Duration.Inf)
      logger.debug("UtpDAO ::  getCorrectProductForCurrentBussiness correctingProduct >> " + correctingProduct)
    } finally { /*db.close */ }

    correctingProduct match {
      case null =>
        var busIdParent = BusinessDaoObject.getBusinessById(penalisedBusId).busIdParent
        busIdParent match {
          case None => null
          case _    => getCorrectProductForCurrentBussiness(busIdParent.get, correctionTypecobId)
        }
      case _ => correctingProduct
    }

  }

  /**
   * get UTP DB Connection
   */
  def getDBConnection(): slick.driver.MySQLDriver.backend.DatabaseDef = {
    if (dbConnection == null) {
      dbConnection = Database.forURL(dbUrl, driver = dbDriver, user = dbUser, password = dbPassword)
    }
    dbConnection
  }

}

