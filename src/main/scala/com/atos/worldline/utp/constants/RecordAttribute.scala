package com.atos.worldline.utp.constants

import scala.collection.mutable.ListMap

/**
 * @author a585002
 */

//Object to store all attributes of Records taken from input file
object RecordAttribute {
  val AttbMap = ListMap("SortKey" -> Array(0, 62), //1,62
    "RecordType" -> Array(62, 63), //63,1
    "ApportionmentIndicator" -> Array(63, 64), //64,1
    "IssuingLocation" -> Array(64, 70), //65,6
    "SellingLocation" -> Array(70, 76), //71,6
    "Origin" -> Array(76, 82), //77,6
    "Destination" -> Array(82, 88), //83,6
    "Route" -> Array(88, 94), // 89,6
    "TicketStatus" -> Array(94, 100), //95,6
    "ApportionmentMOPBasis" -> Array(100, 106), //101,6
    "Product" -> Array(106, 112), //107,6
    "DiscountCode" -> Array(112, 118), //113,6
    "DiscountPercentage" -> Array(118, 122), //119,4
    "DateandTimeofIssue" -> Array(122, 142), //123,20
    "NumberofPeople" -> Array(142, 148), //143,6
    "DeferredIssueIdentifier" -> Array(148, 156), //149,8
    "UNUSED" -> Array(156, 168), //157,12
    "DateofSale" -> Array(168, 176), //169,8
    "DeferredIssueType" -> Array(176, 182), //177,6
    "TransactionValue" -> Array(182, 196), //183,14
    "RetailItemID" -> Array(196, 206), //197,10
    "DateofTravelorSeasonStartDate" -> Array(206, 215), //207,8
    "LostDays" -> Array(215, 217), //215,3
    "PeriodofValidity" -> Array(217, 222), //218,5
    "RetailTransactionID" -> Array(222, 232), //223,10
    "TicketNumber" -> Array(232, 241), //233,9
    "TransactionNumber" -> Array(241, 247), //242,6
    "SundryTransactionNumber" -> Array(247, 253), //248,6
    "NumberofSundryItems" -> Array(254, 259), //254,5
    "BankGiroCreditSerial" -> Array(259, 265), //259,7
    "SeasonTicketEndDate" -> Array(265, 273), //266,8
    "SeasonDateLastUsed" -> Array(273, 281), //274,8
    "NumberofTickets" -> Array(281, 287), //282,6
    "NumberofAdults" -> Array(287, 290), //288,3
    "NumberofChildren" -> Array(290, 293), //291,3
    "DateProcessed" -> Array(293, 313), //294,20
    "OriginRecordType" -> Array(313, 320), //314,6
    "EurostarFareBasis" -> Array(320, 327), //320,8
    "AccountingSundry" -> Array(327, 337), //328,10
    "NonIssueMarker" -> Array(337, 343), //338,6
    "FareMethodMarker" -> Array(343, 349), //344,6
    "RefundMarker" -> Array(349, 355), //350,6
    "BarCodeInputMarker" -> Array(355, 361), //356,6
    "AdvanceIssueMarker" -> Array(361, 367), //362,6
    "CrossLondonMarker" -> Array(367, 373), //368,6
    "BookingMarker" -> Array(373, 379), //374,6
    "MinorMaxFareMarker" -> Array(379, 385), //380,6
    "SystemGeneratedMarker" -> Array(385, 391), //386,6
    "MultipleIssueMarker" -> Array(391, 397), //392,6 
    "PromotionCode" -> Array(397, 400), //398,3
    "RecreatedDataMarker" -> Array(400, 406), //401,6
    "ManualInputMarker" -> Array(406, 412), //407,6
    "RefundValue" -> Array(412, 426), //413,14
    "Filler" -> Array(426, 440), //427,14
    "Currency" -> Array(440, 446), //441,6
    "AccountablePeriod" -> Array(446, 452), //447,6
    "IssuingMachine" -> Array(452, 458), //453,6
    "OperatorID" -> Array(458, 466), //459,8
    "SaleNumber" -> Array(466, 472), //467,6
    "DefaultAudit" -> Array(472, 478), //473,6
    "IssuingWindow" -> Array(478, 484), //479,6
    "SellingWindow" -> Array(484, 490), //485,6
    "CreditCardAcquirer" -> Array(490, 496), //491,6
    "WarrantAccountHolder_loc_id_wah" -> Array(496, 502), //497,6
    "RunDate" -> Array(502, 510), //503,8
    "PassengerCharterMarker" -> Array(510, 511), //511,1
    "FaresCheckingMarker" -> Array(511, 512), //512,1
    "IssueCommissionProportion" -> Array(512, 518), //513,6
    "SellingMachineonIssue" -> Array(518, 524), //519,6
    "MatchingMarker" -> Array(524, 530)) //525,6
}