package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import java.sql.Date
import com.atos.worldline.utp.record.CurrencyDBRecord
import com.atos.worldline.utp.record.CodeBookDBRecord
import com.atos.worldline.utp.record.TypeDBRecord

/**
 * This class represent code_book table into database
 * @author a574891
 */
class CodeBook(tag: Tag)
    extends Table[CodeBookDBRecord](tag, "code_book") {

  // This is the primary key column:
  def cobId: Rep[Int] = column[Int]("COB_ID", O.PrimaryKey)
  def value: Rep[Option[String]] = column[String]("VALUE", O.Nullable)
  def externalRepresentation: Rep[Option[String]] = column[String]("EXTERNAL_REPRESENTATION", O.Nullable)
  def maintainValue: Rep[Option[String]] = column[String]("MAINTAIN_VALUE", O.Nullable)
  def maintainExtRep: Rep[Option[String]] = column[String]("MAINTAIN_EXT_REP", O.Nullable)
 /* def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)*/
  def maintainParentCodeBook: Rep[Option[String]] = column[String]("MAINTAIN_PARENT_CODE_BOOK", O.Nullable)
  def codeBookCobId: Rep[Int] = column[Int]("code_book_cob_id", O.PrimaryKey)
  def typeId: Rep[Int] = column[Int]("TYP_ID", O.NotNull)
  def fk_code_book_code_book1 = foreignKey("code_book_cob_id", codeBookCobId, TableQuery[CodeBook])(_.cobId)
  def COB_TYP_FK = foreignKey("fk_code_book_type1", typeId, TableQuery[TypeMaster])(_.typeId)

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (cobId, value, externalRepresentation, maintainValue, maintainExtRep, maintainParentCodeBook, codeBookCobId, typeId) <> (CodeBookDBRecord.tupled, CodeBookDBRecord.unapply)

}

