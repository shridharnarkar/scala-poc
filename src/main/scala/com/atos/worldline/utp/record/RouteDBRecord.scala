package com.atos.worldline.utp.record

import java.sql.Date

/**
 * @author a162012
 */


case class RouteDBRecord(routeId: Int,expired: Option[String], effectiveFrom: Option[Date], effectiveTo: Option[Date], routeCode: Option[String] , routeDesc: Option[String], tacId: Int, cobIdRouteType: Int)