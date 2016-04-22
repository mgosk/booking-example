package gus.model

abstract class GusReportType(val code: String) {
  override def toString: String = this.code
}

object GusReportType {

  case object PublDaneRaportDzialalnoscFizycznejCeidg extends GusReportType("PublDaneRaportDzialalnoscFizycznejCeidg")

  case object PublDaneRaportDzialalnoscFizycznejRolnicza extends GusReportType("PublDaneRaportDzialalnoscFizycznejRolnicza")

  case object PublDaneRaportDzialalnoscFizycznejPozostala extends GusReportType("PublDaneRaportDzialalnoscFizycznejPozostala")

  case object PublDaneRaportDzialalnoscFizycznejWKrupgn extends GusReportType("PublDaneRaportDzialalnoscFizycznejWKrupgn")

  case object PublDaneRaportPrawna extends GusReportType("PublDaneRaportPrawna")

}
