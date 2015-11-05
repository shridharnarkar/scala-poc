package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import java.sql.Date
import java.sql.Timestamp
import com.atos.worldline.utp.record.GroupFunctionTypeDBRecord
import org.joda.time.DateTime

/**
 * This class represent group_function_type table into database
 * 
 * @author a574891
 */
class GroupFunctionType(tag: Tag)
    extends Table[GroupFunctionTypeDBRecord](tag, "group_function_type") {

  // This is the primary key column:
  def gftId: Rep[Int] = column[Int]("gft_id", O.PrimaryKey) 
  def groupFunctionTypeDesc: Rep[Option[String]] = column[String]("group_function_type_desc", O.Nullable)  
 def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)  
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  // Every table needs a * projection with the same type as the table's type parameter
  //def * : ProvenShape[(Int, String, Date)] = (busId, busName, euroConversionDate)
  def * = (gftId, groupFunctionTypeDesc, expired,effectiveFrom,effectiveTo) <> (GroupFunctionTypeDBRecord.tupled, GroupFunctionTypeDBRecord.unapply)
}
