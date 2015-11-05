package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a579482
 */
case class DefaultAuditDBRecord(deaId: BigDecimal, shiftDate: Date, processingDate: Date, recordContents: String, macId: Option[BigDecimal])