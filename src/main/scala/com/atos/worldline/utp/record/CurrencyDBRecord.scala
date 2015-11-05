package com.atos.worldline.utp.record

import java.sql.Date

/**
 * This Scala case class for currency database record for UTP
 * @author a574891
 */
case class CurrencyDBRecord(currencyRate: BigDecimal, conversionPower:Int, currencyDate: Date) 