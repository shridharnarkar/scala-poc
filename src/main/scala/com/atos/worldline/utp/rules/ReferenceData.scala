package com.atos.worldline.utp.rules

import java.util.Locale
import java.util.ResourceBundle
import org.joda.time.Days
import org.slf4j.LoggerFactory
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.dao.ApplicationParametersDaoObject
import com.atos.worldline.utp.dao.BusinessDaoObject
import com.atos.worldline.utp.dao.CalendarDaoObject
import com.atos.worldline.utp.dao.CodeBookDaoObject
import com.atos.worldline.utp.dao.CurrencyDaoObject
import com.atos.worldline.utp.dao.FareDaoObject
import com.atos.worldline.utp.dao.LocationDaoObject
import com.atos.worldline.utp.dao.ProductDaoObject
import com.atos.worldline.utp.dao.TicketStatusDaoObject
import com.atos.worldline.utp.dao.TicketStatusLinkDaoObject
import com.atos.worldline.utp.dao.UtpDaoObject
import com.atos.worldline.utp.record.ApplicationParametersDBRecord
import com.atos.worldline.utp.record.CalendarDBRecord
import com.atos.worldline.utp.record.CodeBookDBRecord
import com.atos.worldline.utp.record.FareDBRecord
import com.atos.worldline.utp.record.LocationDBRecord
import com.atos.worldline.utp.record.ProductDBRecord
import com.atos.worldline.utp.record.TicketStatusDBRecord
import com.atos.worldline.utp.record.TicketStatusLinkDBRecord
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.dao.FaresCheckingExclusionsDaoObject
import java.sql.Date

/**
 * This scala class provide the place holder to do the UTP data processing
 *
 * @author a574891
 *
 */
object ReferenceDataObject {

  /** logger for ReferenceData */
  val logger = LoggerFactory.getLogger(classOf[ReferenceData])
  var $resourceBundle: ResourceBundle = _
  var $locale = Locale.getDefault
  def $loadBundle(loc: Locale) = this synchronized {
    $resourceBundle = ResourceBundle.getBundle("utp", $locale)
  }
  $loadBundle($locale)

  def locale = $locale
  def locale_=(loc: Locale) = this synchronized {
    $loadBundle(loc)
    $locale = loc
  }
  /**
   * Get Currency Code
   */
  def currencyCode(x: String): Int = {
    logger.debug("UtpReferenceData :: currencyCode >>")
    val currencyMap = CurrencyDaoObject.getCurrencyCode()
    logger.debug(s"UtpReferenceData :: currencyMap >> $currencyMap")
    currencyMap(x)
  }

  /**
   * return Product Type depending upon discount Code
   */
  def getProductType(productCode: Int): Int = {
    logger.debug("UtpReferenceData :: productType >>")
    val productType = ProductDaoObject.getProductTypeById(productCode)
    logger.debug(s"UtpReferenceData :: productType >> $productType")
    productType
  }

  /**
   * return all Discount Products where product code is DEFLT
   */
  def getDefaultDiscountProductList(): List[ProductDBRecord] = {
    logger.debug("UtpReferenceData :: getDefaultDiscountProductList >>")
    ProductDaoObject.getProductByCode(UtpConstants.DEFAULT_DISCOUNT_PRODUCT_CODE)
  }

  /**
   * LN4349S_R_GET_CURRENCY_COBS : get the lennon currency
   */
  def lennonCurrency(): Int = {
    //get Business DB record
    val businessList = BusinessDaoObject.getBusiness()
    logger.debug(s"BusinessList : $businessList")
    var lennonCurrency: Int = 0
    //val businessMap = Map("Lennon" -> "13/10/2015")

    print(businessList)

    val batchDate = org.joda.time.DateTime.now
    for (businessRecord <- businessList) {
      print("BusName :::" + businessRecord.busName.get.toString())
    }

    for (businessRecord <- businessList if businessRecord.busName.get.toString() == UtpConstants.LENNON) {
      val euroConversionDateTime = new org.joda.time.DateTime(businessRecord.euroConversionDate.get.getTime)
      lennonCurrency = Days.daysBetween(euroConversionDateTime, batchDate).getDays match {
        case x if x < 0  => currencyCode(UtpConstants.SterlingCurrency)
        case y if y >= 0 => currencyCode(UtpConstants.EuroCurrency)
        case _           => -1
      }

    }
    lennonCurrency
  }

  /**
   * LN4314S_R_GET_PRODUCT_TYPE
   */
  def getProductTypeById(): String = {
    val prodcutCode = "000000"
    val productMap = Map("000000" -> "000320", "000001" -> "")
    productMap(prodcutCode)
  }

