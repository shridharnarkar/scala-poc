package com.atos.worldline.utp.rules

import com.atos.worldline.utp.record.CurrencyDBRecord
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.utils.Utilities
import com.atos.worldline.utp.constants.RecordAttribute
import com.atos.worldline.utp.constants.UtpConstants
import org.slf4j.LoggerFactory
import scala.util.control.Breaks
import com.atos.worldline.utp.dao.LocationDaoObject
import scala.collection.mutable.ListBuffer
import com.atos.worldline.utp.record.ProductDBRecord
/**
 * This is the UtpRule Trait defines the Rules to process UTP records depending upon record Type
 *
 * @author a574891
 */

trait UtpRule {
  def apply(x: ZRecord): ListBuffer[ZRecord]
}

/**
 * It's Rule Repository for UTP Record types
 */
object RuleRepository {
  override def clone = this

  def apply(record: ZRecord): ListBuffer[ZRecord] = new ZRecordRule().apply(record)

  /**
   * Class to process the 'Z' Record using UtpRule processing
   */
  class ZRecordRule extends UtpRule { 

    /** logger for ReferenceData */
    val logger = LoggerFactory.getLogger(classOf[ZRecordRule])

    def apply(zRecord: ZRecord): ListBuffer[ZRecord] = {

      var zRecordList = new ListBuffer[ZRecord]
      val ruleFunctions = RuleFunctionsObject
      val referenceData = ReferenceDataObject
      /**
       * Retrieve Business currency
       */
      //val lennonCurrency = referenceData.lennonCurrency()
      //logger.debug("lennonCurrency :: " + lennonCurrency)
      /**
       * Determine the target currency in which transaction value needs to be converted
       */
     // val locTargetCurrency = ruleFunctions.currencyConversionString(zRecord.attribute.get(UtpConstants.ZRecord_Currency).get.toInt, lennonCurrency, referenceData.currencyCode(UtpConstants.SterlingCurrency))
    //  logger.debug("locTargetCurrency :: " + locTargetCurrency)
      var performFareCheck = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_FaresCheckingMarker) == (UtpConstants.Y_CHAR)
      var additionalRecords = false
      var penaltyProductId = 0
      var fullFare: Double = 0
      var nonPCDiscount = false
      var fareCheckingResult = 0
      var penaltyProduct = 0

      /**
       * perform currency conversion only if target currency is not SPACES
       */
      //if (UtpConstants.SPACES != locTargetCurrency) ruleFunctions.convertCurrencyProcess(zRecord, locTargetCurrency)

