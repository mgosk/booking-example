package gus.model

import auth.model.CompanyForm
import gus.model.GusReportType.PublDaneRaportDzialalnoscFizycznejWKrupgn
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

case class PublDaneRaportDzialalnoscFizycznejWKrupgnReport(fiz_regon9: String = "",
  fiz_nazwa: String = "",
  fiz_nazwaSkrocon: String = "",
  fiz_dataPowstania: String = "",
  fiz_dataRozpoczeciaDzialalnosci: String = "",
  fiz_dataWpisuDoREGONDzialalnosci: String = "",
  fiz_dataZawieszeniaDzialalnosc: String = "",
  fiz_dataWznowieniaDzialalnosci: String = "",
  fiz_dataZaistnieniaZmianyDzialalnosci: String = "",
  fiz_dataZakonczeniaDzialalnosci: String = "",
  fiz_dataSkresleniazRegonDzialalnosci: String = "",
  fiz_adSiedzKraj_Symbol: String = "",
  fiz_adSiedzWojewodztwo_Symbol: String = "",
  fiz_adSiedzPowiat_Symbol: String = "",
  fiz_adSiedzGmina_Symbol: String = "",
  fiz_adSiedzKodPocztowy: String = "",
  fiz_adSiedzMiejscowoscPoczty_Symbol: String = "",
  fiz_adSiedzMiejscowosc_Symbol: String = "",
  fiz_adSiedzUlica_Symbol: String = "",
  fiz_adSiedzNumerNieruchomosci: String = "",
  fiz_adSiedzNumerLokalu: String = "",
  fiz_adSiedzNietypoweMiejsceLokalizacji: String = "",
  fiz_numerTelefonu: String = "",
  fiz_numerWewnetrznyTelefonu: String = "",
  fiz_numerFaksu: String = "",
  fiz_adresEmail: String = "",
  fiz_adresStronyinternetowej: String = "",
  fiz_adresEmail2: String = "",
  fiz_adSiedzKraj_Nazwa: String = "",
  fiz_adSiedzWojewodztwo_Nazwa: String = "",
  fiz_adSiedzPowiat_Nazwa: String = "",
  fiz_adSiedzGmina_Nazwa: String = "",
  fiz_adSiedzMiejscowosc_Nazwa: String = "",
  fiz_adSiedzMiejscowoscPoczty_Nazwa: String = "",
  fiz_adSiedzUlica_Nazwa: String = "") extends GusReport(PublDaneRaportDzialalnoscFizycznejWKrupgn) {

  override def name: String = this.fiz_nazwa

  override def postalCode: String = this.fiz_adSiedzKodPocztowy

  override def startDate: DateTime = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(this.fiz_dataRozpoczeciaDzialalnosci)

  override def district: String = this.fiz_adSiedzPowiat_Nazwa

  override def city: String = this.fiz_adSiedzMiejscowosc_Nazwa

  override def creationDate: DateTime = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(this.fiz_dataPowstania)

  override def buildingNr: String = this.fiz_adSiedzNumerNieruchomosci

  override def untypicalPlace: String = this.fiz_adSiedzNietypoweMiejsceLokalizacji

  override def country: String = this.fiz_adSiedzKraj_Nazwa

  override def regon: String = this.fiz_regon9

  override def apartmentNr: String = this.fiz_adSiedzNumerLokalu

  override def postalCity: String = this.fiz_adSiedzMiejscowoscPoczty_Nazwa

  override def voivodeship: String = this.fiz_adSiedzWojewodztwo_Nazwa

  override def commune: String = this.fiz_adSiedzGmina_Nazwa

  override def street: String = this.fiz_adSiedzUlica_Nazwa

  override def organizationForm: String = CompanyForm.FizycznaDzialalnosc.code
}

