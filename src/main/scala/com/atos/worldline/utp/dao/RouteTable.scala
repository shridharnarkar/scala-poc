package com.atos.worldline.utp.dao

import slick.driver.MySQLDriver.simple._
import slick.lifted.{ ProvenShape, ForeignKeyQuery }
import com.atos.worldline.utp.record.RouteDBRecord
import java.sql.Date

/**
 * This class represent product table into database
 *
 * @author a162012
 */
class RouteTable(tag: Tag) extends Table[RouteDBRecord](tag, "ROUTE") {
  def routeId: Rep[Int] = column[Int]("rou_id", O.PrimaryKey)
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def routeCode: Rep[Option[String]] = column[String]("route_code", O.Nullable)
  def routeDesc: Rep[Option[String]] = column[String]("route_description", O.Nullable)
  def tacId: Rep[Int] = column[Int]("tac_id")
  def cobIdRouteType: Rep[Int] = column[Int]("cob_id_route_type")

  def fk_route_code_book1 = foreignKey("fk_route_code_book1", cobIdRouteType, TableQuery[CodeBook])(_.codeBookCobId)

  def * = (routeId, expired, effectiveFrom, effectiveTo, routeCode, routeDesc, tacId, cobIdRouteType) <> (RouteDBRecord.tupled, RouteDBRecord.unapply)
}