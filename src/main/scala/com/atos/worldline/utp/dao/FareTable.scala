package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import java.sql.Date
import java.sql.Timestamp
import com.atos.worldline.utp.record.FareDBRecord
import org.joda.time.DateTime

/**
 * This class represent Fare table into database
 * @author a574891
 */
class Fare(tag: Tag)
    extends Table[FareDBRecord](tag, "FARE") {
  def issuingLocation: Rep[Option[String]]  = column[String]("issuing_location", O.Nullable)
  def originLocation: Rep[Option[String]] = column[String]("origin_location", O.Nullable)
  def destinationLocation: Rep[Option[String]] = column[String]("destination_location", O.Nullable)
  def sellingLocation: Rep[Option[String]]  = column[String]("selling_location", O.Nullable)
  def route: Rep[Option[String]]  = column[String]("route", O.Nullable)
  def product: Rep[Option[String]]  = column[String]("product", O.Nullable)
  def ticketStatus:Rep[Option[String]]  = column[String]("ticketStatus", O.Nullable)
  def fare: Rep[Option[BigDecimal]] = column[BigDecimal]("fare", O.Nullable)
  def value: Rep[Int] = column[Int]("value")
  def withEffectFrom: Rep[Timestamp] = column[Timestamp]("with_effect_from")
  def withEffectUntil: Rep[Timestamp] = column[Timestamp]("with_effect_until")
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def nullFare: Rep[String] = column[String]("NULL_FARE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (issuingLocation, originLocation, destinationLocation, sellingLocation, route, product, ticketStatus, fare, value, withEffectFrom, withEffectUntil, expired, effectiveTo, effectiveFrom,nullFare) <> (FareDBRecord.tupled, FareDBRecord.unapply)

}