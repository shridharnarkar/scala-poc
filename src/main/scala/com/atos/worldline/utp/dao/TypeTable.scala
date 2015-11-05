package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import com.atos.worldline.utp.record.TypeDBRecord
import java.sql.Date

/**
 * This class represent type table into database
 * @author a574891
 */
class TypeMaster(tag: Tag)
    extends Table[TypeDBRecord](tag, "TYPE") {

  // This is the primary key column:
  def typeId: Rep[Int] = column[Int]("TYP_ID", O.PrimaryKey)
  def typeCol:Rep[Option[String]] = column[String]("TYPE")
  def maintainType:Rep[Option[String]] = column[String]("maintain_type")
  def expired: Rep[Option[String]] = column[String]("expired")
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from")
  def effectiveTo:Rep[Option[Date]] = column[Date]("effective_to")

  // Every table needs a * projection with the same type as the table's type parameter

  def * = (typeId, typeCol, maintainType, expired, effectiveFrom, effectiveTo) <> (TypeDBRecord.tupled, TypeDBRecord.unapply)
}