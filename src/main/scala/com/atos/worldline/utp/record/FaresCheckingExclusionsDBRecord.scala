package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a579482
 */
case class FaresCheckingExclusionsDBRecord(fchId: BigDecimal, issuingLocation: Option[String], originLocation: Option[String], destinationLoction: Option[String], route: Option[String], product: Option[String], ticketStatus: Option[String], fare: Option[BigDecimal], withEffectFrom: Date, withEffectUntil: Option[Date], nullFare: String, sellingLocatio: Option[String])