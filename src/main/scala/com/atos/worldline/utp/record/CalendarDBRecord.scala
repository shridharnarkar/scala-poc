package com.atos.worldline.utp.record
import java.sql.Date
/**
 * @author a579482
 */
case class CalendarDBRecord(calId: Int, calendarDate: Option[Date], dayOfWeek: Option[String], period: Int, year: Int, week: Int)
  