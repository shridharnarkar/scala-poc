package com.atos.worldline.utp.dao

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future
import com.atos.worldline.utp.record.ProductDBRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * This Scala Object represent DB Operation of Product database table
 *
 * @author a574891
 */
class ProductDAO {

}

/**
 * Dao class Object
 */
object ProductDaoObject {
  val logger = LoggerFactory.getLogger(classOf[ProductTable])
  // The query interface for the product table
  val productQuery: TableQuery[ProductTable] = TableQuery[ProductTable]
  // The query interface for the code_book table
  val codeBookQuery: TableQuery[CodeBook] = TableQuery[CodeBook]
  //product list containing all product records
  val productList = new ListBuffer[ProductDBRecord]

  /**
   * Get All Product List
   */
  def getProductList(): List[ProductDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    logger.debug("ProductDAO :: getProductList >>")
    productList.clear()
    Await.result(
      db.run(productQuery.result).map { x =>
        x.foreach { prodRec =>
          productList.append(prodRec)
          logger.debug(s"ProductDAO :: prodRec >> $prodRec")
          logger.debug(s"ProductDAO :: productList >> $productList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"ProductDAO :: productList.toList >> ${productList.toList}")
    productList match {
      case productList if (productList != null && productList.length > 0) => productList.toList
      case _ => null
    }
  }

  /**
   * get Product by ProdId
   */
  def getProductById(productId: Int): ProductDBRecord = {
    val db = UtpDaoObject.getDBConnection()
    // Filter Query Upon prodId column
    logger.debug("ProductDAO :: getProductById >>")
    val productFilterQuery = productQuery.filter(_.prodId === productId)
    productList.clear()
    Await.result(
      db.run(productFilterQuery.result).map { x =>
        x.foreach { prodRec =>
          productList.append(prodRec)
          logger.debug(s"ProductDAO :: prodRec >> $prodRec")
          logger.debug(s"ProductDAO :: productList >> $productList")
        }
      }, Duration.Inf)
    /*db.close */
    logger.debug(s"ProductDAO :: productList.apply(0) >> ${productList.apply(0)}")
     productList match {
      case productList if (productList != null && productList.length > 0) => productList.apply(0)
      case _ => null
    }
  }

  /**
   *  Get the ID of the linked parent product which has a product type of Primary Product Group (PPG)
   */
  def getProductTypeById(productId: Int): Int = {
    val p: ProductDBRecord = getProductById(productId)
    p.prtId

  }

  /**
   * 
   */
  def getProductByCode(productCode: String): List[ProductDBRecord] = {
    val db = UtpDaoObject.getDBConnection()
    //Filter Query Upon prodCode column
    logger.debug("ProductDAO :: getProductByCode >>")
    val productFilterQuery = productQuery.filter(_.productCode === productCode)
    productList.clear()
    Await.result(db.run(productFilterQuery.result).map { x =>
      x.foreach { prodRec =>
        productList.append(prodRec)
        logger.debug(s"ProductDAO :: prodRec >> $prodRec")
        logger.debug(s"ProductDAO :: productList >> $productList")
      }
    }, Duration.Inf)
    /*db.close */()
    logger.debug(s"ProductDAO :: productList.toList >> ${productList.toList}")
     productList match {
      case productList if (productList != null && productList.length > 0) => productList.toList
      case _ => null
    }
  }

}
