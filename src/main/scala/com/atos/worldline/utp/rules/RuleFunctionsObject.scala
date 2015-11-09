package com.atos.worldline.utp.rules

import scala.math.BigDecimal.double2bigDecimal
import scala.math.BigDecimal.int2bigDecimal
import scala.util.control.Breaks
import java.sql.Date
import java.util.Calendar
import java.sql.Timestamp
import org.joda.time.Days
import org.joda.time.Months
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.dao.CurrencyDaoObject
import com.atos.worldline.utp.dao.ProductDaoObject
import com.atos.worldline.utp.dao.RouteDaoObject
import com.atos.worldline.utp.dao.TicketStatusDaoObject
import com.atos.worldline.utp.dao.FareDaoObject
import com.atos.worldline.utp.dao.LocationDaoObject
import com.atos.worldline.utp.dao.CodeBookDaoObject
import com.atos.worldline.utp.dao.TicketStatusLinkDaoObject
import com.atos.worldline.utp.exceptions.UtpException
import com.atos.worldline.utp.record.CurrencyDBRecord
import com.atos.worldline.utp.record.CodeBookDBRecord
import com.atos.worldline.utp.record.FareDBRecord
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.utils.Utilities
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.ProductDBRecord

/**
 * This is scala class define the function do the processing UTP Records
 * @author a574891
 */
object RuleFunctionsObject {

  val ruleFunctions = RuleFunctionsObject
  val referenceData = ReferenceDataObject
  /**
   *  Get the target Currency Conversion String
   */
  def currencyConversionString(recordString: Int, lennon_currency: Integer, sterling_currency: Int): String = (recordString, lennon_currency, sterling_currency) match {
    case (recordString, lennon_currency, sterling_currency) if (recordString == lennon_currency) => UtpConstants.SPACES
    case (recordString, lennon_currency, sterling_currency) if (recordString == sterling_currency) => UtpConstants.EuroCurrency
    case _ => UtpConstants.SterlingCurrency

  }

