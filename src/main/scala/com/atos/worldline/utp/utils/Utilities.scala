package com.atos.worldline.utp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import scala.BigDecimal
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.constants.RecordAttribute
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * This Utilities scala object provide the common utility function used into UTP file processing
 * @author a574891
 */
object Utilities {

  /**
   * Retrieve the requested attribute value from ZRecord map
   */
  def getZRecordAttrValue(r: ZRecord, paraname: String): String = {
    if (r.attribute.apply(paraname) != null) { r.attribute.apply(paraname).trim() } else { UtpConstants.EMPTY_STRING }
  }

  /**
   * Set the passed value to the requested attribute in the ZRecord map
   */
  def setZRecordAttrValue(r: ZRecord, paraname: String, newValue: String): Unit = {
    r.attribute += ((paraname, newValue))
  }

  //TODO need to implement
  /**
   * Convert ZRecord object to ZRecord Line String
   */
  def getZRecordLine(r: ZRecord, recordLine: StringBuilder): StringBuilder = {

    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SortKey))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RecordType))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_ApportionmentIndicator))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_IssuingLocation))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SellingLocation))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Origin))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Destination))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Route))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_TicketStatus))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_ApportionmentMOPBasis))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Product))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DiscountCode))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DiscountPercentage))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DateandTimeofIssue))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NumberofPeople))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DeferredIssueIdentifier))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_UNUSED))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DateofSale))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DeferredIssueType))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_TransactionValue))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RetailItemID))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DateofTravelorSeasonStartDate))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_LostDays))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_PeriodofValidity))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RetailTransactionID))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_TicketNumber))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_TransactionNumber))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SundryTransactionNumber))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NumberofSundryItems))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_BankGiroCreditSerial))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SeasonTicketEndDate))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SeasonDateLastUsed))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NumberofTickets))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NumberofAdults))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NumberofChildren))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DateProcessed))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_OriginRecordType))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_EurostarFareBasis))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_AccountingSundry))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_NonIssueMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_FareMethodMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RefundMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_BarCodeInputMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_AdvanceIssueMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_CrossLondonMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_BookingMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_MinorMaxFareMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SystemGeneratedMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_MultipleIssueMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_PromotionCode))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RecreatedDataMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_ManualInputMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RefundValue))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Filler))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_Currency))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_AccountablePeriod))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_IssuingMachine))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_OperatorID))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SaleNumber))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_DefaultAudit))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RetailTransactionID))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SellingWindow))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_CreditCardAcquirer))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_WarrantAccountHolder_loc_id_wah))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_RunDate))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_PassengerCharterMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_FaresCheckingMarker))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_IssueCommissionProportion))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_SellingMachineonIssue))
    recordLine.append(getZRecordAttrValue(r, UtpConstants.ZRecord_MatchingMarker))
    recordLine.append('\n') //adding new line character to the end of the outputLine
  }

  /**
   *  Left padding String
   */
  def leftPad(inputString: String, nochars: Integer, paddingchar: String): String = {

    inputString.reverse.padTo(nochars, paddingchar).reverse.mkString

  }

  /**
   *
   */
  def roundAt(n: Double, p: Int): Double = BigDecimal(n).setScale(p, BigDecimal.RoundingMode.HALF_UP).toDouble

  /**
   *
   */
  def getInteger(n: Double): Integer = BigDecimal(n).setScale(0, BigDecimal.RoundingMode.HALF_UP).toInt

  /**
   * check Is first date is future date of second input date
   */
  def isFuture(para1: Date, para2: Date) = para1.after(para2)

  /**
   * check Is first date is before date of second input date
   */
  def isBefore(para1: Date, para2: Date) = para1.before(para2)

  /**
   * check Is first date and second input date are same
   */
  def isSameDate(para1: Date, para2: Date) = !(isBefore(para1, para2) || isFuture(para1, para2))

  /**
   * parse the date UtpConstants.UTP_DATE_FORMAT date format
   */
  @throws(classOf[Exception])
  def parseDate(value: String, fromatString: String): Date = {
    try {
      (new SimpleDateFormat(fromatString)).parse(value)
    } catch {
      case e: Exception => throw e
    }
  }

  /**
   *
   */
  def getDateTimeFormatter(formatString: String): DateTimeFormatter = DateTimeFormat.forPattern(formatString)

  /**
   *
   */

  def convertSQLDateToJavaDate(inputDate: java.sql.Date): java.util.Date = new java.util.Date(inputDate.getTime)

  def generateNewSortKey(originalSortKey: String): String = {

    var sortKeyOne: Long = (originalSortKey.drop(1).take(10)).toLong
    sortKeyOne = sortKeyOne + 30: Long
    val sortKeyOneString = sortKeyOne.toString

    var sortKeyTwo: Long = (originalSortKey.drop(11).take(10)).toLong
    sortKeyTwo = sortKeyTwo + 10: Long
    val sortKeyTwoString = sortKeyTwo.toString

    var sortKeyThree: Long = (originalSortKey.drop(21).take(10)).toLong
    sortKeyThree = sortKeyThree + 10: Long
    val sortKeyThreeString = sortKeyThree.toString

    "R" + sortKeyOneString + sortKeyTwoString + sortKeyThreeString + originalSortKey.drop(31)

  }
  
  /**
   *  Retrieve the requested attribute Integer value from ZRecord map
   */
 def getZRecordAttrDoubleValue(r: ZRecord, paraname: String): BigDecimal = {
   
   var s = Utilities.getZRecordAttrValue(r, paraname)   
    var returnValue: BigDecimal = -11111111
    if (s != null && s.length() > 0) {
      print(s)
      returnValue = s match {
        case s if (s.charAt(0) == '-') => BigDecimal(s.substring(1)).setScale(0) * -1
        case s if (s.charAt(0) == '+') => BigDecimal(s.substring(1)).setScale(0)
        case _ => BigDecimal(s).setScale(0)
      }
    }
    returnValue
  }
 
  /**
   * 
   */
  def toUtpNumberConversion(s: String): Long = {
    var inValidValue: Long = -11111111
    if (s != null && s.length() > 0) {
      print(s)
      inValidValue = s match {
        case s if (s.charAt(0) == '-') => s.substring(1).toLong * -1
        case s if (s.charAt(0) == '+') => s.substring(1).toLong
        case _ => s.toLong
      }
    }
    inValidValue
  }
}

