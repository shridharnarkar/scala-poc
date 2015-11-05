package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a579482
 */
case class BusinessDBRecord(busId: Int, busName: Option[String], euroConversionDate: Option[Date], busIdParent: Option[Int] )