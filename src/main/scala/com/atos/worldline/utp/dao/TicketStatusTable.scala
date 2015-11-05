package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import com.atos.worldline.utp.record.TicketStatusDBRecord
import java.sql.Date

/**
 * @author a162012
 */
class TicketStatusTable(tag: Tag) extends Table[TicketStatusDBRecord](tag, "TICKET_STATUS") {

  def tisId: Rep[Int] = column[Int]("tis_id", O.PrimaryKey)
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def ticketStatusCode: Rep[Option[String]] = column[String]("ticket_status_code", O.Nullable)
  def ticketStatusDesc: Rep[Option[String]] = column[String]("ticket_status_description", O.Nullable)
  def percentDiscount: Rep[Option[BigDecimal]] = column[BigDecimal]("percent_discount", O.Nullable)
  def ratRailcardTypeDesc: Rep[Option[String]] = column[String]("rat_railcard_type_desc", O.Nullable)
  def ticketStatusTypeDesc: Rep[Option[String]] = column[String]("ticket_status_type_desc", O.Nullable)
  
  def * = (tisId, expired, effectiveFrom, effectiveTo, ticketStatusCode, ticketStatusDesc, percentDiscount, ratRailcardTypeDesc, ticketStatusTypeDesc) <> (TicketStatusDBRecord.tupled, TicketStatusDBRecord.unapply)

}