  /**
   *  getCodeBookId for code Book Value
   */
  def getCodeBookIdByValue(value: String): Int = {
    logger.debug("UtpReferenceData :: getCodeBookIdByValue")
    CodeBookDaoObject.getCodeBookByValue(value).cobId
  }

  /**
   * LN4355S_FC_EXCLUDE_DEFAULTED
   */
  def getFareCheckingResult(zRecord: ZRecord, excludedByDefaultCobId: Int): Int = {
    logger.debug("UtpReferenceData :: getFareCheckingResult")
    excludedByDefaultCobId
  }

  /**
   * Get Full Fare ln4306s_o_passengers_charter
   */
  def getFullFare(transactionValue: Double, discountPercentage: Int): Double = {
    logger.debug("UtpReferenceData :: getFullFare")
    val fullFare: Double = (transactionValue / (100 - discountPercentage)) * 100
    fullFare
  }

  /**
   *
   * @param propertyName
   * @return
   */
  def getPropertyValue(propertyName: String): String = {
    /* Reads properties file */
    $resourceBundle.getString(propertyName)

  }

  /**
   *
   * @param value
   * @return
   */
  def getAbsolute(value: Double): Double = {
    if (value < 0) -value else value
  }

  /**
   *  LN4308S_O_GET_VALID_FARE :  fetch Fare object list to obtain Construct FBO query
   */
  def getFareList(): List[FareDBRecord] = {
    logger.debug("UtpReferenceData :: getFareList")
    FareDaoObject.getFares();
  }

  /**
   *
   * @param name
   * @return
   */
  def getApplicationParameterValue(name: String): Int = {
    logger.debug("UtpReferenceData :: getApplicationParameters")
    val applicationParamaeterValue: ApplicationParametersDBRecord = ApplicationParametersDaoObject.getApplicationParametersByName(name)
    applicationParamaeterValue.integerValue
  }

  /**
   *
   */
  def getCurrentPeriodByDate(): Int = {
    logger.debug("UtpReferenceData :: getCurrentPeriodByDate")
    val calendarPeriodValue: CalendarDBRecord = CalendarDaoObject.getCalendarPeriodByDate()
    calendarPeriodValue.period
  }

  /**
   * *
   *
   */
  def getBusinessIdBySellingLocation(groupFunTypeDesc: String): Int = {
    logger.debug("UtpReferenceData :: getBusinessIdBySellingLocation")
    UtpDaoObject.getBusinessIdBySellingLocation(groupFunTypeDesc)
  }

  /**
   *
   */

  def getLocationById(locationId: Int): LocationDBRecord = {
    logger.debug("UtpReferenceData :: getLocationById")
    LocationDaoObject.getLocationById(locationId)
  }

  /**
   *  Get the ID of the linked parent product which has a product type of Primary Product Group (PPG)
   */
  def getCodeBookById(id: Int): CodeBookDBRecord = {
    logger.debug("UtpReferenceData :: getCodeBookById")
    CodeBookDaoObject.getCodeBookById(id)
  }

  /**
   *
   */
  def getTicketStatusLinkByLinkedById(ticketStatusId: Int): TicketStatusLinkDBRecord = {
    logger.debug("UtpReferenceData :: getTicketStatusLinkByLinkedById")
    TicketStatusLinkDaoObject.getTicketStatusLinkByLinkedById(ticketStatusId)

  }

  /**
   *
   */
  def getTicketStatusByCode(ticketStatusCode: String): TicketStatusDBRecord = {
    logger.debug("UtpReferenceData :: getTicketStatusByCode")
    TicketStatusDaoObject.getTicketStatusByCode(ticketStatusCode)
  }

  /**
   *
   */
  def getTicketStatusById(ticketStatusId: Int): TicketStatusDBRecord = {
    logger.debug("UtpReferenceData :: getTicketStatusById")
    TicketStatusDaoObject.getTicketStatusById(ticketStatusId)
  }

  /**
   * Look for correcting product for current business
   */
  def getCorrectProductForCurrentBussiness(penalisedBusId: Int, correctionTypecobId: Int): ProductDBRecord = {
    logger.debug("UtpReferenceData :: getCorrectProductForCurrentBussiness")
    UtpDaoObject.getCorrectProductForCurrentBussiness(penalisedBusId, correctionTypecobId)
  }

  /**
   *  verify is FaresCheckingExclusions
   */
  def isFareCheckExclusion(locIssueDate: Date, fareRec: FareDBRecord): Boolean = {
    logger.debug("UtpReferenceData :: isFareCheckExclusion")
    return FaresCheckingExclusionsDaoObject.getFaresCheckingExclusionsId(locIssueDate,fareRec)
  }

}

/**
 *   This scala Object provide the place holder do the UTP data processing
 */
/*object ReferenceDataObject {
  def apply(): ReferenceData = {
   return new ReferenceData
  }

}*/

class ReferenceData {

}