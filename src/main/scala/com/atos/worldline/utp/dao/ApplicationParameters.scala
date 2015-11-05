package com.atos.worldline.utp.dao


import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.ApplicationParametersDBRecord

/**
 * This class represent PERIOD table into database
 * @author a574891
 */

class ApplicationParameters (tag: Tag)
    extends Table[ApplicationParametersDBRecord](tag, "APPLICATIONPARAMETERS") {

  // This is the primary key column:
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey) 
  def name: Rep[String] = column[String]("name")  
  def integerValue: Rep[Int] = column[Int]("integer_value")
  def expired: Rep[Option[String]] = column[String]("integer_value", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
    

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, integerValue, expired, effectiveFrom, effectiveTo) <> (ApplicationParametersDBRecord.tupled, ApplicationParametersDBRecord.unapply)
}