package com.atos.worldline.utp.fileprocessing

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import com.atos.worldline.utp.constants.RecordAttribute
import com.atos.worldline.utp.record.ZRecord
import scala.collection.mutable.ListMap

/**
 * @author a574891
 */

@RunWith(classOf[JUnitRunner])
class FileInputTransformerSuit extends FunSuite with BeforeAndAfter {

  val zRrecordLine = "R99999946741680246366449809170000000000000000000000 0000000000ZG050483050483050483000008002545000001000313000320000000 00520150908182800000000+00001                    00000000000307 0000000125.00644980917020150901 00+00304168024636         +81923 00000 0000       2015093000000000+00001+01 0020150909010239591000000059        0000000000000000000444000000000000000000000000000000000000000000000000 00000432000000 0000000000.00 0000000000.0000030020160607601600010440+10782000000023848023848000000      20150909YY0.0000076016000000"
  val attrbs: scala.collection.mutable.ListMap[String, String] = scala.collection.mutable.ListMap()
  val transformedZRecord = new ZRecord(ListMap("SellingMachineonIssue" -> "076016", "RunDate" -> "20150909", "IssuingWindow" -> "023848", "IssuingMachine" -> "076016", "RefundValue" -> " 0000000000.00", "MultipleIssueMarker" -> "000000", "CrossLondonMarker" -> "000000", "FareMethodMarker" -> "000444", "OriginRecordType" -> "000059 ", "NumberofTickets" -> "+00001", "NumberofSundryItems" -> "0000 ", "RetailTransactionID" -> "4168024636", "RetailItemID" -> "6449809170", "UNUSED" -> "            ", "DiscountPercentage" -> " 005", "TicketStatus" -> "000001", "SellingLocation" -> "050483", "SortKey" -> "R99999946741680246366449809170000000000000000000000 0000000000", "IssuingLocation" -> "050483", "Route" -> "002545", "DiscountCode" -> "000000", "DeferredIssueIdentifier" -> "        ", "TransactionValue" -> " 0000000125.00", "PeriodofValidity" -> "+0030", "SundryTransactionNumber" -> " 00000", "SeasonDateLastUsed" -> "00000000", "DateProcessed" -> "20150909010239591000", "NonIssueMarker" -> "000000", "AdvanceIssueMarker" -> "000000", "SystemGeneratedMarker" -> "000000", "ManualInputMarker" -> "000000", "AccountablePeriod" -> "201606", "DefaultAudit" -> "000000", "WarrantAccountHolder_loc_id_wah" -> "      ", "IssueCommissionProportion" -> "0.0000", "MatchingMarker" -> "000000", "PassengerCharterMarker" -> "Y", "SellingWindow" -> "023848", "OperatorID" -> "00010440", "Filler" -> " 0000000000.00", "PromotionCode" -> " 00", "BookingMarker" -> "000000", "RefundMarker" -> "000000", "EurostarFareBasis" -> "       ", "NumberofAdults" -> "+01", "BankGiroCreditSerial" -> "      ", "TicketNumber" -> "         ", "DateofTravelorSeasonStartDate" -> "20150901 ", "DateofSale" -> "00000000", "DateandTimeofIssue" -> "20150908182800000000", "ApportionmentMOPBasis" -> "000313", "Origin" -> "050483", "RecordType" -> "Z", "ApportionmentIndicator" -> "G", "Destination" -> "000008", "Product" -> "000320", "NumberofPeople" -> "+00001", "DeferredIssueType" -> "000307", "LostDays" -> "00", "TransactionNumber" -> "+81923", "SeasonTicketEndDate" -> "20150930", "NumberofChildren" -> " 00", "AccountingSundry" -> "0000000000", "BarCodeInputMarker" -> "000000", "MinorMaxFareMarker" -> "000000", "RecreatedDataMarker" -> "000432", "Currency" -> "000300", "SaleNumber" -> "+10782", "CreditCardAcquirer" -> "000000", "FaresCheckingMarker" -> "Y"))
  
  before {
  }

  test("Extract/Transform the ZRecord record attribute value") {

    RecordAttribute.AttbMap foreach { 
      case (dKey, dVal) => attrbs.put(dKey, zRrecordLine.substring(dVal(0), dVal(1)))
                          //println(dKey + "   >>" + attrbs.get(dKey))
    }
     println(ZRecord(attrbs))
    assertResult(transformedZRecord)(ZRecord(attrbs))
  }
}