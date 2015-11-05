package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a579482
 */
case class PeriodDBRecord(periodId: Int , year: Int, period: Option[Int] ,startDate: Option[Date], endDate: Option[Date], status: Option[String], periodMask: Option[String])