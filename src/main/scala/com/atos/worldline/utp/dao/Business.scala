package com.atos.worldline.utp.dao

import java.sql.Date
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape
import com.atos.worldline.utp.record.BusinessDBRecord

/**
 * This class represent BUSINESS table into database
 * @author a574891
 */
class Business(tag: Tag)
    extends Table[BusinessDBRecord](tag, "BUSINESS") {

  // This is the primary key column:
  def busId: Rep[Int] = column[Int]("bus_id", O.PrimaryKey) 
  def busName: Rep[Option[String]] = column[String]("bus_name", O.Nullable)  
  def euroConversionDate: Rep[Option[Date]] = column[Date]("euro_conversion_date", O.Nullable)
  def busIdParent: Rep[Option[Int]] = column[Int]("bus_id_tp_parent", O.Nullable)
  

  // Every table needs a * projection with the same type as the table's type parameter
  //def * : ProvenShape[(Int, String, Date)] = (busId, busName, euroConversionDate)
  def * = (busId, busName, euroConversionDate, busIdParent) <> (BusinessDBRecord.tupled, BusinessDBRecord.unapply)
}