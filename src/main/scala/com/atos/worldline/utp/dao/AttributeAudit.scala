package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.AttributeAuditDBRecord

/**
 * @author a579482
 */
class AttributeAudit(tag: Tag)
    extends Table[AttributeAuditDBRecord](tag, "ATTRIBUTE_AUDIT") {

  // This is the primary key column:
  def originalValue: Rep[Option[String]] = column[String]("ORIGINAL_VALUE", O.Nullable)
  def refId: Rep[Option[BigDecimal]] = column[BigDecimal]("REF_ID", O.Nullable)
  def ataId: Rep[BigDecimal] = column[BigDecimal]("ATA_ID", O.PrimaryKey)
  def deaId: Rep[Option[BigDecimal]] = column[BigDecimal]("DEA_ID", O.Nullable)
  def defaultApplied: Rep[Option[String]] = column[String]("DEFAULT_APPLIED", O.Nullable)

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (originalValue, refId, ataId, deaId, defaultApplied) <> (AttributeAuditDBRecord.tupled, AttributeAuditDBRecord.unapply)

}