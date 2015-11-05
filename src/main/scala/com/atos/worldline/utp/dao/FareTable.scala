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
 
  def originLocation: Rep[String] = column[String]("origin_location")
  def destinationLocation: Rep[String] = column[String]("destination_location")
  def sellingLocation: Rep[String] = column[String]("selling_location")
  def route: Rep[String] = column[String]("route")
  def product: Rep[String] = column[String]("product")
  def ticketStatus: Rep[String] = column[String]("ticketStatus")
  def fare: Rep[Double] = column[Double]("fare")
  def value: Rep[Int] = column[Int]("value")
  def withEffectFrom: Rep[Timestamp] = column[Timestamp]("with_effect_from")
  def withEffectUntil: Rep[Timestamp] = column[Timestamp]("with_effect_until")
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (originLocation, destinationLocation, sellingLocation, route, product, ticketStatus, fare, value, withEffectFrom, withEffectUntil, expired, effectiveTo, effectiveFrom) <> (FareDBRecord.tupled, FareDBRecord.unapply)

}