package gus.model

import auth.model.CompanyForm
import gus.model.GusReportType.PublDaneRaportPrawna
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

case class PublDaneRaportPrawnaReport(
  praw_regon14: String = "",
  praw_nip: String = "",
  praw_nazwa: String = "",
  praw_nazwaSkrocona: String = "",
  praw_numerWrejestrzeEwidencji: String = "",
  praw_dataPowstania: String = "",
  praw_dataRozpoczeciaDzialalnosci: String = "",
  praw_dataWpisuDoREGON: String = "",
  praw_dataZawieszeniaDzialalnosci: String = "",
  praw_dataWznowieniaDzialalnosci: String = "",
  praw_dataZaistnieniaZmiany: String = "",
  praw_dataZakonczeniaDzialalnosci: String = "",
  praw_dataSkresleniazRegon: String = "",
  praw_adSiedzKraj_Symbol: String = "",
  praw_adSiedzWojewodztwo_Symbol: String = "",
  praw_adSiedzPowiat_Symbol: String = "",
  praw_adSiedzGmina_Symbol: String = "",
  praw_adSiedzKodPocztowy: String = "",
  praw_adSiedzMiejscowoscPoczty_Symbol: String = "",
  praw_adSiedzMiejscowosc_Symbol: String = "",
  praw_adSiedzUlica_Symbol: String = "",
  praw_adSiedzNumerNieruchomosci: String = "",
  praw_adSiedzNumerLokalu: String = "",
  praw_adSiedzNietypoweMiejsceLokalizacji: String = "",
  praw_numerTelefonu: String = "",
  praw_numerWewnetrznyTelefonu: String = "",
  praw_numerFaksu: String = "",
  praw_adresEmail: String = "",
  praw_adresStronyinternetowej: String = "",
  praw_adresEmail2: String = "",
  praw_adKorKraj_Symbol: String = "",
  praw_adKorWojewodztwo_Symbol: String = "",
  praw_adKorPowiat_Symbol: String = "",
  praw_adKorGmina_Symbol: String = "",
  praw_adKorKodPocztowy: String = "",
  praw_adKorMiejscowoscPoczty_Symbol: String = "",
  praw_adKorMiejscowosc_Symbol: String = "",
  praw_adKorUlica_Symbol: String = "",
  praw_adKorNumerNieruchomosci: String = "",
  praw_adKorNumerLokalu: String = "",
  praw_adKorNietypoweMiejsceLokalizacji: String = "",
  praw_adKorNazwaPodmiotuDoKorespondencji: String = "",
  praw_adSiedzKraj_Nazwa: String = "",
  praw_adSiedzWojewodztwo_Nazwa: String = "",
  praw_adSiedzPowiat_Nazwa: String = "",
  praw_adSiedzGmina_Nazwa: String = "",
  praw_adSiedzMiejscowosc_Nazwa: String = "",
  praw_adSiedzMiejscowoscPoczty_Nazwa: String = "",
  praw_adSiedzUlica_Nazwa: String = "",
  praw_adKorKraj_Nazwa: String = "",
  praw_adKorWojewodztwo_Nazwa: String = "",
  praw_adKorPowiat_Nazwa: String = "",
  praw_adKorGmina_Nazwa: String = "",
  praw_adKorMiejscowosc_Nazwa: String = "",
  praw_adKorMiejscowoscPoczty_Nazwa: String = "",
  praw_adKorUlica_Nazwa: String = "",
  praw_podstawowaFormaPrawna_Symbol: String = "",
  praw_szczegolnaFormaPrawna_Symbol: String = "",
  praw_formaFinansowania_Symbol: String = "",
  praw_formaWlasnosci_Symbol: String = "",
  praw_organZalozycielski_Symbol: String = "",
  praw_organRejestrowy_Symbol: String = "",
  praw_rodzajRejestruEwidencji_Symbol: String = "",
  praw_podstawowaFormaPrawna_Nazwa: String = "",
  praw_szczegolnaFormaPrawna_Nazwa: String = "",
  praw_formaFinansowania_Nazwa: String = "",
  praw_formaWlasnosci_Nazwa: String = "",
  praw_organZalozycielski_Nazwa: String = "",
  praw_organRejestrowy_Nazwa: String = "",
  praw_rodzajRejestruEwidencji_Nazwa: String = "",
  praw_jednostekLokalnych: String = "") extends GusReport(PublDaneRaportPrawna) {

  override def name: String = this.praw_nazwa

  override def postalCode: String = this.praw_adSiedzKodPocztowy

  override def organizationForm: String = CompanyForm.Prawna.code

  override def startDate: DateTime = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(this.praw_dataRozpoczeciaDzialalnosci)

  override def district: String = this.praw_adSiedzPowiat_Nazwa

  override def city: String = this.praw_adSiedzMiejscowosc_Nazwa

  override def creationDate: DateTime = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(this.praw_dataPowstania)

  override def buildingNr: String = this.praw_adSiedzNumerNieruchomosci

  override def untypicalPlace: String = this.praw_adKorNietypoweMiejsceLokalizacji

  override def country: String = this.praw_adSiedzKraj_Nazwa

  override def regon: String = this.praw_regon14

  override def apartmentNr: String = this.praw_adSiedzNumerLokalu

  override def postalCity: String = this.praw_adSiedzMiejscowoscPoczty_Nazwa

  override def voivodeship: String = this.praw_adSiedzWojewodztwo_Nazwa

  override def commune: String = this.praw_adSiedzGmina_Nazwa

  override def street: String = this.praw_adSiedzUlica_Nazwa
}
