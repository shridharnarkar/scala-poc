package com.atos.worldline.utp.record

import java.sql.Timestamp
import java.sql.Date
import java.util.Calendar
import org.joda.time.DateTime
/**
 * This Scala case class for group_function_type database record for UTP
 * @author a574891
 */
case class GroupFunctionTypeDBRecord(gftId: Int, groupFunctionTypeDesc: Option[String], expired: Option[String] = Some("N"), effectiveTo: Option[Date] = Some(new Date(Calendar.getInstance.getTime().getTime())), effectiveFrom: Option[Date] = Some(new Date(Calendar.getInstance.getTime().getTime()))) 