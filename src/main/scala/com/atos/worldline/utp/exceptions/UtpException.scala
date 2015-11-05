package com.atos.worldline.utp.exceptions

/**
 * This UPT Custom Exception class which Handle UTP processing Exception
 *
 * @author a574891
 */
class UtpException(errorcode: String, message: String, nestedException: Throwable) extends Exception {
  def this() = this("","", new RuntimeException())
  def this(errorcode: String, message: String) = this(errorcode, message, null)
  def this(message: String) = this("",message, null)
  def this(message: String,nestedException: Throwable) = this("",message, nestedException)
  def this(nestedException: Throwable) = this("","", nestedException)
}

object UtpException {
  def apply() = new UtpException()
  def apply(errorcode: String, message: String) = new UtpException(errorcode, message)
  def apply(message: String) = new UtpException(message)
  def apply(message: String, throwable: Throwable) = new UtpException(message, throwable)
  def apply(errorcode: String,message: String, throwable: Throwable) = new UtpException(errorcode,message, throwable)
}