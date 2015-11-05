package com.atos.worldline.utp.record

import java.sql.Date

/**
 * This Scala case class for type database record for UTP
 * @author a574891
 */
case class TypeDBRecord(typeId: Int, typeCol: Option[String], maintainType: Option[String], expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date]) 
