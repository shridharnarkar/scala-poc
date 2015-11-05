package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import java.sql.Date
import java.sql.Timestamp
import com.atos.worldline.utp.record.LocationLinkDBRecord
import org.joda.time.DateTime

/**
 * @author a579482
 */
class LocationLink(tag: Tag)
    extends Table[LocationLinkDBRecord](tag, "Location_Link") {

  // This is the primary key column:
  def lolId: Rep[Int] = column[Int]("lol_id", O.PrimaryKey) 
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)  
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def locId: Rep[Int] = column[Int]("loc_Id")
  def busId: Rep[Int] = column[Int]("bus_id")
  def gftId: Rep[Int] = column[Int]("gft_id")
    

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (lolId, expired, effectiveFrom, effectiveTo, locId, busId, gftId) <> (LocationLinkDBRecord.tupled, LocationLinkDBRecord.unapply)
}
