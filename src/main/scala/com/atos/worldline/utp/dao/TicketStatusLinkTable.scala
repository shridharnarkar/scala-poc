package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import com.atos.worldline.utp.record.TicketStatusLinkDBRecord
import java.sql.Date
import com.atos.worldline.utp.record.TicketStatusLinkDBRecord
import com.atos.worldline.utp.record.TicketStatusLinkDBRecord

/**
 * @author a162012
 */
class TicketStatusLinkTable(tag: Tag) extends Table[TicketStatusLinkDBRecord](tag, "TICKET_STATUS_LINK") {

  def tslId: Rep[Int] = column[Int]("tsl_id", O.PrimaryKey)
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def tisIdLinks: Rep[Int] = column[Int]("tis_id_links", O.NotNull)
  def tisIdLinkedBy: Rep[Int] = column[Int]("tis_id_linked_by", O.NotNull)
  
  def fk_ticket_status_link_ticket_status1 = foreignKey("fk_ticket_status_link_ticket_status1", tisIdLinks, TableQuery[TicketStatusTable])(_.tisId)
  def fk_ticket_status_link_ticket_status2 = foreignKey("fk_ticket_status_link_ticket_status2", tisIdLinkedBy, TableQuery[TicketStatusTable])(_.tisId)
  
  def * = (tslId, expired, effectiveFrom, effectiveTo, tisIdLinks, tisIdLinkedBy) <> (TicketStatusLinkDBRecord.tupled, TicketStatusLinkDBRecord.unapply)

}