package com.atos.worldline.utp.record

import java.sql.Date

/**
 * This Scala case class for CodeBook database record for UTP
 * @author a574891
 */
case class CodeBookDBRecord(cobId: Int, value: Option[String], externalRepresentation: Option[String], maintainValue: Option[String], maintainExtRep: Option[String], maintainParentCodeBook: Option[String], codeBookCobId: Int, typeId: Int)
    
    