package com.atos.worldline.utp.dao

/**
 * @author a579482
 */

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import java.sql.Date
import java.sql.Timestamp
import com.atos.worldline.utp.record.CalendarDBRecord
import org.joda.time.DateTime

/**
 * @author a579482
 */
class Calendar(tag: Tag)
    extends Table[CalendarDBRecord](tag, "CALENDAR") {
 
  def calId: Rep[Int] = column[Int]("cal_id", O.PrimaryKey)
  def calendarDate: Rep[Option[Date]] = column[Date]("calendar_date", O.Nullable)
  def dayOfWeek: Rep[Option[String]] = column[String]("day_of_week", O.Nullable)
  def period: Rep[Int] = column[Int]("period")
  def year: Rep[Int] = column[Int]("year")
  def week: Rep[Int] = column[Int]("week")
  
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (calId, calendarDate, dayOfWeek, period, year, week) <> (CalendarDBRecord.tupled, CalendarDBRecord.unapply)

}