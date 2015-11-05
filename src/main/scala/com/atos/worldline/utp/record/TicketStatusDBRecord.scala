package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a162012
 */
case class TicketStatusDBRecord(tisId: Int, expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date],
    ticketStatusCode: Option[String], ticketStatusDes: Option[String], percentDiscount: Option[BigDecimal], 
    ratRailcardTypeDesc: Option[String], ticketStatusTypeDesc: Option[String])