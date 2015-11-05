package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.DefaultAuditDBRecord

/**
 * @author a579482
 */
class DefaultAudit (tag: Tag)
    extends Table[DefaultAuditDBRecord](tag, "DEFAULT_AUDIT") {

  // This is the primary key column:
  def deaId: Rep[BigDecimal] = column[BigDecimal]("DEA_ID", O.PrimaryKey) 
  def shiftDate: Rep[Date] = column[Date]("SHIFT_DATE")  
  def processingDate: Rep[Date] = column[Date]("PROCESSING_DATE")
  def recordContents: Rep[String] = column[String]("RECORD_CONTENTS")
  def macId: Rep[Option[BigDecimal]] = column[BigDecimal]("MAC_ID", O.Nullable)


  // Every table needs a * projection with the same type as the table's type parameter
  def * = (deaId, shiftDate, processingDate, recordContents, macId) <> (DefaultAuditDBRecord.tupled, DefaultAuditDBRecord.unapply)

  
}