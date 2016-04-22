package core

import java.text.DecimalFormat

import sale.model.Currency

/**
  * Implementation based on http://www.algorytm.org/inne/zamiana-liczby-na-slowa-z-polska-gramatyka.html
  */
object NumberTranslator {

  val unity = Array("", "jeden ", "dwa ", "trzy ", "cztery ", "pięć ", "sześć ", "siedem ", "osiem ", "dziewięć ")
  val tens = Array("", "jedenaście ", "dwanaście ", "trzynaście ", "czternaście ", "piętnaście ", "szesnaście ",
    "siedemnaście ", "osiemnaście ", "dziewiętnaście ")
  val dozens = Array("", "dziesięć ", "dwadzieścia ", "trzydzieści ", "czterdzieści ", "pięćdziesiąt ",
    "sześćdziesiąt ", "siedemdziesiąt ", "osiemdziesiąt ", "dziewięćdziesiąt ")
  val hundreds = Array("", "sto ", "dwieście ", "trzysta ", "czterysta ", "pięćset ", "sześćset ", "siedemset ",
    "osiemset ", "dziewięćset ")
  val groups = Array(Array("", "", ""),
    Array("tysiąc ", "tysiące ", "tysięcy "),
    Array("milion ", "miliony ", "milionów "),
    Array("miliard ", "miliardy ", "miliardów "),
    Array("bilion ", "biliony ", "bilionów "),
    Array("biliard ", "biliardy ", "biliardów "),
    Array("trylion ", "tryliony ", "tryliardów "))

  def toCurrency(amount: BigDecimal): String ={
    val decimalFormat = new DecimalFormat("0.00")
    decimalFormat.setGroupingUsed(true)
    decimalFormat.setGroupingSize(3)
    decimalFormat.format(amount)
  }

  def getCurrencyInWords(amount: BigDecimal, currency: Currency = Currency.PLN):String =  {
    currency match {
      case Currency.PLN =>
        s"${convertNumberToWords(amount.toLong)} ${zlVariant(amount.toLong)} ${(amount.remainder(1.0)*100).abs.toBigInt}/100 groszy"
      case _ => ???
    }
  }
  private def convertNumberToWords(number: Long): String = {
    if (number < 0) throw new IllegalArgumentException("The number should be positive")

    if (number == 0) return "zero"

    var numberToConvert = number
    var numberInWords = ""

    var u = 0 // unit
    var t = 0 // ten
    var d = 0 // dozen
    var h = 0 // hundred
    var g = 0 // group
    var e = 0 // endings
    var c = 0 // correction

    while (numberToConvert != 0) {
      h = (numberToConvert % 1000 / 100).toInt
      d = (numberToConvert % 100 / 10).toInt
      u = (numberToConvert % 10).toInt

      // if dealing with tens
      if (d == 1 & u > 0) {
        t = u
        d = 0
        u = 0
      } else {
        t = 0
      }

      // endings
      if (u == 1 & h + d + t == 0) {
        e = 0
      } else if (u == 2) {
        e = 1
      } else if (u == 3) {
        e = 1
      } else if (u == 4) {
        e = 1
      } else {
        e = 2
      }

      // minor amendment - that did not return empty numbers (values)
      c = 0
      if (h == 0 & d == 0 & t==0 & u == 0) {
        c = g
        g = 0
        e = 0
      }

//      println(s"h:$h d:$d u:$u g:$g e:$e c:$c")
      numberInWords = hundreds(h) + dozens(d) + tens(t) + unity(u) + groups(g)(e) + numberInWords

      // get rid of these numbers we have already reworked
      numberToConvert = numberToConvert / 1000
      // and increasing g which is responsible for the number of fields in a multidimensional array
      g = g + 1 + c
    }

    numberInWords.trim
  }

  private def zlVariant(numberZl: Long): String = {
    numberZl match {
      case 1 => "złoty"
      case 2 => "złote"
      case 3 => "złote"
      case 4 => "złote"
      case _ => "złotych"
    }
  }
}