  /**
   * Process the Currency conversion depending upon target currency
   */
  def convertCurrencyProcess(zRecord: ZRecord, targetCurrency: String) = {
    // get the Currency DB record from exchange details
    val currencyDbRecordList = CurrencyDaoObject.getCurrencyMaster()
    for (currencyDbRecord <- currencyDbRecordList if (isConversionDateSame(currencyDbRecord.currencyDate, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DateandTimeofIssue)))) yield currencyDbRecordList
    //Convert input currency value to target currency depending upon input targetCurrency 
    currencyConversion(zRecord, targetCurrency, currencyDbRecordList.apply(0))
  }

  /**
   * Verify the Conversion date and Zrecord Transaction date are same
   */
  def isConversionDateSame(currencyDate: java.sql.Date, transactionDate: String): Boolean = {
    val currencyDateDB = new org.joda.time.DateTime(currencyDate.getTime)
    var fmt: DateTimeFormatter = Utilities.getDateTimeFormatter(UtpConstants.UTP_DATE_FORMAT_ONE)
    val transactionDateTime = fmt.parseDateTime(transactionDate.take(8).toString())
    currencyDateDB.isEqual(transactionDateTime)
  }

  /**
   * Currency Conversion depending upon target Currency
   */
  def currencyConversion(zRecord: ZRecord, targetCurrency: String, currencyDbRecord: CurrencyDBRecord) = (zRecord, targetCurrency, currencyDbRecord) match {
    case (zRecord, targetCurrency, currencyDbRecord) if (targetCurrency.equalsIgnoreCase(UtpConstants.EuroCurrency))     => zRecord.attribute.put(UtpConstants.ZRecord_TransactionValue, convertSterlingCurrencyTEuros(zRecord: ZRecord, currencyDbRecord: CurrencyDBRecord).toString())
    case (zRecord, targetCurrency, currencyDbRecord) if (targetCurrency.equalsIgnoreCase(UtpConstants.SterlingCurrency)) => zRecord.attribute.put(UtpConstants.ZRecord_TransactionValue, convertEurosCurrencyToSterling(zRecord: ZRecord, currencyDbRecord: CurrencyDBRecord).toString())
  }

  /**
   * Get the discount type
   */
  def getDiscountType(discountCode: Int): Int = {
    referenceData.getProductType(discountCode)
  }

  /**
   *  Check Passenger Charter discount applicable to this record
   */
  def isPassengerCharterDiscountApplicable(passengerCharterMarker: String, discountType: Int): Boolean = {
    /** get the discount Type from z record */
    (UtpConstants.Y_CHAR.equalsIgnoreCase(passengerCharterMarker) &&
      discountType == referenceData.getCodeBookIdByValue(UtpConstants.PASSENGERS_CHARTER_DISCOUNT))
  }

  /**
   * get the passenger charter service
   */
  def getPassengerCharterFullFare(zRecord: ZRecord): Double = {

    /**
     * Get calculated full fare
     */
    //full_fare = (zRecord.ZRecord_TransactionValue/(100 - zRecord.ZRecord_DiscountPercentage)) * 100 
    val transactionValue = referenceData.getAbsolute(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
    val discountPercentage = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt

    var fullFare = referenceData.getFullFare(transactionValue, discountPercentage)

    //step 2.1: get code_book.cob_id for values "Adult Group" and "Child Group"    
    val adultCodeBookId = referenceData.getCodeBookIdByValue(UtpConstants.TICKET_STATUS_TYPE_ADULT_GROUP)
    val childCodeBookId = referenceData.getCodeBookIdByValue(UtpConstants.TICKET_STATUS_TYPE_CHILD_GROUP)

    /**
     * step 2.3: determine if the ticket type is Adult or Child by comparing
     * the ZRecord.ZRecord_TicketStatus code with the adultCodeBookId and
     * childCodeBookId retrieved above.
     */
    val ticketType = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TicketStatus).toInt match {
      case (ticketStatus) if (adultCodeBookId == ticketStatus) => UtpConstants.TICKET_TYPE_A
      case (ticketStatus) if (childCodeBookId == ticketStatus) => UtpConstants.TICKET_TYPE_C
    }

    /**
     * step 2.4: Round the calculated full fare to the nearest 5 or 10 pence
     * depending on the ticket type. If ticket type Child, round it to 5 else
     * round it to 10. Refer lines 22 to 50 of LN4306S_O_PASSENGERS_CHARTER
     */

    if (UtpConstants.TICKET_TYPE_C == ticketType) {
      // round it to nearest 5 pence for child ticket
      fullFare = roundNearestFivePence(fullFare)
    } // if (UtpConstants.TICKET_TYPE_C == ticketType)
    else {
      // round it to nearest 10 pence for adult ticket
      fullFare = roundNearestTenPence(fullFare)
    } // else (UtpConstants.TICKET_TYPE_C != ticketType)

    /**
     * step 2.5: For a child ticket, add 5 pence to the calculated full fare
     * and then calculate the discounted fare based on ZRecord.ZRecord_Discount
     * _Percentage. Then check if this discounted fare calculated matches
     * ZRecord.ZRecord_TransactionValue. If it does not match, then run this
     * loop again till the calculated discounted fare matches ZRecord.ZRecord_
     * TransactionValue.
     * Note: This has to be done only if the ticket type is "C"
     */
    if (UtpConstants.TICKET_TYPE_C == ticketType) {
      val ZRecDiscountPercent: Integer = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt
      val ZRecTransactionValue: Double = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble
      fullFare = calculateFullChildFare(ZRecDiscountPercent, ZRecTransactionValue, fullFare)
    } //if (UtpConstants.TICKET_TYPE_C == ticketType)

    /**
     * step 2.6: If the ZRecord.ZRecord_DiscountPercentage > 50 then full fare
     * is divided by 2 assuming that the percentage will be more than 50 only
     * for child ticket type.
     */
    if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt > 50) fullFare = fullFare / 2

    fullFare
  }

  /**
   * Get the Non passenger charter service
   */
  def determineNonPCDiscount(zRecord: ZRecord): (Boolean, Boolean) = {

    val defaultDiscountProductList = referenceData.getDefaultDiscountProductList()
    var discount = true
    var fareCheck = false
    val loop = new Breaks
    for (defaultDiscountProduct <- defaultDiscountProductList) {
      if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountCode).toInt == defaultDiscountProduct.prodId) {
        discount = false
        if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_FaresCheckingMarker) == UtpConstants.N_CHAR)
          fareCheck = checkDiscountStatus(referenceData.getCodeBookIdByValue(UtpConstants.TICKET_STATUS_TYPE_DISCOUNT_TICKET_STATUS_GROUP), Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TicketStatus).toInt)
        loop.break()
      } //if ZRecord_DiscountCode == defaultDiscountProduct.product_id
    } //for each defaultDiscountProduct
    (fareCheck, discount)
  } //determineNonPCDiscount

  /**
   * Check fares as per LN4324S_O_CHECK_FARES
   */
  def checkFares(zRecord: ZRecord, fullFare: Double, nonPCDiscount: Boolean): (Int, Double) = {
    var fcResult = 0
    var fcFullFare = 0.0
    var potentialFCExclusionFare = 0.0 //to store transaction value
    var fareInPennies: Int = 0 //to store transaction value in pennies/cents
    var testFareInPennies: Int = 0 //to store transaction value in pennies/cents
    var mainFareInPennies: Int = 0 //to store main fare value in pennies/cents
    var locTravelDate: DateTime = null
    var locIssueDate: DateTime = null
    var discountedFare: Double = 0.0
    val sqlCurrentDate = new Date(Calendar.getInstance.getTime().getTime())
    val dateFormatter: DateTimeFormatter = Utilities.getDateTimeFormatter(UtpConstants.UTP_DATE_FORMAT_TWO)
    val dateFormatterTwo: DateTimeFormatter = Utilities.getDateTimeFormatter(UtpConstants.UTP_DATE_FORMAT_TWO)

    //TODO: Implement as per LN4324S_O_CHECK_FARES
    if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble >= 0) {
      potentialFCExclusionFare = fullFare
      fareInPennies = (fullFare * 100).toInt
    } //if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble >= 0)
    else {
      /**
       * converting negative transaction value to positive
       */
      potentialFCExclusionFare = -fullFare
      fareInPennies = (fullFare * -100).toInt
    } //else (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble < 0)

    testFareInPennies = (fullFare * 100).toInt
    locTravelDate = dateFormatter.parseDateTime(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DateofTravelorSeasonStartDate).take(8).toString())

    /**
     * if Ticket is a TOD then take date of sale else take date of issue
     */
    locIssueDate = if (isTODTicket(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DeferredIssueType).toInt))
      dateFormatter.parseDateTime(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DateofSale).take(8).toString())
    else
      dateFormatter.parseDateTime(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DateandTimeofIssue).take(8).toString())

    val originLocation = referenceData.getLocationById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Origin).toInt)
    val destinationLocation = referenceData.getLocationById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Destination).toInt)
    val sellingLocation = referenceData.getLocationById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_SellingLocation).toInt)
    val route = RouteDaoObject.getRouteById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Route).toInt)
    val ticketStatus = referenceData.getTicketStatusById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TicketStatus).toInt)
    val product = ProductDaoObject.getProductById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Product).toInt)
    val issuingLocation = referenceData.getLocationById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Origin).toInt)

    /**
     * fare checking exclusion
     */
    //fcResult = ifExcludeFare(locIssueDate, new FareDBRecord(issuingLocation.location.get, originLocation.location.get, destinationLocation.location.get, sellingLocation.location.get, route.routeCode.get, product.productCode.get, ticketStatus.ticketStatusCode.get, potentialFCExclusionFare))
    fcResult = ifExcludeFare(locIssueDate, new FareDBRecord(Some(issuingLocation.location.get), Some(originLocation.location.get), Some(destinationLocation.location.get), Some(sellingLocation.location.get), Some(route.routeCode.get), Some(product.productCode.get), Some(ticketStatus.ticketStatusCode.get), Some(potentialFCExclusionFare)))

    /**
     * if record is excluded from fare checking do nothing and return values
     * calculated upto now
     */
    if (fcResult == referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_EXCLUSION)) return (fcResult, fcFullFare)

    /**
     * Advance fare checks to be done only if Advance Issue Marker contains
     * matching value of code_book Advance Issue
     */
    if (referenceData.getCodeBookIdByValue(UtpConstants.ADVANCE_ISSUE) == Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_AdvanceIssueMarker).toInt) {
      //originLocation: String, destinationLocation: String, sellingLocation: String, routeCode: String, productCode: String, ticketStatusCode: String, fare: Double=6.65, nullFare: Double=0.0, value: Int=0, withEffectFrom: DateTime=new DateTime(2015,1,1,0,0), withEffectUntil: DateTime=new DateTime(2015,12,31,23,59)
      locTravelDate = advanceFCCheck(locIssueDate, new FareDBRecord(Some(issuingLocation.location.get), Some(originLocation.location.get), Some(destinationLocation.location.get), Some(sellingLocation.location.get), Some(route.routeCode.get), Some(product.productCode.get), Some(ticketStatus.ticketStatusCode.get), Some(potentialFCExclusionFare), fareInPennies))
    } //if code_book.Advance Issue = zRecord.Advance Issue Marker

    /**
     * Fare checking calculating main fare in pennies
     */

    mainFareInPennies = calculateDiscountedFareInPennies(new FareDBRecord(Some(issuingLocation.location.get), Some(originLocation.location.get), Some(destinationLocation.location.get), Some(sellingLocation.location.get),
      Some(route.routeCode.get), Some(product.productCode.get), Some(ticketStatus.ticketStatusCode.get)), locIssueDate, locTravelDate, nonPCDiscount, zRecord, UtpConstants.SPACES)

    /**
     * Next check for the number of adults and child in the record
     */
    val adultCount = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_NumberofAdults).toInt
    val childCount = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_NumberofChildren).toInt
    var calTotalFare: Int = 0

    if ((adultCount == 1 && childCount == 0) || (adultCount == 0 && childCount == 1))
      calTotalFare = mainFareInPennies
    else if (adultCount > 1 && childCount == 0)
      calTotalFare = mainFareInPennies * adultCount
    else if (adultCount == 0 && childCount > 1)
      calTotalFare = mainFareInPennies * childCount
    else if (adultCount > 0 && childCount > 0) {
      val childTicketStatus = deriveStatusCode(ticketStatus.ticketStatusCode.get)
      val childFareInPennies = calculateDiscountedFareInPennies(
        new FareDBRecord(Some(issuingLocation.location.get), Some(originLocation.location.get), Some(destinationLocation.location.get),
          Some(sellingLocation.location.get), Some(route.routeCode.get), Some(product.productCode.get), Some(childTicketStatus)), locIssueDate, locTravelDate, nonPCDiscount, zRecord, childTicketStatus)
      calTotalFare = mainFareInPennies * adultCount + childFareInPennies * childCount
    } else {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_INCALCULABLE)
      return (fcResult, fcFullFare)
    }

    /**
     * TODO: maxNonSeasonFare and maxSeasonFare present in application_parameters table
     *    to be extracted using DAO
     */

    val maxNonSeasonFare: Int = referenceData.getApplicationParameterValue(UtpConstants.MAXIMUM_NON_SEASON_FARE) //10
    val maxSeasonFare: Int = referenceData.getApplicationParameterValue(UtpConstants.MAXIMUM_SEASON_FARE) //20
    val supendableIndicator: Int = getSuspendableIndicatorForProduct(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_Product))
    /**
     * Non season ticket, check for Non Season Maximum Fare
     */
    if ((supendableIndicator == referenceData.getCodeBookIdByValue(UtpConstants.NON_SUSPENDABLE_SEASON_TICKET)
      || supendableIndicator == referenceData.getCodeBookIdByValue(UtpConstants.NONE)) &&
      (maxNonSeasonFare > 0 && calTotalFare > maxNonSeasonFare)) {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_EXCESSIVE_FARE)
      return (fcResult, fcFullFare)
    } //if Non-Season ticket and fare is greater than maximum non season fare
    /**
       * Season Ticket, check for Season Maximum Fare
       */ else if (maxSeasonFare > 0 && calTotalFare > maxSeasonFare) {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_EXCESSIVE_FARE)
      return (fcResult, fcFullFare)
    } //else if season ticket and fare is greater than maximum season fare

    /**
     * check whether the fare for non season ticket is correct at this
     * point.
     */
    fcFullFare = returnFullFare((calTotalFare / 100).toDouble, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
    /**
     * if the ticket is a non-season ticket check if the calculated fare is
     * CORRECT, OVERCHARGE or UNDERCHARGE and return
     */
    if (supendableIndicator == referenceData.getCodeBookIdByValue(UtpConstants.NON_SUSPENDABLE_SEASON_TICKET)
      || supendableIndicator == referenceData.getCodeBookIdByValue(UtpConstants.NONE)) {
      fcResult = determineFareCheckResult(calTotalFare, testFareInPennies)
      return (fcResult, fcFullFare)
    } //if suspendableIndicator == NON_SUSPENDABLE_SEASON_TICKET || NONE

    val recordTravelDate = dateFormatter.parseDateTime(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DateofTravelorSeasonStartDate))
    val seasonTicketEndDate = dateFormatterTwo.parseDateTime(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_SeasonTicketEndDate))
    /**
     * Determine travel date > in season ticket end date excluding any lost
     * days.
     */
    if (recordTravelDate.isAfter(seasonTicketEndDate)) {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_INCALCULABLE)
      return (fcResult, fcFullFare)
    }

    val seasonEndDate = seasonTicketEndDate.minusDays(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_LostDays).toInt)
    /**
     * if season ends before travel starts, result is insensible hence
     * INCALCULABLE should be returned
     */
    val diffDays: Int = Days.daysBetween(seasonEndDate, locTravelDate).getDays
    val diffMonth: Int = Months.monthsBetween(seasonEndDate, locTravelDate).getMonths

    if (diffDays < 0 || diffMonth < 1) {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_INCALCULABLE)
      return (fcResult, fcFullFare)
    } //if daysBetween < 0 or monthsBetween < 0

    /**Determine the factor by which the season ticket was adjusted by ln4327s_o_return_season_factor (diff_month, diff_days)*/
    val locSeasonAdjustmentFactor = getSeasonFactor(diffMonth, diffDays)
    if (locSeasonAdjustmentFactor > 40.00) {

      /**
       * check whether the fare is correct
       */
      if (calTotalFare == testFareInPennies || calTotalFare == -testFareInPennies) {
        //fcResult = correct
        fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_CORRECT)
        fcFullFare = (calTotalFare / 100).toDouble
        //call method implementing ln4329s_o_return_full_fare(fcFullFare,zRecord) method returns fcFullFare
        fcFullFare = returnFullFare(fcFullFare, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
        return (fcResult, fcFullFare)
      }
      //else Set fare is undercharge
      //fcResult = undercharge
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_UNDERCHARGE)
      fcFullFare = (calTotalFare / 100).toDouble
      //call method implementing ln4329s_o_return_full_fare(fcFullFare, zRecord) method returns fcFullFare
      fcFullFare = returnFullFare(fcFullFare, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
      return (fcResult, fcFullFare)
    }

    var locAdjSeasonFare: Double = calTotalFare * (locSeasonAdjustmentFactor / 40)
    val ticketTypeCodeBook: CodeBookDBRecord = referenceData.getCodeBookById(ticketStatus.tisId)
    var roundingFactor: Int = 0

    /**  Determine the rounding factor upon Ticket Status */
    if (ticketTypeCodeBook.value == UtpConstants.TICKET_STATUS_TYPE_ADULT_GROUP) {
      roundingFactor = 10
    } else {
      roundingFactor = 5
    }

    /** Determine the rounding required for locAdjSeasonFare */
    if ((locAdjSeasonFare % roundingFactor) != 0) {
      if (ticketTypeCodeBook.value == UtpConstants.TICKET_STATUS_TYPE_ADULT_GROUP) {
        locAdjSeasonFare = roundNearestTenPence(locAdjSeasonFare)
      } else {
        locAdjSeasonFare = roundNearestFivePence(locAdjSeasonFare)
      }
    }

    //return full fare to match the transaction value present in the record
    fcFullFare = returnFullFare(locAdjSeasonFare / 100, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)

    /**
     * returns appropriate fare checking result: CORRECT, OVERCHARGE or
     * UNDERCHARGE
     */
    fcResult = determineFareCheckResult((locAdjSeasonFare * 100).toInt, testFareInPennies)

    return (fcResult, fcFullFare)
  } //checkFares

  /**
   * This method will determine if the calculated fare is CORRECT, OVERCHARGE or
   * UNDERCHARGE
   */
  def determineFareCheckResult(testFare: Int, testWithFare: Int): Int = {
    val refData = ReferenceDataObject
    var fcResult: Int = refData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_UNDERCHARGE)

    if (testFare == testWithFare || testFare == -testWithFare) {
      fcResult = refData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_CORRECT)
    } //if (testFare == testWithFare || testFare == -testWithFare)
    if (testFare < testWithFare || testFare < -testWithFare) {
      fcResult = refData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_OVERCHARGE)
    } //if (testFare < testWithFare || testFare < -testWithFare) 

    fcResult
  } //determineFareCheckResult

  /**
   * Check if issued ticket is a TOD (Ticket on Departure)
   */
  def isTODTicket(ticketIssueType: Int): Boolean = {
    ticketIssueType == referenceData.getCodeBookIdByValue(UtpConstants.ISSUE_OF_TICKET)
  } //isTODTicket

  /**
   * Convert Sterling Currency to Euros currency value
   */
  def convertSterlingCurrencyTEuros(record: ZRecord, curDbRecord: CurrencyDBRecord): String = {
    // zero-filled with round (12,2) decimal
    var transcationValue = "%014.2f".format(Utilities.getZRecordAttrValue(record, UtpConstants.ZRecord_TransactionValue).toDouble)
    var currencyRate = Utilities.getInteger(curDbRecord.currencyRate.toDouble)
    var conversionPower = curDbRecord.conversionPower.toDouble
    val returnTranscationValue = transcationValue.toDouble * (currencyRate * scala.math.pow(10, conversionPower))
    "%014.2f".format(returnTranscationValue.toDouble)
  }

  /**
   * Convert Sterling Currency to Euros currency value
   */
  def convertEurosCurrencyToSterling(record: ZRecord, curDbRecord: CurrencyDBRecord): String = {
    // zero-filled with round (12,2) decimal
    var transcationValue = "%014.2f".format(Utilities.getZRecordAttrValue(record, UtpConstants.ZRecord_TransactionValue).toDouble)
    var currencyRate = Utilities.getInteger(curDbRecord.currencyRate.toDouble);
    var conversionPower = curDbRecord.conversionPower.toDouble
    val returnTranscationValue = transcationValue.toDouble / (currencyRate * scala.math.pow(10, conversionPower))
    "%014.2f".format(returnTranscationValue.toDouble)
  }

  /**
   * Passenger Charter marker enabled
   */
  def isPassengerCharterMarkerEnabled(passengerCharterMarker: String): Boolean = UtpConstants.Y_CHAR.equalsIgnoreCase(passengerCharterMarker)

  /**
   * isFare checking Marker enable checkFares
   */
  def isFareCheckMarkerEnabled(faresCheckingMarker: String): Boolean = UtpConstants.Y_CHAR.equalsIgnoreCase(faresCheckingMarker)

  /**
   * LN8425S_R_TEST_TIS_GROUP_MBRSHIP: This method determines if the issued
   * ticket belongs to any TIS group
   */
  def checkDiscountStatus(tisType: Int, ticketStatus: Int): Boolean = {
    ticketStatus == tisType
  } //checkDiscountStatus

  /**
   * step 2.5: For a child ticket, add 5 pence to the calculated full fare
   * and then calculate the discounted fare based on ZRecord.ZRecord_Discount
   * _Percentage. Then check if this discounted fare calculated matches
   * ZRecord.ZRecord_TransactionValue. If it does not match, then run this
   * loop again till the calculated discounted fare matches ZRecord.ZRecord_
   * TransactionValue.
   * Note: This has to be done only if the ticket type is "C"
   */
  def calculateFullChildFare(zRecDiscountPercent: Integer, zRecTransactionValue: Double, fullFare: Double): Double = {
    var childFullFare: Double = 0
    childFullFare = fullFare + UtpConstants.FIVE_PENCE_OR_CENTS
    var childDiscFare: Double = calculateDiscountedFareValue(childFullFare, zRecDiscountPercent)
    //round it to nearest 5 pence
    childDiscFare = roundNearestFivePence(childDiscFare)
    //if this discounted fare calculated matches ZRecord.ZRecord_TransactionValue
    if (childDiscFare != zRecTransactionValue)
      childFullFare = calculateFullChildFare(zRecDiscountPercent, zRecTransactionValue, childFullFare)
    childFullFare
  } //calculateFullChildFare

  /**
   * calculate the basic discounted fare in pounds to 4 dp  [ fare * (100 - discount %) / 100 ]
   */
  def calculateDiscountedFareValue(fareValue: Double, discountPercentage: Integer): Double = {
    var discFareValue: Double = (fareValue * (100 - discountPercentage)) / 100
    discFareValue
  }

  /**
   * round it to nearest 5 pence
   */
  def roundNearestFivePence(fullFarePence: Double): Double = {
    ((((fullFarePence: Double) * (20: BigDecimal)) + (0.5: BigDecimal)).toInt / ((20: BigDecimal): BigDecimal)).toDouble
  }

  /**
   * round it to nearest 10 pence
   */
  def roundNearestTenPence(fullFarePence: Double): Double = {
    ((((fullFarePence: Double) * (10: BigDecimal)) + (0.5: BigDecimal)).toInt / ((10: BigDecimal): BigDecimal)).toDouble
  }

  /**
   * Implementation of the LN4350S_V_IF_EXCLUDE_FARE
   *  This action block is to determine whether the fare is to
   *  be excluded from fares checking.  If the fare is excluded
   *  then the out cob_id is set to the exlcuded code passed in.
   *  Otherwise the out cob_id is set to zero, indicating that
   *  this fare should be checked.
   */
  def ifExcludeFare(locIssueDate: DateTime, fcExclusions: FareDBRecord): Int =
    {
      if (referenceData.isFareCheckExclusion(new Date(locIssueDate.getMillis()), fcExclusions)) {
        return referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_EXCLUSION)
      } else {
        return 0
      }
      //where value = 'Exclusion'
    }

  /**
   * Implementation of the LN4315S_O_APPLY_FARE_DISCOUNT
   */
  def applyFareDiscount(fareValue: Double, zRecord: ZRecord, fareType: String): Int = {
    var discountedFareValue: Double = 0.0d
    //Convert the imported full fare from pence to pounds
    var locWorkFare2dp: Double = fareValue / 100
    /* If this is a child fare from an APTIS machine (discount over 50%) then
     * double the imported fare as the discount should be applied to the
     * adult fare rather than the child fare which will have been returned.
     */
    var zRecDiscountPercent = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt
    if (zRecDiscountPercent > 50 && UtpConstants.CHILD_FARE.equalsIgnoreCase(fareType)) {
      locWorkFare2dp = locWorkFare2dp * 2
    }
    //Calculate the basic discounted fare in pounds to 4 dp  [ full fare * (100 - discount %) / 100 ]    
    discountedFareValue = calculateDiscountedFareValue(locWorkFare2dp, zRecDiscountPercent)
    /**
     * ********************************
     * Round up to the nearest penny
     * (e.g. 35.6235 becomes 35.63).
     */
    //round it to nearest 5 pence
    discountedFareValue = roundNearestFivePence(discountedFareValue)

    //Convert the result from pounds back to pence for export.
    (discountedFareValue * 100).toInt
  }

  /**
   * Implementation of the LN4308S_O_GET_VALID_FARE
   */
  def getValidFare(fareObjIn: FareDBRecord, locIssueDate: DateTime, locTravelDate: DateTime): List[FareDBRecord] = {
    val EXITSTATE = "processing_ok"
    var outputFboList = new ListBuffer[FareDBRecord]

    if (EXITSTATE.equalsIgnoreCase("processing_ok")) {
      if (fareObjIn.ticketStatus.getOrElse("0").toInt > ("000").toInt) {
        /** TODO READ replacement ticket_status **/
        val ticket_status = fareObjIn.ticketStatus

        /***** Construct FBO query ***/

        /**** Recycle results ****/
        val locGrpFboOutput: List[FareDBRecord] = FareDaoObject.getFares()
        locGrpFboOutput.foreach { fareObj =>
          if ((fareObj.product).toString().equalsIgnoreCase((fareObjIn.product).toString())) {
            outputFboList.append(fareObj)
          }
        }
        outputFboList
      }
    }
    outputFboList.toList
  }

  /**
   * Implementation of the LN4057S_O_HERITAGE_ADV_FARE_CHECK
   */
  def advanceFCCheck(issueDate: DateTime, fcFareObj: FareDBRecord): DateTime = {
    var checkAheadDays: Int = referenceData.getApplicationParameterValue(UtpConstants.UTP_FARES_CHECK_AHEAD_DAYS) //This value is to be retrieved from application_parameters table
    var travelDate: DateTime = issueDate
    var endDate: DateTime = if (checkAheadDays == 0) issueDate.plusDays(366) else issueDate.plusDays(checkAheadDays)
    var lowestFareDate: DateTime = null
    var lowestFareValue: Int = 0

    travelDate = travelDate.plusDays(1) //Next day of travel date
    var fboOutputList = getValidFare(fcFareObj, travelDate, issueDate)

    if (fboOutputList.isEmpty) travelDate

    for (fareObj <- fboOutputList) {
      if (!travelDate.isBefore(fareObj.withEffectFrom.getTime)
        && (fareObj.withEffectUntil == null
          || (travelDate.isBefore(fareObj.withEffectUntil.getTime)
            || travelDate.isEqual(fareObj.withEffectUntil.getTime)))) {
        if (fareObj.value <= fcFareObj.value) {
          //Next day of travel date matches fare charged or is an overcharge
          travelDate
        } //if (fareObj.value <= fcFareObj.value)
        else {
          lowestFareDate = travelDate
          lowestFareValue = fareObj.value
          if (fareObj.withEffectUntil != null
            && endDate.isBefore(fareObj.withEffectUntil.getTime)) {
            travelDate
          } //if fareObj.withEffectUntil >= endDate

          if (fareObj.withEffectUntil == null) {
            travelDate = endDate
          } else {
            travelDate = new DateTime(fareObj.withEffectUntil)
          }
        } //else (fareObj.value > fcFareObj.value)
      } //if withEffectFrom <= travelDate <= withEffectUntil
    } //for (fareObj <- fboOutputList)

    //if no match is found in the whole list exit with exception message "No valid fares found"
    while (travelDate.isBefore(endDate)) {
      travelDate = travelDate.plusDays(1)
      fboOutputList = getValidFare(fcFareObj, travelDate, issueDate)
      //No fares found
      if (fboOutputList.isEmpty) lowestFareDate
      for (fareObj <- fboOutputList) {
        if ((travelDate.isAfter(fareObj.withEffectUntil.getTime)
          || travelDate.isEqual(fareObj.withEffectUntil.getTime))
          && (fareObj.withEffectUntil != null
            && travelDate.isBefore(fareObj.withEffectUntil.getTime))) {
          if (fareObj.value <= fcFareObj.value) {
            //Next day of travel date matches fare charged or is an overcharge
            travelDate
          } //if (fareObj.value <= fcFareObj.value)
          else {
            if (fareObj.value < lowestFareValue) {
              lowestFareValue = fareObj.value
              lowestFareDate = travelDate
            } //if (fareObj.value < lowestFareValue)
            travelDate = new DateTime(fareObj.withEffectUntil)
          } //else (fareObj.value > fcFareObj.value)
        } //if withEffectFrom <= travelDate <= withEffectUntil
      } //for (fareObj <- fboOutputList)
    } //while (travelDate.isBefore(endDate))

    //if no match is found in the whole list exit with exception message "No valid fares found"

    travelDate
  } //def advanceFCCheck

  /**
   * Implementation of the LN4329S_O_RETURN_FULL_FARE
   */
  def returnFullFare(fare: Double, transactionValue: Double): Double = {
    if (transactionValue >= 0)
      fare
    else -fare
  }

  /**
   * Implementation of method to get suspendable indicator for product
   */
  def getSuspendableIndicatorForProduct(product: String): Int = {
    //val siCobId = 0
    var productRecord = ProductDaoObject.getProductById(product.toInt)

    productRecord.cobIdSuspendableInd
  }

  /**
   * Implementation of the IN4317S_O_DERIVE_STATUS_CODE
   */
  def deriveStatusCode(ticketStatusCode: String): String = {
    var childStatusCode: String = UtpConstants.SPACES
    val parentTicketStatus = referenceData.getTicketStatusByCode(ticketStatusCode)
    val parentTicketStatusLink = referenceData.getTicketStatusLinkByLinkedById(parentTicketStatus.tisId)
    val ticketStatusLinkCodeBook = referenceData.getCodeBookById(parentTicketStatusLink.tslId)
    val ticketStatusLinkerCodeBookId = referenceData.getCodeBookIdByValue(UtpConstants.TICKET_STATUS_TYPE_ADULT_CHILD_AAA_LINKER)
    /**
     * if ticket status link retrieved for parent ticket status matches the
     * linker code_book entry, get the child ticket status and compare it with
     * the code_book child ticket status group id before returning to calling
     * method.
     */
    if (ticketStatusLinkerCodeBookId == ticketStatusLinkCodeBook.cobId) {
      val childTicketStatus = referenceData.getTicketStatusById(parentTicketStatusLink.tisIdLinks)
      val childTicketStatusCodeBook = referenceData.getCodeBookById(childTicketStatus.tisId)
      val childTicketStatusCodeBookId = referenceData.getCodeBookIdByValue(UtpConstants.TICKET_STATUS_TYPE_CHILD_GROUP)
      /**
       * If ticket status of child ticket status object matches the Child Ticket
       * Status Group in code book, return the child ticket status group's code
       */
      if (childTicketStatusCodeBookId == childTicketStatusCodeBook.cobId)
        childStatusCode = childTicketStatus.ticketStatusCode.get
    } //if (ticketStatusLinker == codeBook.cobId)
    childStatusCode
  }

  /**
   * Implementation of the LN4308S_O_GET_VALID_FARE  : Fare checking: get list of all valid fares and return calculated discounted Fare in pennies
   */
  def calculateDiscountedFareInPennies(fareObjIn: FareDBRecord, locIssueDate: DateTime, locTravelDate: DateTime, isNonPCDiscount: Boolean, zRecord: ZRecord, fareType: String): Int = {
    val EXITSTATE = "processing_ok"
    var fcResult = 0
    var fcFullFare = 0.0
    var mainFareInPennies: Int = 0 //to store main fare value in pennies/cents

    val fboOutputList: List[FareDBRecord] = getValidFare(fareObjIn, locIssueDate, locTravelDate)

    if (fboOutputList.isEmpty) {
      fcResult = referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_INCALCULABLE)
      (fcResult, fcFullFare)
    } //if (fboOutputList.isEmpty)

    /**
     * List may contain several fares covering different effective periods
     * Set Group subscript to fare effective for travel or lowest date
     */
    for (fareObj <- fboOutputList) {
      if (locTravelDate.isAfter(fareObj.withEffectFrom.getTime)
        && (fareObj.withEffectUntil == null
          || (locTravelDate.isBefore(fareObj.withEffectFrom.getTime)
            || locTravelDate.isEqual(fareObj.withEffectFrom.getTime)))) {
        mainFareInPennies = fareObj.value
      } //if fareObj.withEffectFrom <= locTravelDate <= fareObj.withEffectUntil
    } //for (fareObj <- fboOutputList)

    /**
     * Apply a Non PC discount to the full fare retrieved if applicable
     */
    if (isNonPCDiscount) {
      mainFareInPennies = applyFareDiscount(mainFareInPennies, zRecord, fareType)
    } //if (isNonPCDiscount)
    mainFareInPennies
  } //calculateDiscountedFareInPennies

  /**
   * Implementation of the LN4327S_O_RETURN_SEASON_FACTOR : Now we can determine the factor by which
   * the season ticket was adjusted by.
   * locSeasonAdjustmentFactor =  ln4327s_o_return_season_factor(diff_month, diff_days)
   *
   */
  /**
   * *********************************************************
   * We now apply the formula:
   * f=3.84m+(0.64(rounddown(abs(d/5),0))+(0.13(mod(d/5))
   * Where f is the Factor, d is the number of days and
   * m is the number of months.
   */
  /**
   * *********************************************************
   *
   */
  def getSeasonFactor(diffMonth: Int, diffDays: Int): Double = {
    (3.84d * diffMonth).toDouble + (0.64d * (diffDays / 5).toDouble) + (0.13d * (diffDays % 5).toDouble)
  }

  /**
   * Penalty check before processing to find additional_records flag in case of Fare checking undercharge
   * if fcResult = UNDERCHARGE
   */
  def checkForAdditionalRecords(zRecord: ZRecord, fcResult: Int, fullFare: Double): (Int, Double, Boolean, Int, ProductDBRecord) = {
    var fcResultRec: Int = fcResult
    var fcFullFareRec: Double = 0.0
    var locFcAdditionalRecordsRec: Boolean = false
    var penalisedBusId: Int = 0
    var correctionTypecobId: Int = 0
    var correctingProduct: ProductDBRecord = null

    if (fcResult == referenceData.getCodeBookIdByValue(UtpConstants.FARE_CHECKING_UNDERCHARGE)) {
      locFcAdditionalRecordsRec = true
      /**Cater for the freak chance of an Issue / Non-Issued zero fare. */
      if ((Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_NonIssueMarker).toDouble != 0)
        && (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble == 0)) {
        fcFullFareRec = -fullFare
      } //if NonIssueMarker != 0 and TransactionValue == 0

      // Determine correct business to be penalised
      penalisedBusId = getCorrectBusinessToBePenalised(zRecord)
      correctionTypecobId = referenceData.getCodeBookIdByValue(UtpConstants.DEFAULT_FARES_CHECKING_PENALTY)
      
      //Ashay this value comes as null since the if block is not getting executed. check on Monday if this is the Mismatch issue
      correctingProduct = getCorrectProductForCurrentBussiness(penalisedBusId, correctionTypecobId)
    }
    val dummyProductDBRecord = new ProductDBRecord(0, Some("A"), Some("B"), Some(math.BigDecimal(2.0)),Some(0), Some("C"), Some("D"), Some(0), Some("E"),Some("F"), Some("G"), Some(new java.sql.Date(Utilities.parseDate("20151016010239281000".take(8), UtpConstants.UTP_DATE_FORMAT_ONE).getTime)), Some(new java.sql.Date(Utilities.parseDate("20151017010239281000".take(8), UtpConstants.UTP_DATE_FORMAT_ONE).getTime)), Some(0),Some("H"),0, 0, 0, 0,0, 0, 0)
    (fcResultRec, fcFullFareRec, locFcAdditionalRecordsRec, penalisedBusId, if(correctingProduct != null) correctingProduct else dummyProductDBRecord)
  }

  /**
   * Implementation of the ln4320s_r_identify_penalty_pro: Determine correct business to be penalised. Using Selling Business to processing.
   */
  def getCorrectBusinessToBePenalised(zRecord: ZRecord): Int = {
    val sellingLocationId = referenceData.getLocationById(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_SellingLocation).toInt).locationId
    val testPeriod: Int = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_AccountablePeriod).toInt
    val currentPeriod: Int = referenceData.getCurrentPeriodByDate()
    val penalisedBusId: Int = ruleFunctions.findCorrectBusinessLevel(currentPeriod, testPeriod, sellingLocationId)

    penalisedBusId
  }

  /**
   * Implementation of the ln4320s_r_identify_penalty_pro: Look for correcting product for current business
   */
  def getCorrectProductForCurrentBussiness(penalisedBusId: Int, correctionTypecobId: Int): ProductDBRecord = {
    referenceData.getCorrectProductForCurrentBussiness(penalisedBusId, correctionTypecobId)
  }

  /**
   * Implementation of  LN4320S_R_IDENTIFY_PENALTY_PRO :
   * Revised to find correct business level - that is, lowest level of
   * business from ISD upwards that has an associated correcting product
   */

  def findCorrectBusinessLevel(currentPeriod: Int, testPeriod: Int, sellingLocationId: Int): Int = {

    var currentBusId: Int = 0
    if (currentPeriod == testPeriod) {
      currentBusId = referenceData.getBusinessIdBySellingLocation(UtpConstants.GROUP_FUNCTION_TYPE_OWNS)
    } else {
      currentBusId = referenceData.getBusinessIdBySellingLocation(UtpConstants.GROUP_FUNCTION_TYPE_PREVIOUSLY_OWNED)
    }
    currentBusId
  }

}

/**
 * This is scala object define the function do the processing UTP Records
 *
 */
/*object RuleFunctionsObject {
  def apply(): RuleFunctions = {
    return new RuleFunctions();
  }
}*/