      val discountCode: Int = Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_DiscountCode).toInt
      val discountPercentage: Int = Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_DiscountPercentage).toInt

      /**
       * perform Passenger Charter and Non Passenger Charter discount calculation
       * only if discount code and percentage given in record are not zero.
       */
      if (discountCode != 0 && discountPercentage != 0) {
        /**
         * Retrieve discount type based on discount code present in record
         */
        val discountType = ruleFunctions.getDiscountType(discountCode)
        /** Check Is it Passenger Charter discount applicable to this record  */
        if (ruleFunctions.isPassengerCharterDiscountApplicable(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_PassengerCharterMarker), discountType)) {

          logger.debug("CharterServiceObject Before::  " + Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
          fullFare = ruleFunctions.getPassengerCharterFullFare(zRecord) //"CharterServiceObject"

          /**
           * change sign of full fare to match that of the original transaction value.
           */
          fullFare = if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble < 0) -fullFare else fullFare

          if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble != fullFare) {
            Utilities.setZRecordAttrValue(zRecord, UtpConstants.ZRecord_TransactionValue, fullFare.toString())
            additionalRecords = true
            penaltyProductId = Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DiscountCode).toInt
          } // if TransactionValue != calculated fullFare

          logger.debug("CharterServiceObject After:: " + Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_TransactionValue).toDouble)
        } //if RuleFunctionsObject.isPassengerCharterDiscountApplicable
        else if (discountType == referenceData.getCodeBookIdByValue(UtpConstants.DISCOUNT_CODE)) {

          /**
           * check if discount is applicable to the fare applied and fare
           * checking is required as per Non-Passenger Charter discount product
           */
          val (fareCheck, locDiscount) = ruleFunctions.determineNonPCDiscount(zRecord)
          performFareCheck = fareCheck
          nonPCDiscount = locDiscount
        } //else if (discountType == referenceData.getCodeBookIdByValue(UtpConstants.DISCOUNT_CODE))
      } //if (discountCode != 0 && discountPercentage != 0) 

      if (performFareCheck) {
        //First check if any defaults were applied
        if (Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_DefaultAudit).toInt != 0) {
          //A default was applied, so check if record should be excluded from fare checking
          val excludedByDefaultId = referenceData.getCodeBookIdByValue(UtpConstants.EXCLUDED_BY_DEFAULTING)
          fareCheckingResult = referenceData.getFareCheckingResult(zRecord, excludedByDefaultId)
          /**
           * if fare checking result confirms that the checking has to be
           * excluded by default, skip further processing and return the Z
           * record object.
           */
          if (excludedByDefaultId == fareCheckingResult) zRecord
        } //if (Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_DefaultAudit).toInt != 0)

        //add call to LN4324S_O_CHECK_FARES which returns the fareCheckingResult and the fullFare
        val (fcResult, fcFullFare) = ruleFunctions.checkFares(zRecord, fullFare, nonPCDiscount)
        //TODO Penalty check before processing
        val (fcResultRec: Int, fcFullFareRec: Double, locFcAdditionalRecordsRec: Boolean, penalisedBusId: Int, correctingProduct: ProductDBRecord) = ruleFunctions.checkForAdditionalRecords(zRecord, fcResult, fcFullFare)
       	additionalRecords = locFcAdditionalRecordsRec
        // Added by Ashay

        var fares_checking_result_cob_id: Int = 0
        var correct_fare: Double = 0.0

        //zRecordCopyOne will be a cloned copy of zRecord
        val zRecordCopyOne: ZRecord = zRecord.clone()

        if (performFareCheck) {
          //?? what is ln_utp_retail_item_details -> zrecord ??
          //SET loc_current ln_utp_retail_item_details fares_checking_result_cob_id TO in_fc_result code_book 
          //Setting fare checking result to passenger charter marker field in output
          // need to do padding for both if and else block
          Utilities.setZRecordAttrValue(zRecordCopyOne, UtpConstants.ZRecord_PassengerCharterMarker, fcResult.toString())
          Utilities.setZRecordAttrValue(zRecordCopyOne, UtpConstants.ZRecord_Filler, fcFullFareRec.toString())

        } else {
          Utilities.setZRecordAttrValue(zRecordCopyOne, UtpConstants.ZRecord_PassengerCharterMarker, "000000")
          Utilities.setZRecordAttrValue(zRecordCopyOne, UtpConstants.ZRecord_Filler, "")
        }

        zRecordList.append(zRecordCopyOne)

        //zRecordCopyTwo another copy of zRecord
        val zRecordCopyTwo: ZRecord = zRecord.clone()

        // generating_retail_item_rei_id = rei_id
        //Setting Retail Item Id of original input record to Fares Checking Marker as Generating Retail Item
        Utilities.setZRecordAttrValue(zRecordCopyTwo, UtpConstants.ZRecord_FaresCheckingMarker, Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_RetailItemID))
        //correct_fare = 0.0
        Utilities.setZRecordAttrValue(zRecordCopyTwo, UtpConstants.ZRecord_Filler, "0")

        /**
         *  LN4323 _O_Write_Supplementaries
         */

        //zRecordCopyThree : copy of zRecordCopyTwo
        val zRecordCopyThree: ZRecord = zRecordCopyTwo.clone()
        if (Utilities.getZRecordAttrDoubleValue(zRecordCopyThree, UtpConstants.ZRecord_NonIssueMarker) != 0) {
          Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_NonIssueMarker, "0")
        } else {
          Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_NonIssueMarker, referenceData.getCodeBookIdByValue(UtpConstants.ZRecord_NonIssueMarker).toString())
        }

        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_TransactionValue,"-" + ("%014.2f".format(Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_RetailItemID).toDouble)).toString())
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_NumberofTickets, "-" + Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_NumberofTickets))
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_PeriodofValidity, "-" + Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_PeriodofValidity))
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_SystemGeneratedMarker, referenceData.getCodeBookIdByValue("Fares Checking Cancellation").toString())
        
        var zRecordThreeSortKey: String = Utilities.getZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_SortKey)
        val modifiedSortKeyThree = Utilities.generateNewSortKey(zRecordThreeSortKey)
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_SortKey, modifiedSortKeyThree)
        
        zRecordList.append(zRecordCopyThree)

        //zRecordCopyFour : copy of zRecordCopyThree
        val zRecordCopyFour: ZRecord = zRecordCopyThree.clone()

        var decimal10v02 = Utilities.getZRecordAttrDoubleValue(zRecord, UtpConstants.ZRecord_TransactionValue)
        decimal10v02 = (decimal10v02.toDouble - fcFullFare.toDouble)
        Utilities.setZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_TransactionValue, "%014.2f".format(decimal10v02))
        Utilities.setZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_SystemGeneratedMarker, referenceData.getCodeBookIdByValue("Fares Checking Penalty").toString())
        Utilities.setZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_Product, referenceData.getCodeBookIdByValue("Fares Checking Correction").toString())
        Utilities.setZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_NumberofTickets, "-" + Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_NumberofTickets))
        Utilities.setZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_PeriodofValidity, "-" + Utilities.getZRecordAttrValue(zRecord, UtpConstants.ZRecord_PeriodofValidity))

        //// USE ln4328s_w_write_utp_record
        var zRecordFourSortKey: String = Utilities.getZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_SortKey)
        val modifiedSortKeyFour = Utilities.generateNewSortKey(zRecordFourSortKey)
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_SortKey, modifiedSortKeyFour)

        zRecordList.append(zRecordCopyFour)

        //zRecordCopyFive : copy of zRecordCopyFour
        val zRecordCopyFive: ZRecord = zRecordCopyFour.clone()
        Utilities.setZRecordAttrValue(zRecordCopyFive, UtpConstants.ZRecord_TransactionValue, decimal10v02.toString())
        Utilities.setZRecordAttrValue(zRecordCopyFive, UtpConstants.ZRecord_SystemGeneratedMarker, referenceData.getCodeBookIdByValue("Fares Checking Penalty").toString())

        ///// USE ln4328s_w_write_utp_record
        var zRecordFiveSortKey: String = Utilities.getZRecordAttrValue(zRecordCopyFour, UtpConstants.ZRecord_SortKey)
        val modifiedSortKeyFive = Utilities.generateNewSortKey(zRecordFourSortKey)
        Utilities.setZRecordAttrValue(zRecordCopyThree, UtpConstants.ZRecord_SortKey, modifiedSortKeyFive)
        
        zRecordList.append(zRecordCopyFive)
        

      } //if (performFareCheck)

      logger.debug("retrun zRecordList :: " + zRecordList)
      //returns zRecord List
      zRecordList
    } //apply


  } //class ZRecordRule extends UtpRule

}//object RuleRepository

