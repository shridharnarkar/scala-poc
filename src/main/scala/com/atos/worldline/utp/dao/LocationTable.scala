package com.atos.worldline.utp.dao

import java.sql.Date

import com.atos.worldline.utp.record.LocationDBRecord

import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery

/**
 * @author a162012
 */
class LocationTable(tag: Tag)
    extends Table[LocationDBRecord](tag, "LOCATION") {
  def locationId: Rep[Int] = column[Int]("loc_id", O.PrimaryKey)
  def location: Rep[Option[String]] = column[String]("location", O.Nullable)
  def locationDesc: Rep[Option[String]] = column[String]("location_description", O.Nullable)
  def lotId: Rep[Int] = column[Int]("lot_id")
  def address: Rep[Option[String]] = column[String]("address", O.Nullable)
  def postCode: Rep[Option[String]] = column[String]("postcode", O.Nullable)
  def telephone: Rep[Option[String]] = column[String]("telephone", O.Nullable)
/*  def primaryZoneLocId: Rep[Option[Int]] = column[Int]("loc_id_primary_zone", O.Nullable)
  def primaryZoneCode: Rep[Option[String]] = column[String]("primary_zone_code", O.Nullable)
  def primaryZoneDesc: Rep[Option[String]] = column[String]("primary_zone_desc", O.Nullable)
  def secondaryZoneLocId: Rep[Option[Int]] = column[Int]("loc_id_secondary_zone", O.Nullable)
  def secondaryZoneCode: Rep[Option[String]] = column[String]("secondary_zone_code", O.Nullable)
  def secondaryZoneDesc: Rep[Option[String]] = column[String]("secondary_zone_desc", O.Nullable)
  def tertiaryZoneLocId: Rep[Option[Int]] = column[Int]("loc_id_tertiary_zone", O.Nullable)
  def tertiaryZoneCode: Rep[Option[String]] = column[String]("tertiary_zone_code", O.Nullable)
  def tertiaryZoneDesc: Rep[Option[String]] = column[String]("tertiary_zone_desc", O.Nullable)
  def tvStationLocId: Rep[Option[Int]] = column[Int]("loc_id_tv_station", O.Nullable)
  def tvStationCode: Rep[Option[String]] = column[String]("tv_station_code", O.Nullable)
  def tvStationName: Rep[Option[String]] = column[String]("tv_station_name", O.Nullable)
  def localAuthLocId: Rep[Option[Int]] = column[Int]("loc_id_local_authority", O.Nullable)
  def localAuthCode: Rep[Option[String]] = column[String]("local_authority_code", O.Nullable)
  def localAuthName: Rep[Option[String]] = column[String]("local_authority_name", O.Nullable)
  def stationGrpLocId: Rep[Option[Int]] = column[Int]("loc_id_station_group", O.Nullable)
  def stationGrp: Rep[Option[String]] = column[String]("station_group", O.Nullable)
  def stationGrpDesc: Rep[Option[String]] = column[String]("station_group_desc", O.Nullable)
  def londonStationGrpLocId: Rep[Option[Int]] = column[Int]("loc_id_london_station_grp", O.Nullable)
  def londonStationGrpCode: Rep[Option[String]] = column[String]("london_station_grp_code", O.Nullable)
  def londonStationGrpDesc: Rep[Option[String]] = column[String]("london_station_grp_desc", O.Nullable)
  def osGridReference: Rep[Option[String]] = column[String]("os_grid_reference", O.Nullable)
  def postalRegion: Rep[Option[String]] = column[String]("postal_region", O.Nullable)
  def nalco12CharDesc: Rep[Option[String]] = column[String]("nalco_12char_description", O.Nullable)
  def nalco16CharDesc: Rep[Option[String]] = column[String]("nalco_16char_description", O.Nullable)
  def nalco26CharDesc: Rep[Option[String]] = column[String]("nalco_26char_description", O.Nullable)
  def nalcoTipLoc: Rep[Option[String]] = column[String]("nalco_tiploc", O.Nullable)
  def nalco3CharCode: Rep[Option[String]] = column[String]("nalco_3char_code", O.Nullable)
  def effectiveFrom: Rep[Option[Date]] = column[Date]("effective_from", O.Nullable)
  def effectiveTo: Rep[Option[Date]] = column[Date]("effective_to", O.Nullable)
  def expired: Rep[Option[String]] = column[String]("expired", O.Nullable)
  def mstAcctLocId: Rep[Option[Int]] = column[Int]("loc_id_master_account", O.Nullable)
  def mstAcctCode: Rep[Option[String]] = column[String]("master_account_code", O.Nullable)
  def mstAcctName: Rep[Option[String]] = column[String]("master_account_name", O.Nullable)*/
  def cobIdGroupType: Rep[Int] = column[Int]("cob_id_group_type")

  def fk_location_code_book1 = foreignKey("fk_location_code_book1", cobIdGroupType, TableQuery[CodeBook])(_.codeBookCobId)

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (locationId, location, locationDesc, lotId, address, postCode, telephone, cobIdGroupType) <>  (LocationDBRecord.tupled, LocationDBRecord.unapply)
      
 // def * = (locationId, location, locationDesc, lotId, address, postCode, telephone, primaryZoneLocId, primaryZoneCode, primaryZoneDesc, secondaryZoneLocId, secondaryZoneCode, secondaryZoneDesc, tertiaryZoneLocId, tertiaryZoneCode, tertiaryZoneDesc, tvStationLocId, tvStationCode, tvStationName, localAuthLocId, localAuthCode, localAuthName, stationGrpLocId, stationGrp, stationGrpDesc, londonStationGrpLocId, londonStationGrpCode, londonStationGrpDesc, osGridReference, postalRegion, nalco12CharDesc, nalco16CharDesc, nalco26CharDesc, nalcoTipLoc, nalco3CharCode, effectiveFrom, effectiveTo, expired, mstAcctLocId, mstAcctCode, mstAcctName, cobIdGroupType) <> (LocationDBRecord.tupled, LocationDBRecord.unapply)
      
     
}