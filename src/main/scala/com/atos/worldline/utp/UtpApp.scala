package com.atos.worldline.utp

import com.atos.worldline.utp.fileprocessing.FileInputRoute
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem

/**
 * @author a585002
 */

object UtpApp extends App {
  //creating Config instances
  val myConfig = ConfigFactory.load("app.conf") 
  val system = ActorSystem("utp", myConfig)
  
  //initiating input route
  FileInputRoute.init(system)
  //initiating output route
  ProcessingRoute.init(system)
  
}  