package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import com.atos.worldline.utp.record.ProductDBRecord
import java.sql.Date

/**
 * This class represent product table into database
 * @author a574891
 */
class ProductTable(tag: Tag)
    extends Table[ProductDBRecord](tag, "PRODUCT") {

  // This is the primary key column:

  def prodId: Rep[Int] = column[Int]("pro_id", O.PrimaryKey)
  def productCode: Rep[Option[String]] = column[String]("product_code")
  def productDescription: Rep[Option[String]] = column[String]("product_description")
  def jofJourneyFactor: Rep[Option[BigDecimal]] = column[BigDecimal]("jof_journey_factor")
  def prodIdGroup1: Rep[Option[Int]] = column[Int]("pro_id_group_1") /*ID of the linked parent product which has a product type of Primary Product Group (PPG)*/
  def proGroup1Code: Rep[Option[String]] = column[String]("pro_group_1_code")
  def proGroup1Desc: Rep[Option[String]] = column[String]("pro_group_1_desc")
  def proGroup2: Rep[Option[Int]] = column[Int]("pro_id_group_2")
  def proGroup2Code: Rep[Option[String]] = column[String]("pro_group_2_code")
  def proGroup2Desc: Rep[Option[String]] = column[String]("pro_group_2_desc")
  def expired: Rep[Option[String]] = column[String]("expired")
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from")
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to")
  def busIdCorrective: Rep[Option[Int]] = column[Int]("bus_id_corrective")
  def excludeFromNgtSundries: Rep[Option[String]] = column[String]("exclude_from_ngt_sundries")
  def business: Rep[Int] = column[Int]("business")
  def prtId: Rep[Int] = column[Int]("prt_id")
  def cobIdDebitCreditType: Rep[Int] = column[Int]("cob_id_debit_credit_type")
  def cobIdRailcardType: Rep[Int] = column[Int]("cob_id_railcard_type")
  def cobIdSuspendableInd: Rep[Int] = column[Int]("cob_id_suspendable_ind")
  def ticId: Rep[Int] = column[Int]("tic_id")
  def tacId: Rep[Int] = column[Int]("tac_id")
  
  def fk_product_business1 = foreignKey("fk_product_business1", business, TableQuery[Business])(_.busId)
  def fk_product_code_book1 = foreignKey("fk_product_code_book1", cobIdDebitCreditType, TableQuery[CodeBook])(_.codeBookCobId)
  def fk_product_code_book2 = foreignKey("fk_product_code_book2", cobIdRailcardType, TableQuery[CodeBook])(_.codeBookCobId)
  def fk_product_code_book3 = foreignKey("fk_product_code_book3", cobIdSuspendableInd, TableQuery[CodeBook])(_.codeBookCobId)
  
  /*def fk_product_product_type1 = foreignKey("fk_product_product_type1", cobIdDebitCreditType, TableQuery[ProductType])(_.code_book_cob_id)
  def fk_product_ticket_allocation_code1 = foreignKey("fk_product_ticket_allocation_code1", tacId, TableQuery[TicketAllocationCode])(_.tacId)
  def fk_product_ticket_class1 = foreignKey("fk_product_ticket_class1", ticId, TableQuery[TicketClass])(_.ticId)
*/
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (prodId, productCode, productDescription, jofJourneyFactor, prodIdGroup1, proGroup1Code, proGroup1Desc, proGroup2, proGroup2Code, proGroup2Desc, expired, effectiveFrom , effectiveTo, busIdCorrective, excludeFromNgtSundries, business, prtId, cobIdDebitCreditType, cobIdRailcardType, cobIdSuspendableInd, ticId, tacId) <> (ProductDBRecord.tupled, ProductDBRecord.unapply)
}