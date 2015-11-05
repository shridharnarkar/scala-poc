package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.FaresCheckingExclusionsDBRecord

/**
 * @author a579482
 */
class FaresCheckingExclusions(tag: Tag)
    extends Table[FaresCheckingExclusionsDBRecord](tag, "FARES_CHECKING_EXCLUSIONS") {

  // This is the primary key column:
  def fchId: Rep[BigDecimal] = column[BigDecimal]("FCH_ID", O.PrimaryKey)
  def issuingLocation: Rep[Option[String]] = column[String]("ISSUING_LOCATION", O.Nullable)
  def originLocation: Rep[Option[String]] = column[String]("ORIGIN_LOCATION", O.Nullable)
  def destinationLoction: Rep[Option[String]] = column[String]("DESTINATION_LOCATION", O.Nullable)
  def route: Rep[Option[String]] = column[String]("ROUTE", O.Nullable)
  def product: Rep[Option[String]] = column[String]("PRODUCT", O.Nullable)
  def ticketStatus: Rep[Option[String]] = column[String]("TICKET_STATUS", O.Nullable)
  def fare: Rep[Option[BigDecimal]] = column[BigDecimal]("FARE", O.Nullable)
  def withEffectFrom: Rep[Date] = column[Date]("WITH_EFFECT_FROM")
  def withEffectUntil: Rep[Option[Date]] = column[Date]("WITH_EFFECT_UNTIL", O.Nullable)
  def nullFare: Rep[String] = column[String]("NULL_FARE")
  def sellingLocation: Rep[Option[String]] = column[String]("SELLING_LOCATION", O.Nullable)
  
  
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (fchId, issuingLocation, originLocation, destinationLoction, route, product, ticketStatus, fare, withEffectFrom, withEffectUntil, nullFare, sellingLocation) <> (FaresCheckingExclusionsDBRecord.tupled, FaresCheckingExclusionsDBRecord.unapply)

}