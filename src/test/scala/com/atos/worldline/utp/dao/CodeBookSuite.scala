package com.atos.worldline.utp.dao

import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

import com.atos.worldline.utp.constants.UtpConstants
import com.atos.worldline.utp.record.CodeBookDBRecord
import org.mockito.Matchers._

import org.junit.{Test, Before}
import org.mockito.runners.MockitoJUnitRunner

/**
 * @author a579482
 */
@RunWith(classOf[MockitoJUnitRunner])
class CodeBookSuite extends FunSuite with BeforeAndAfter with MockitoSugar {

  before {
    //TODO do nothing
  }

  test("test getCodeBook DB call functionality ") {

    val codeBookDAOService = CodeBookDaoObject
    val mockDummyData: CodeBookDBRecord = new CodeBookDBRecord(300, Some("Sterling"), None, None, None, None, 300, 1)
    when(codeBookDAOService.getCodeBookByValue(anyString())).thenReturn(mockDummyData)
    when(codeBookDAOService.getCodeBookById(anyInt())).thenReturn(mockDummyData)
    assertResult(mockDummyData)(codeBookDAOService.getCodeBookByValue(UtpConstants.SterlingCurrency))

  }

  test("test CodeBook dao service for getCodeBookId functionality") {

    val codeBookDAOService = CodeBookDaoObject
    val mockDummyData: CodeBookDBRecord = new CodeBookDBRecord(300, Some("Sterling"), None, None, None,None, 300, 1)

    when(codeBookDAOService.getCodeBookByValue(anyString())).thenReturn(mockDummyData)
    when(codeBookDAOService.getCodeBookById(anyInt())).thenReturn(mockDummyData)

    assertResult(300)(codeBookDAOService.getCodeBookById(anyInt()))
    //Without mock
    assertResult(300)(CodeBookDaoObject.getCodeBookByValue(UtpConstants.SterlingCurrency).cobId)
  }

}