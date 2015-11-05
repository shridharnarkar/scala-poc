package com.atos.worldline.utp.fileprocessing

import scala.collection.mutable.ListMap
import scala.io.Source
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.atos.worldline.utp.ProcessingRoute.UtpProducer
import com.atos.worldline.utp.constants.RecordAttribute
import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.fileprocessing.FileInputRoute.FileInputConsumer
import com.atos.worldline.utp.fileprocessing.FileInputRoute.FileInputTransformer
import com.atos.worldline.utp.record.ZRecord
import akka.event.Logging
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.camel.Producer
import org.apache.camel.converter.stream.InputStreamCache
import com.atos.worldline.utp.rules.ReferenceDataObject

/**
 * @author a584667
 */
object FileInputRoute {

  def init(system: ActorSystem) = {
    val inputProducer = system.actorOf(Props[FileInputProducer])
    val outputProducer = system.actorOf(Props[UtpProducer])
    val transformer = system.actorOf(Props(classOf[FileInputTransformer], inputProducer))
    val consumer = system.actorOf(Props(classOf[FileInputConsumer], transformer, outputProducer))
    println("FileInputRoute init")
  }

  /**
   * Reads the file,checks for the line  sends to the transformer
   * need to transfer only the Z record
   */

  class FileInputConsumer(transformer: ActorRef, outputProducer: ActorRef) extends Consumer {

    val logger = LoggerFactory.getLogger(classOf[FileInputConsumer])

    /** Reference value : ""file:C:/Lennon/POC/input"" */
    def endpointUri = ReferenceDataObject.getPropertyValue("inputFile")

    def receive = {
      case msg: CamelMessage =>
        print("FileInputConsumer")
        val ip = msg.getBodyAs(classOf[InputStreamCache], camelContext)
        for (line <- Source.fromInputStream(ip).getLines()) {
          line match {
            case ht if ht.matches("""^[H,T].*""") =>  outputProducer.tell(msg.withBody(line), sender)
            case zr if zr.matches("""^R.{61}Z.*""") =>  transformer.tell(msg.withBody(line), sender)
            case nzr if nzr.matches("""^R.{61}[^Z].*""") => outputProducer.tell(msg.withBody(line), sender)
            case _ => "Error - None of them are matching"
          }
          logger.debug(line)
          print(line)

        } //end of for loop
    } // end of def receive
  } // end of class FileInputConsumer

  /**
   * This transformer Class used to enrich the Zrecord object received into Camel Message body
   */
  class FileInputTransformer(producer: ActorRef) extends Actor {

    val logger = LoggerFactory.getLogger(classOf[FileInputTransformer])

    def receive = {
      case msg: CamelMessage =>
        logger.debug("FileInputTransformer")
        var zrecordObj = transformToRecordZ(msg.body.toString())
        producer.tell(msg.withBody(zrecordObj), sender)
    }

