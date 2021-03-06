package com.atos.worldline.utp.record

import java.sql.Timestamp
import java.sql.Date
import java.util.Calendar
import org.joda.time.DateTime

/**
 * This Scala case class for Fare database record for UTP
 * @author a574891
 */
case class FareDBRecord(issuingLocation: Option[String], originLocation: Option[String], destinationLocation: Option[String], sellingLocation: Option[String], route: Option[String], product: Option[String], ticketStatus: Option[String], fare: Option[BigDecimal] = Some(7.65), value: Int = 7, withEffectFrom: Timestamp = new Timestamp(new DateTime(2015, 12, 31, 12, 0).getMillis()), withEffectUntil: Timestamp = new Timestamp(new DateTime(2015, 12, 31, 12, 0).getMillis()), expired: Option[String] = Some("N"), effectiveTo: Option[Date] = Some(new Date(Calendar.getInstance.getTime().getTime())), effectiveFrom: Option[Date] = Some(new Date(Calendar.getInstance.getTime().getTime())), nullFare: String = "N")                        
                        