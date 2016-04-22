package gus

import javax.inject.{Inject, Singleton}

import gus.model.{GusReport, GusReportType, SzukajReponse}
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.xml.{Elem, XML}

@Singleton
class GusGateway @Inject()(ws: WSClient, configuration: Configuration) {

  lazy val key = configuration.getString(path = "gus.key").get
  lazy val endpointAddress = configuration.getString(path = "gus.endpointAddress").get
  lazy val requests = new Requests(endpointAddress, key)

  def daneSzukaj(sid: String, nip: String): Future[Option[SzukajReponse]] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.daneSzukaj(nip)).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "DaneSzukajResponse" \ "DaneSzukajResult").text
      if (extracted.length > 0) {
        val rootXML: Elem = XML.loadString(extracted)
        val obj = SzukajReponse(
          regon = (rootXML \ "dane" \ "Regon").text,
          nazwa = (rootXML \ "dane" \ "Nazwa").text,
          wojewodztwo = (rootXML \ "dane" \ "Wojewodztwo").text,
          powiat = (rootXML \ "dane" \ "Powiat").text,
          gmina = (rootXML \ "dane" \ "Gmina").text,
          miejscowosc = (rootXML \ "dane" \ "Miejscowosc").text,
          kodPocztowy = (rootXML \ "dane" \ "KodPocztowy").text,
          ulica = (rootXML \ "dane" \ "Ulica").text,
          typ = (rootXML \ "dane" \ "Typ").text,
          silosID = (rootXML \ "dane" \ "SilosID").text)
        Some(obj)
      } else {
        None
      }
    }
  }

  def pobierzPelnyRaport(sid: String, regon: String, reportType: GusReportType): Future[Option[GusReport]] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.danePobierzPelnyRaport(regon, reportType)).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "DanePobierzPelnyRaportResponse" \ "DanePobierzPelnyRaportResult").text
      Logger.info(extracted)
      if (extracted.length > 0) {
        Some(GusReport.fromXML(XML.loadString(extracted), reportType))
      } else {
        None
      }
    }
  }

  //SID and captcha management
  def zaloguj(): Future[String] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8")
      .post(requests.zaloguj).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "ZalogujResponse" \ "ZalogujResult").text
      if (extracted.length < 1) {
        Logger.error(s"SidBody:$response")
        Logger.error(s"SidXML:$xml")
      }
      extracted
    }
  }

  def getSearchStatus(sid: String): Future[String] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.getValue("KomunikatKod")).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "GetValueResponse" \ "GetValueResult").text
      if (extracted.length < 1) {
        Logger.error(s"StatusBody:$response")
        Logger.error(s"StatusXML:$xml")
      }
      extracted
    }
  }

  def getSessionStatus(sid: String): Future[String] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.getValue("StatusSesji")).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "GetValueResponse" \ "GetValueResult").text
      if (extracted.length < 1) {
        Logger.error(s"StatusBody:$response")
        Logger.error(s"StatusXML:$xml")
      }
      extracted
    }
  }

  def pobierzCaptcha(sid: String): Future[String] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.pobierzCaptcha).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "PobierzCaptchaResponse" \ "PobierzCaptchaResult").text
      extracted
    }
  }

  def sprawdzCaptcha(sid: String, captcha: String): Future[String] = {
    ws.url(s"$endpointAddress")
      .withHeaders("Content-Type" -> "application/soap+xml; charset=utf-8", "sid" -> sid)
      .post(requests.sprawdzCaptcha(captcha)).map { response =>
      val string = response.body.split("\r\n").filter(_.startsWith("<s:Envelope")).head
      val xml: Elem = XML.loadString(string)
      val extracted = (xml \ "Body" \ "SprawdzCaptchaResponse" \ "SprawdzCaptchaResult").text
      Logger.info(s"CaptchaStatus:$extracted")
      extracted
    }
  }
}
