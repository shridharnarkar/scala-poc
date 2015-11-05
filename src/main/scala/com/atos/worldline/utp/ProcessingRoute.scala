package com.atos.worldline.utp

import scala.collection.mutable.ListBuffer
import org.apache.camel.CamelContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.atos.worldline.utp.record.ZRecord
import com.atos.worldline.utp.rules.ReferenceDataObject
import com.atos.worldline.utp.rules.RuleRepository
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.camel.CamelExtension
import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.camel.Producer
import com.atos.worldline.utp.utils.Utilities

/**
 * This is class do the processing of Utp Record message of Type Z
 *
 * @author a574891
 */
object ProcessingRoute {

  /**
   * Initialize the requested resource for Processing route
   */
  def init(system: ActorSystem) = {
    val producer = system.actorOf(Props[UtpProducer])
    val processingCamelContext = CamelExtension(system).context
    val ruleActor = system.actorOf(Props(classOf[RuleActor], producer, processingCamelContext))
    val consumer = system.actorOf(Props(classOf[UtpConsumer], ruleActor))

    println("ProcessingRoute init")
  }

  /**
   *
   */
  class UtpConsumer(ruleActor: ActorRef) extends Consumer {
    def endpointUri = "direct:utpZrecordProcessing"
    override def autoAck = true
    def receive = {
      case msg: CamelMessage =>
        ruleActor.tell(msg, sender)

    }
  }

  /**
   *
   */
  class RuleActor(producer: ActorRef, ruleCamelContext: CamelContext) extends Actor {
    def receive = {
      case msg: CamelMessage =>
        val zRecord = synchronized(RuleRepository.apply(msg.getBodyAs(classOf[ZRecord], ruleCamelContext)))
        producer.tell(msg.withBody(zRecord), sender)
    }
  }

  /**
   *
   */
  class UtpProducer extends Producer {

    val logger = LoggerFactory.getLogger(classOf[UtpProducer])

    /** Reference value : ""file:C:/Lennon/POC/output"" */
    def endpointUri = ReferenceDataObject.getPropertyValue("outputFile") + "?fileExist=Append"

    override def oneway = true
    override def transformOutgoingMessage(msg: Any) = msg match {
      case camelMsg: CamelMessage =>

        var outputLine = new StringBuilder()
        outputLine = camelMsg match {
          case camelMsg if (camelMsg.body.isInstanceOf[ListBuffer[ZRecord]]) =>
                  logger.debug("ZRecord : " + msg)
                  //Process camel message body according to its type
                  var zRecordList: ListBuffer[ZRecord] = camelMsg.getBodyAs(classOf[ListBuffer[ZRecord]], camelContext)
                  outputLine = zRecordList match {
                    case zRecordList if (zRecordList != null && zRecordList.length > 0) =>
                      for (zRecValue <- zRecordList) Utilities.getZRecordLine(zRecValue, outputLine)
                      outputLine //Returning Output RecordLine
                    case _ =>
                      outputLine.append(camelMsg.body.toString())
                      outputLine.append('\n') //adding new line character to the end of the outputLine
                      outputLine //Returning Output RecordLine
                  }
                 outputLine //Returning Output RecordLine
          case _ =>
                outputLine.append(camelMsg.body.toString())
                outputLine.append('\n')//Returning Output RecordLine
        }
        println("outputLine : " + outputLine)
        //Determining outputFile name from camel message header. The name should be same as input file
        val headers = camelMsg.headers
        val inputFileName = headers.get("CamelFileName").toString()
        val outputFileName = inputFileName.substring(inputFileName.indexOf('(') + 1, inputFileName.indexOf(')'))

        logger.debug("outputFileName : " + outputFileName)
        CamelMessage(outputLine.toString(), Map("CamelFileName" -> (outputFileName)))
    }

  }
}