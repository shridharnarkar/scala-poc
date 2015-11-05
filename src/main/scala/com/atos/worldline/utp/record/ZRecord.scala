package com.atos.worldline.utp.record
/**
 * @author a585002
 */
import scala.collection.mutable.ListMap

/** Z-Record Type for UTP */ 
case class ZRecord (attribute : ListMap[String, String]){
  override def clone = this 
}
