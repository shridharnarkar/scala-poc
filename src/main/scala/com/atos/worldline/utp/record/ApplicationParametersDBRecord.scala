package com.atos.worldline.utp.record
import java.sql.Date

/**
 * @author a579482
 */
case class ApplicationParametersDBRecord(id: Int, name: String, integerValue: Int, expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date] )
