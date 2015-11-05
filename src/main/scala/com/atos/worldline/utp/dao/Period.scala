package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.PeriodDBRecord

/**
 * This class represent PERIOD table into database
 * @author a574891
 */
class Period (tag: Tag)
    extends Table[PeriodDBRecord](tag, "PERIOD") {

  // This is the primary key column:
  def periodId: Rep[Int] = column[Int]("per_id", O.PrimaryKey) 
  def year: Rep[Int] = column[Int]("year")  
  def period: Rep[Option[Int]] = column[Int]("integer_value", O.Nullable)
  def startDate: Rep[Option[Date]] = column[Date]("start_date", O.Nullable)
  def endDate: Rep[Option[Date]] = column[Date]("end_date", O.Nullable)
  def status: Rep[Option[String]] = column[String]("status", O.Nullable)
  def periodMask: Rep[Option[String]] = column[String]("period_mask", O.Nullable)
    

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (periodId, year, period, startDate, endDate, status, periodMask) <> (PeriodDBRecord.tupled, PeriodDBRecord.unapply)
}