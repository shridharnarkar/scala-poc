package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.ImportAttributeDBRecord

/**
 * @author a579482
 */
class ImportAttribute(tag: Tag)
    extends Table[ImportAttributeDBRecord](tag, "IMPORT_ATTRIBUTE") {

  // This is the primary key column:
  def imaID: Rep[BigDecimal] = column[BigDecimal]("IMA_ID", O.PrimaryKey)
  def name: Rep[String] = column[String]("NAME")
  def cRefNo: Rep[BigDecimal] = column[BigDecimal]("C_REF_NO")


  // Every table needs a * projection with the same type as the table's type parameter
  def * = (imaID, name, cRefNo) <> (ImportAttributeDBRecord.tupled, ImportAttributeDBRecord.unapply)
}