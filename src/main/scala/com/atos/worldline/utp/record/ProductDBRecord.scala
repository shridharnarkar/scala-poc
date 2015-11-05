package com.atos.worldline.utp.record

import java.sql.Date

/**
 * This Scala case class for product database record for UTP
 * @author a574891
 */
case class ProductDBRecord(prodId: Int, productCode: Option[String], productDescription: Option[String], jofJourneyFactor: Option[BigDecimal],
                           prodIdGroup1: Option[Int], proGroup1Code: Option[String], proGroup1Desc: Option[String], proGroup2: Option[Int], proGroup2Code: Option[String],
                           proGroup2Desc: Option[String], expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date], busIdCorrective: Option[Int],
                           excludeFromNgtSundries: Option[String], business: Int, prtId: Int, cobIdDebitCreditType: Int, cobIdRailcardType: Int,
                           cobIdSuspendableInd: Int, ticId: Int, tacId: Int) 
    
    