package com.atos.worldline.utp.rules

import java.sql.Date
import java.sql.Timestamp
import java.util.Calendar
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ListMap
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.mockito.Mockito
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.dao.BusinessDAO
import com.atos.worldline.utp.dao.CurrencyDAO
import com.atos.worldline.utp.dao.Fare
import com.atos.worldline.utp.record.BusinessDBRecord
import com.atos.worldline.utp.record.FareDBRecord
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.utils.Utilities
import com.atos.worldline.utp.record.FareDBRecord
import com.atos.worldline.utp.record.CurrencyDBRecord
import com.atos.worldline.utp.dao.FareDAO
import com.atos.worldline.utp.dao.BusinessDaoObject
import com.atos.worldline.utp.dao.CurrencyDaoObject
import com.atos.worldline.utp.dao.FareDaoObject

/**
 * @author a574891
 */
@RunWith(classOf[JUnitRunner])
class RuleFunctionsObjectSuite extends FunSuite with BeforeAndAfter with MockitoSugar {

  val zRecord = new ZRecord(ListMap("SellingMachineonIssue" -> "076016", "RunDate" -> "20150909", "IssuingWindow" -> "023848", "IssuingMachine" -> "076016", "RefundValue" -> " 0000000000.00", "MultipleIssueMarker" -> "000000", "CrossLondonMarker" -> "000000", "FareMethodMarker" -> "000444", "OriginRecordType" -> "000059 ", "NumberofTickets" -> "+00001", "NumberofSundryItems" -> "0000 ", "RetailTransactionID" -> "4168024636 ", "RetailItemID" -> "6449809170", "UNUSED" -> "            ", "DiscountPercentage" -> "005", "TicketStatus" -> "000001", "SellingLocation" -> "050483", "SortKey" -> "R99999946741680246366449809170000000000000000000000 0000000000", "IssuingLocation" -> "050483", "Route" -> "002545", "DiscountCode" -> "000000", "DeferredIssueIdentifier" -> "        ", "TransactionValue" -> " 0000000006.25", "PeriodofValidity" -> "+0030", "SundryTransactionNumber" -> "00000 ", "SeasonDateLastUsed" -> "00000000", "DateProcessed" -> "20150909010239591000", "NonIssueMarker" -> "000000", "AdvanceIssueMarker" -> "000000", "SystemGeneratedMarker" -> "000000", "ManualInputMarker" -> "000000", "AccountablePeriod" -> "201606", "DefaultAudit" -> "000000", "WarrantAccountHolder_loc_id_wah" -> "      ", "IssueCommissionProportion" -> "0.0000", "MatchingMarker" -> "000000", "PassengerCharterMarker" -> "Y", "SellingWindow" -> "023848", "OperatorID" -> "00010440", "Filler" -> " 0000000000.00", "PromotionCode" -> " 00", "BookingMarker" -> "000000", "RefundMarker" -> "000000", "EurostarFareBasis" -> "       ", "NumberofAdults" -> "+01", "BankGiroCreditSerial" -> "      ", "TicketNumber" -> "        +", "DateofTravelorSeasonStartDate" -> "20150901 ", "DateofSale" -> "00000000", "DateandTimeofIssue" -> "20150908182800000000", "ApportionmentMOPBasis" -> "000313", "Origin" -> "050483", "RecordType" -> "Z", "ApportionmentIndicator" -> "G", "Destination" -> "000008", "Product" -> "000320", "NumberofPeople" -> "+00001", "DeferredIssueType" -> "000307", "LostDays" -> "00", "TransactionNumber" -> "+81923 ", "SeasonTicketEndDate" -> "20150930", "NumberofChildren" -> " 00", "AccountingSundry" -> "000000000", "BarCodeInputMarker" -> "000000", "MinorMaxFareMarker" -> "000000", "RecreatedDataMarker" -> "000432", "Currency" -> "000300", "SaleNumber" -> "+10782", "CreditCardAcquirer" -> "000000", "FaresCheckingMarker" -> "Y"))
  val targetCurrency = "Euro"
  val curDbRecord: CurrencyDBRecord = new CurrencyDBRecord(math.BigDecimal(2.0), 10, new java.sql.Date(Utilities.parseDate("20151016010239281000".take(8), UtpConstants.UTP_DATE_FORMAT_ONE).getTime))
  val businessDAOService = BusinessDaoObject
  val currencyDAOService = CurrencyDaoObject
  val ruleFunctionsService = RuleFunctionsObject
  val referenceDataObject = ReferenceDataObject
  val fareDaoService = FareDaoObject
  val sqlCurrentDate = new Date(Calendar.getInstance.getTime().getTime())
  val mockDummyBusinessDBRecord = List(BusinessDBRecord(3, Some("Lennon"), Some(sqlCurrentDate), Some(1)))
  val mockCurrencyMap = scala.collection.mutable.Map[String, Int]("Sterling" -> 300, "Euro" -> 310)
  val mockFareObject: FareDBRecord = new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "A", "B", "1", 7.65, 7, new Timestamp(new DateTime(2015, 10, 29, 12, 5, 55, 0).getMillis()), new Timestamp(new DateTime(2015, 10, 29, 12, 5, 55, 0).getMillis()), Some("N"), Some(sqlCurrentDate), Some(sqlCurrentDate))
  val mockFareObjectList: List[FareDBRecord] = List(new FareDBRecord("ARMMAL", "PAX", "sellingLocation", "A", "B", "1", 7.65, 7, new Timestamp(new DateTime(2015, 10, 29, 12, 5, 55, 0).getMillis()), new Timestamp(new DateTime(2015, 10, 29, 12, 5, 55, 0).getMillis()), Some("N"), Some(sqlCurrentDate), Some(sqlCurrentDate)))
  before {

  }

  test("test fullFare with five pence rounding") {
    val fullFare: Double = 6.27d
    assertResult(6.25)(RuleFunctionsObject.roundNearestFivePence(fullFare))
  }

  test("test fullFare with five pence 35.6235 rounding") {
    val fullFare: Double = 35.6235
    assertResult(35.60)(RuleFunctionsObject.roundNearestFivePence(fullFare))
  }

  test("test fullFare with ten pence rounding") {
    val fullFare: Double = 6.27d
    assertResult(6.3)(RuleFunctionsObject.roundNearestTenPence(fullFare))
  }

  test("call the fare checking") {
    //assertResult(zRecord)(RuleRepository.CFMarker.checkFareCheckMarker(zRecord))
  }

  test("get Currency Conversion String") {

    when(businessDAOService.getBusiness()).thenReturn(mockDummyBusinessDBRecord)
    val lennonCurrency: Integer = 310
    Mockito.doReturn(mockCurrencyMap).when(currencyDAOService).getCurrencyCode()
    // when(currencyDAOService.getCurrencyCode()).thenReturn(mockCurrencyMap) 
    // when(UtpReferenceData.currencyCode(anyString())).thenReturn(300)    
    val currecny = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Currency).toInt
    assertResult("Euro")(RuleFunctionsObject.currencyConversionString(currecny, lennonCurrency, 300))
  }

  test("get the target Currency Conversion String") {
    Mockito.doReturn(mockCurrencyMap).when(currencyDAOService).getCurrencyCode()
    ruleFunctionsService.convertCurrencyProcess(zRecord, targetCurrency)
    //RuleFunctionsObject.convertCurrencyProcess(zRecord, targetCurrency)
    assertResult(" 0000000006.25")(zRecord.attribute.get(UtpConstants.ZRecord_TransactionValue).get)
  }

  test("Convert Sterling Currency to Euros currency value") {
    assertResult("%014.2f".format(125000000000.00))(RuleFunctionsObject.convertSterlingCurrencyTEuros(zRecord, curDbRecord))
  }

  test("Convert Sterling Currency to Sterling currency value") {
    assertResult("%014.2f".format(125000000000.00))(RuleFunctionsObject.convertSterlingCurrencyTEuros(zRecord, curDbRecord))
  }

  test("test calculatedChileFullFare functionality.") {
    val zRecDiscountPercent: Integer = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt
    val zRecTransactionValue: Double = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble
    val fullFare: Double = 6.55d
    //TODO Run this testcase alone time being
    assertResult(6.6)(RuleFunctionsObject.calculateFullChildFare(zRecDiscountPercent, zRecTransactionValue, fullFare))
  }

  test("test ln4315s_o_apply_fare_discount functionality.") {
    val fareValue: Double = 3750
    assertResult(3565.0)(RuleFunctionsObject.applyFareDiscount(fareValue, zRecord, ""))
  }

  test("test ln4315s_o_apply_fare_discount for Child Fare functionality.") {
    val fareValue: Double = 3750
    zRecord.attribute.put("DiscountPercentage", "051")
    assertResult(3675.0)(RuleFunctionsObject.applyFareDiscount(fareValue, zRecord, UtpConstants.CHILD_FARE))
  }

  test("test get Valid Fare") {

    var locGrpFboOutputCheck = new ListBuffer[FareDBRecord]
    locGrpFboOutputCheck.append(mockFareObject)
    assertResult(locGrpFboOutputCheck.toList)(RuleFunctionsObject.getValidFare(mockFareObject, new DateTime(2015, 10, 27, 0, 0), new DateTime(2015, 10, 30, 0, 0)))
  }

  test("test LN4317S_0_DERIVE_STATUS_CODE fuctionality ") {
    assertResult("001")(RuleFunctionsObject.deriveStatusCode("001"))
  }

  test("test calculate Discounted FareInPennies functionality") {
    var locGrpFboOutputCheck = new ListBuffer[FareDBRecord]
    locGrpFboOutputCheck.append(mockFareObject)
    assertResult(5)(RuleFunctionsObject.calculateDiscountedFareInPennies(mockFareObject, new DateTime(2015, 1, 2, 0, 0), new DateTime(2015, 9, 30, 0, 0), true, zRecord, UtpConstants.EMPTY_STRING))

  }

}