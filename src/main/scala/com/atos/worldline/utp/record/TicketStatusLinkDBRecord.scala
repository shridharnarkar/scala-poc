package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a162012
 */
case class TicketStatusLinkDBRecord (tslId: Int, expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date], tisIdLinks: Int, tisIdLinkedBy: Int)