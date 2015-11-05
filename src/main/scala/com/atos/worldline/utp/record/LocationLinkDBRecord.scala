package com.atos.worldline.utp.record

import java.sql.Date
/**
 * @author a579482
 */
case class LocationLinkDBRecord (lolId: Int, expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date], locId: Int, busId: Int, gftId: Int)