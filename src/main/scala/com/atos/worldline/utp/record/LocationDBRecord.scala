package com.atos.worldline.utp.record

import java.sql.Date

/**
 * This Scala case class for location database record for UTP
 * @author a162012
 */
case class LocationDBRecord(locationId: Int, location: Option[String], locationDesc: Option[String],lotId: Int, address: Option[String], postcode: Option[String], telephone: Option[String],cobIdGroupType: Int)



                           /*
                            * TODO Right now these fild attribute not required 
                            * 
                            * Note : tuple accepting only 22 parameters  implementation restricts functions to 22 parameters 
                            * 
                            * primaryZoneLocId: Option[Int], primaryZoneCode: Option[String], primaryZoneDesc: Option[String],
                            secondaryZoneLocId: Option[Int], secondaryZoneCode: Option[String], secondaryZoneDesc: Option[String],
                            tertiaryZoneLocId: Option[Int], tertiaryZoneCode: Option[String], tertiaryZoneDesc: Option[String],
                            tvStationLocId: Option[Int], tvStationCode: Option[String], tvStationName: Option[String],
                            localAuthLocId: Option[Int], localAuthCode: Option[String], localAuthName: Option[String],
                            stationGrpLocId: Option[Int])
                             stationGrp: Option[String], stationGrpDesc: Option[String])
                            londonStationGrpLocId: Option[Int], londonStationGrpCode: Option[String], londonStationGrpDesc: Option[String],
                            osGridReference: Option[String], postalRegion: Option[String], nalco12CharDesc: Option[String],
                            nalco16CharDesc: Option[String], nalco26CharDesc: Option[String], nalcoTipLoc: Option[String],
                            nalco3CharCode: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date], expired: Option[String],
                            mstAcctLocId: Option[Int], mstAcctCode: Option[String], mstAcctName: Option[String],  cobIdGroupType: Int)     
                            cobIdGroupType: Int)*/