    /** Extract/Transform the ZRecord record attribute value */
    def transformToRecordZ(record: String): ZRecord = {

      val attbs: scala.collection.mutable.ListMap[String, String] = scala.collection.mutable.ListMap()
      RecordAttribute.AttbMap foreach { case (dKey, dVal) => attbs.put(dKey, record.substring(dVal(0), dVal(1))) }
      //      val attribMap: HashMap[String, String] = HashMap()
      //      val sortkey = line.substring(62)
      //      attribMap.put(RecordAttribute.SortKey, sortkey)
      //      val recordType = line.drop(62).take(1)
      //      attribMap.put(RecordAttribute.RecordType, recordType)
      //      val apportionmentIndicator = line.drop(63).take(1)
      //      attribMap.put(RecordAttribute.ApportionmentIndicator, apportionmentIndicator)
      //      val issuingLocation = line.drop(64).take(6)
      //      attribMap.put(RecordAttribute.IssuingLocation, issuingLocation)
      //      val sellingLocation = line.drop(70).take(6)
      //      attribMap.put(RecordAttribute.SellingLocation, sellingLocation)
      //      val origin = line.drop(76).take(6)
      //      attribMap.put(RecordAttribute.Origin, origin)
      //      val destination = line.drop(82).take(6)
      //      attribMap.put(RecordAttribute.Destination, destination)
      //      val route = line.drop(88).take(6)
      //      attribMap.put(RecordAttribute.Route, route)
      //      val ticketStatus = line.drop(94).take(6)
      //      attribMap.put(RecordAttribute.TicketStatus, ticketStatus)
      //      val apportionmentMOPBasis = line.drop(100).take(6)
      //      attribMap.put(RecordAttribute.ApportionmentMOPBasis, apportionmentMOPBasis)
      //      val product = line.drop(106).take(6)
      //      attribMap.put(RecordAttribute.Product, product)
      //      val discountCode = line.drop(112).take(6)
      //      attribMap.put(RecordAttribute.DiscountCode, discountCode)
      //      val discountPercentage = line.drop(118).take(4)
      //      attribMap.put(RecordAttribute.DiscountPercentage, discountPercentage)
      //      val dateandTimeofIssue = line.drop(122).take(20)
      //      attribMap.put(RecordAttribute.DateandTimeofIssue, dateandTimeofIssue)
      //      val numberofPeople = line.drop(142).take(6)
      //      attribMap.put(RecordAttribute.NumberofPeople, numberofPeople)
      //      val deferredIssueIdentifier = line.drop(148).take(8)
      //      attribMap.put(RecordAttribute.DeferredIssueIdentifier, deferredIssueIdentifier)
      //      val unsued = line.drop(156).take(12)
      //      attribMap.put(RecordAttribute.UNUSED, unsued)
      //      val dateofSale = line.drop(168).take(8)
      //      attribMap.put(RecordAttribute.DateofSale, dateofSale)
      //      val deferredIssueType = line.drop(176).take(6)
      //      attribMap.put(RecordAttribute.DeferredIssueType, deferredIssueType)
      //      val transactionValue = line.drop(182).take(14)
      //      attribMap.put(RecordAttribute.TransactionValue, transactionValue)
      //      val retailItemID = line.drop(196).take(10)
      //      attribMap.put(RecordAttribute.RetailItemID, retailItemID)
      //      val dateofTravelSeasonStartDate = line.drop(206).take(8)
      //      attribMap.put(RecordAttribute.DateofTravelorSeasonStartDate, dateofTravelSeasonStartDate)
      //      val lostDays = line.drop(214).take(3)
      //      attribMap.put(RecordAttribute.LostDays, lostDays)
      //      val periodofValidity = line.drop(217).take(5)
      //      attribMap.put(RecordAttribute.PeriodofValidity, periodofValidity)
      //      val retailTransactionID = line.drop(222).take(10)
      //      attribMap.put(RecordAttribute.RetailTransactionID, retailTransactionID)
      //      val ticketNumber = line.drop(232).take(9)
      //      attribMap.put(RecordAttribute.TicketNumber, ticketNumber)
      //      val transactionNumber = line.drop(241).take(6)
      //      attribMap.put(RecordAttribute.TransactionNumber, transactionNumber)
      //      val sundryTransactionNumber = line.drop(247).take(6)
      //      attribMap.put(RecordAttribute.SundryTransactionNumber, sundryTransactionNumber)
      //      val numberofSundryItems = line.drop(253).take(5)
      //      attribMap.put(RecordAttribute.NumberofSundryItems, numberofSundryItems)
      //      val bankGiroCreditSerial = line.drop(258).take(7)
      //      attribMap.put(RecordAttribute.BankGiroCreditSerial, bankGiroCreditSerial)
      //      val seasonTicketEndDate = line.drop(265).take(8)
      //      attribMap.put(RecordAttribute.SeasonTicketEndDate, seasonTicketEndDate)
      //      val seasonDateLastUsed = line.drop(273).take(8)
      //      attribMap.put(RecordAttribute.SeasonDateLastUsed, seasonDateLastUsed)
      //      val numberofTickets = line.drop(281).take(6)
      //      attribMap.put(RecordAttribute.NumberofTickets, numberofTickets)
      //      val numberofAdults = line.drop(287).take(3)
      //      attribMap.put(RecordAttribute.NumberofAdults, numberofAdults)
      //      val numberofChildren = line.drop(290).take(3)
      //      attribMap.put(RecordAttribute.NumberofChildren, numberofChildren)
      //      val dateProcessed = line.drop(293).take(20)
      //      attribMap.put(RecordAttribute.DateProcessed, dateProcessed)
      //      val originRecordType = line.drop(313).take(6)
      //      attribMap.put(RecordAttribute.OriginRecordType, originRecordType)
      //      val eurostarFareBasis = line.drop(319).take(8)
      //      attribMap.put(RecordAttribute.EurostarFareBasis, eurostarFareBasis)
      //      val accountingSundry = line.drop(327).take(10)
      //      attribMap.put(RecordAttribute.AccountingSundry, accountingSundry)
      //      val nonIssueMarker = line.drop(337).take(6)
      //      attribMap.put(RecordAttribute.NonIssueMarker, nonIssueMarker)
      //      val fareMethodMarker = line.drop(343).take(6)
      //      attribMap.put(RecordAttribute.FareMethodMarker, fareMethodMarker)
      //      val refundMarker = line.drop(349).take(6)
      //      attribMap.put(RecordAttribute.RefundMarker, refundMarker)
      //      val barCodeInputMarker = line.drop(355).take(6)
      //      attribMap.put(RecordAttribute.BarCodeInputMarker, barCodeInputMarker)
      //      val advanceIssueMarker = line.drop(361).take(6)
      //      attribMap.put(RecordAttribute.AdvanceIssueMarker, advanceIssueMarker)
      //      val crossLondonMarker = line.drop(367).take(6)
      //      attribMap.put(RecordAttribute.CrossLondonMarker, crossLondonMarker)
      //      val bookingMarker = line.drop(373).take(6)
      //      attribMap.put(RecordAttribute.BookingMarker, bookingMarker)
      //      val minMaxFareMarker = line.drop(379).take(6)
      //      attribMap.put(RecordAttribute.MinorMaxFareMarker, minMaxFareMarker)
      //      val systemGeneratedMarker = line.drop(385).take(6)
      //      attribMap.put(RecordAttribute.SystemGeneratedMarker, systemGeneratedMarker)
      //      val multipleIssueMarker = line.drop(391).take(6)
      //      attribMap.put(RecordAttribute.MultipleIssueMarker, multipleIssueMarker)
      //      val promotionCode = line.drop(397).take(3)
      //      attribMap.put(RecordAttribute.PromotionCode, promotionCode)
      //      val recreatedDataMarker = line.drop(400).take(6)
      //      attribMap.put(RecordAttribute.RecreatedDataMarker, recreatedDataMarker)
      //      val manualInputMarker = line.drop(406).take(6)
      //      attribMap.put(RecordAttribute.ManualInputMarker, manualInputMarker)
      //      val refundValue = line.drop(412).take(14)
      //      attribMap.put(RecordAttribute.RefundValue, refundValue)
      //      val filler = line.drop(426).take(14)
      //      attribMap.put(RecordAttribute.Filler, filler)
      //      val currency = line.drop(440).take(6)
      //      attribMap.put(RecordAttribute.Currency, currency)
      //      val accountablePeriod = line.drop(446).take(6)
      //      attribMap.put(RecordAttribute.AccountablePeriod, accountablePeriod)
      //      val issuingMachine = line.drop(452).take(6)
      //      attribMap.put(RecordAttribute.IssuingMachine, issuingMachine)
      //      val operatorID = line.drop(458).take(8)
      //      attribMap.put(RecordAttribute.OperatorID, operatorID)
      //      val saleNumber = line.drop(466).take(6)
      //      attribMap.put(RecordAttribute.SaleNumber, saleNumber)
      //      val defaultAudit = line.drop(472).take(6)
      //      attribMap.put(RecordAttribute.DefaultAudit, defaultAudit)
      //      val issuingWindow = line.drop(478).take(6)
      //      attribMap.put(RecordAttribute.IssuingWindow, issuingWindow)
      //      val sellingWindow = line.drop(484).take(6)
      //      attribMap.put(RecordAttribute.SellingWindow, sellingWindow)
      //      val creditCardAcquirer = line.drop(490).take(6)
      //      attribMap.put(RecordAttribute.CreditCardAcquirer, creditCardAcquirer)
      //      val warrantAccountHolder = line.drop(496).take(6)
      //      attribMap.put(RecordAttribute.WarrantAccountHolder_loc_id_wah, warrantAccountHolder)
      //      val runDate = line.drop(502).take(8)
      //      attribMap.put(RecordAttribute.RunDate, runDate)
      //      val passengerCharterMarker = line.drop(510).take(1)
      //      attribMap.put(RecordAttribute.PassengerCharterMarker, passengerCharterMarker)
      //      val faresCheckingMarker = line.drop(511).take(1)
      //      attribMap.put(RecordAttribute.FaresCheckingMarker, faresCheckingMarker)
      //      val issueCommissionProportion = line.drop(512).take(6)
      //      attribMap.put(RecordAttribute.IssueCommissionProportion, issueCommissionProportion)
      //      val sellingMachineonIssue = line.drop(518).take(6)
      //      attribMap.put(RecordAttribute.SellingMachineonIssue, sellingMachineonIssue)
      //      val matchingMarker = line.drop(524).take(6)
      //      attribMap.put(RecordAttribute.MatchingMarker, matchingMarker)

      logger.debug("FileInputTransformer  ZRecord  :: " + ZRecord(attbs))
      ZRecord(attbs)

    } // end of transformToRecordZ

  } // end of class FileInputTransformer

  class FileInputProducer extends Producer {

    val logger = LoggerFactory.getLogger(classOf[FileInputProducer])
    
    def endpointUri = "direct:utpZrecordProcessing"
   
    override def oneway = true
    override def transformOutgoingMessage(msg: Any) = msg match {
      case camelMsg: CamelMessage =>
        logger.debug("FileInputProducer transformOutgoingMessage" + camelMsg)
        camelMsg
    }
  } //class FileInputProducer extends Producer

}  