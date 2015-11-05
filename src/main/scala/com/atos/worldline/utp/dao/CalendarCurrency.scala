package com.atos.worldline.utp.dao

import java.sql.Date
import com.atos.worldline.utp.record.CurrencyDBRecord
import slick.driver.MySQLDriver.simple._
import slick.lifted.ForeignKeyQuery
import slick.lifted.ProvenShape

/**
 * This class represent Calendar_Currency table into database
 */
class CalendarCurrency(tag: Tag)
    extends Table[CurrencyDBRecord](tag, "calendar_currency") {

  // This is the primary key column:
  def currencyRate: Rep[BigDecimal] = column[BigDecimal]("currency_rate")
  def conversionRate: Rep[Int] = column[Int]("conversion_rate")
  def currencyDate: Rep[Date] = column[Date]("currency_date")
 
  def * = (currencyRate, conversionRate, currencyDate) <> (CurrencyDBRecord.tupled, CurrencyDBRecord.unapply)
}

/**
 * This scala object represent the Query Object for calendar_currency database table
 */
/*object CalendarCurrencyQueryObject {
  lazy val calendarCurrency = TableQuery[CalendarCurrency]
}
*/

