package gus.model

import gus.model.GusReportType._
import org.joda.time.DateTime

import scala.xml.Elem

abstract class GusReport(val reportType: GusReportType) {

  def name: String

  def regon: String

  def country: String

  def voivodeship: String

  def district: String

  def commune: String

  def city: String

  def postalCode: String

  def postalCity: String

  def street: String

  def buildingNr: String

  def apartmentNr: String

  def untypicalPlace: String

  def creationDate: DateTime

  def startDate: DateTime

  def organizationForm: String

  def address1: String = {
    val apartmentAppendix = if (apartmentNr.isEmpty) "" else s" lok. $apartmentNr"
    if (street.isEmpty) {
      s"$city $buildingNr$apartmentAppendix"
    } else {
      s"$street $buildingNr$apartmentAppendix"
    }
  }
}

object GusReport {

  def fromXML(rootXML: Elem, reportType: GusReportType): GusReport =
    reportType match {
      case PublDaneRaportDzialalnoscFizycznejCeidg =>
        PublDaneRaportDzialalnoscFizycznejCeidgReport(
          fiz_regon9 = (rootXML \ "dane" \ "fiz_regon9").text,
          fiz_nazwa = (rootXML \ "dane" \ "fiz_nazwa").text,
          fiz_nazwaSkrocon = (rootXML \ "dane" \ "fiz_nazwaSkrocon").text,
          fiz_dataPowstania = (rootXML \ "dane" \ "fiz_dataPowstania").text,
          fiz_dataRozpoczeciaDzialalnosci = (rootXML \ "dane" \ "fiz_dataRozpoczeciaDzialalnosci").text,
          fiz_dataWpisuDoREGONDzialalnosci = (rootXML \ "dane" \ "fiz_dataWpisuDoREGONDzialalnosci").text,
          fiz_dataZawieszeniaDzialalnosc = (rootXML \ "dane" \ "fiz_dataZawieszeniaDzialalnosc").text,
          fiz_dataWznowieniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataWznowieniaDzialalnosci").text,
          fiz_dataZaistnieniaZmianyDzialalnosci = (rootXML \ "dane" \ "fiz_dataZaistnieniaZmianyDzialalnosci").text,
          fiz_dataZakonczeniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataZakonczeniaDzialalnosci").text,
          fiz_dataSkresleniazRegonDzialalnosci = (rootXML \ "dane" \ "fiz_dataSkresleniazRegonDzialalnosci").text,
          fiz_adSiedzKraj_Symbol = (rootXML \ "dane" \ "fiz_adSiedzKraj_Symbol").text,
          fiz_adSiedzWojewodztwo_Symbol = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Symbol").text,
          fiz_adSiedzPowiat_Symbol = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Symbol").text,
          fiz_adSiedzGmina_Symbol = (rootXML \ "dane" \ "fiz_adSiedzGmina_Symbol").text,
          fiz_adSiedzNumerNieruchomosci = (rootXML \ "dane" \ "fiz_adSiedzNumerNieruchomosci").text,
          fiz_adSiedzKodPocztowy = (rootXML \ "dane" \ "fiz_adSiedzKodPocztowy").text,
          fiz_adSiedzNumerLokalu = (rootXML \ "dane" \ "fiz_adSiedzNumerLokalu").text,
          fiz_adSiedzNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "fiz_adSiedzNietypoweMiejsceLokalizacji").text,
          fiz_numerTelefonu = (rootXML \ "dane" \ "fiz_numerTelefonu").text,
          fiz_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "fiz_numerWewnetrznyTelefonu").text,
          fiz_numerFaksu = (rootXML \ "dane" \ "fiz_numerFaksu").text,
          fiz_adresEmail = (rootXML \ "dane" \ "fiz_adresEmail").text,
          fiz_adresStronyinternetowej = (rootXML \ "dane" \ "fiz_adresStronyinternetowej").text,
          fiz_adresEmail2 = (rootXML \ "dane" \ "fiz_adresEmail2").text,
          fiz_adSiedzKraj_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzKraj_Nazwa").text,
          fiz_adSiedzWojewodztwo_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Nazwa").text,
          fiz_adSiedzPowiat_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Nazwa").text,
          fiz_adSiedzGmina_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzGmina_Nazwa").text,
          fiz_adSiedzMiejscowosc_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowosc_Nazwa").text,
          fiz_adSiedzMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowoscPoczty_Nazwa").text,
          fiz_adSiedzUlica_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzUlica_Nazwa").text,
          fizC_dataWpisuDoRejestruEwidencji = (rootXML \ "dane" \ "fizC_dataWpisuDoRejestruEwidencji").text,
          fizC_numerwRejestrzeEwidencji = (rootXML \ "dane" \ "fizC_numerwRejestrzeEwidencji").text,
          fizC_OrganRejestrowy_Symbol = (rootXML \ "dane" \ "fizC_OrganRejestrowy_Symbol").text,
          fizC_OrganRejestrowy_Nazwa = (rootXML \ "dane" \ "fizC_OrganRejestrowy_Nazwa").text,
          fizC_RodzajRejestru_Symbol = (rootXML \ "dane" \ "fizC_RodzajRejestru_Symbol").text,
          fizC_RodzajRejestru_Nazwa = (rootXML \ "dane" \ "fizC_RodzajRejestru_Nazwa").text,
          fizC_numerTelefonu = (rootXML \ "dane" \ "fizC_numerTelefonu").text,
          fizC_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "fizC_numerWewnetrznyTelefonu").text,
          fizC_numerFaksu = (rootXML \ "dane" \ "fizC_numerFaksu").text,
          fizC_adresEmail = (rootXML \ "dane" \ "fizC_adresEmail").text,
          fizC_adresStronyInternetowej = (rootXML \ "dane" \ "fizC_adresStronyInternetowej").text)
      case PublDaneRaportDzialalnoscFizycznejRolnicza =>
        PublDaneRaportDzialalnoscFizycznejRolniczaReport(
          fiz_regon9 = (rootXML \ "dane" \ "fiz_regon9").text,
          fiz_nazwa = (rootXML \ "dane" \ "fiz_nazwa").text,
          fiz_nazwaSkrocon = (rootXML \ "dane" \ "fiz_nazwaSkrocon").text,
          fiz_dataPowstania = (rootXML \ "dane" \ "fiz_dataPowstania").text,
          fiz_dataRozpoczeciaDzialalnosci = (rootXML \ "dane" \ "fiz_dataRozpoczeciaDzialalnosci").text,
          fiz_dataWpisuDoREGONDzialalnosci = (rootXML \ "dane" \ "fiz_dataWpisuDoREGONDzialalnosci").text,
          fiz_dataZawieszeniaDzialalnosc = (rootXML \ "dane" \ "fiz_dataZawieszeniaDzialalnosc").text,
          fiz_dataWznowieniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataWznowieniaDzialalnosci").text,
          fiz_dataZaistnieniaZmianyDzialalnosci = (rootXML \ "dane" \ "fiz_dataZaistnieniaZmianyDzialalnosci").text,
          fiz_dataZakonczeniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataZakonczeniaDzialalnosci").text,
          fiz_dataSkresleniazRegonDzialalnosci = (rootXML \ "dane" \ "fiz_dataSkresleniazRegonDzialalnosci").text,
          fiz_adSiedzKraj_Symbol = (rootXML \ "dane" \ "fiz_adSiedzKraj_Symbol").text,
          fiz_adSiedzWojewodztwo_Symbol = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Symbol").text,
          fiz_adSiedzPowiat_Symbol = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Symbol").text,
          fiz_adSiedzGmina_Symbol = (rootXML \ "dane" \ "fiz_adSiedzGmina_Symbol").text,
          fiz_adSiedzNumerNieruchomosci = (rootXML \ "dane" \ "fiz_adSiedzNumerNieruchomosci").text,
          fiz_adSiedzKodPocztowy = (rootXML \ "dane" \ "fiz_adSiedzKodPocztowy").text,
          fiz_adSiedzNumerLokalu = (rootXML \ "dane" \ "fiz_adSiedzNumerLokalu").text,
          fiz_adSiedzNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "fiz_adSiedzNietypoweMiejsceLokalizacji").text,
          fiz_numerTelefonu = (rootXML \ "dane" \ "fiz_numerTelefonu").text,
          fiz_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "fiz_numerWewnetrznyTelefonu").text,
          fiz_numerFaksu = (rootXML \ "dane" \ "fiz_numerFaksu").text,
          fiz_adresEmail = (rootXML \ "dane" \ "fiz_adresEmail").text,
          fiz_adresStronyinternetowej = (rootXML \ "dane" \ "fiz_adresStronyinternetowej").text,
          fiz_adresEmail2 = (rootXML \ "dane" \ "fiz_adresEmail2").text,
          fiz_adSiedzKraj_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzKraj_Nazwa").text,
          fiz_adSiedzWojewodztwo_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Nazwa").text,
          fiz_adSiedzPowiat_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Nazwa").text,
          fiz_adSiedzGmina_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzGmina_Nazwa").text,
          fiz_adSiedzMiejscowosc_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowosc_Nazwa").text,
          fiz_adSiedzMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowoscPoczty_Nazwa").text,
          fiz_adSiedzUlica_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzUlica_Nazwa").text)
      case PublDaneRaportDzialalnoscFizycznejPozostala =>
        PublDaneRaportDzialalnoscFizycznejPozostalaReport(
          fiz_regon9 = (rootXML \ "dane" \ "fiz_regon9").text,
          fiz_nazwa = (rootXML \ "dane" \ "fiz_nazwa").text,
          fiz_nazwaSkrocon = (rootXML \ "dane" \ "fiz_nazwaSkrocon").text,
          fiz_dataPowstania = (rootXML \ "dane" \ "fiz_dataPowstania").text,
          fiz_dataRozpoczeciaDzialalnosci = (rootXML \ "dane" \ "fiz_dataRozpoczeciaDzialalnosci").text,
          fiz_dataWpisuDoREGONDzialalnosci = (rootXML \ "dane" \ "fiz_dataWpisuDoREGONDzialalnosci").text,
          fiz_dataZawieszeniaDzialalnosc = (rootXML \ "dane" \ "fiz_dataZawieszeniaDzialalnosc").text,
          fiz_dataWznowieniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataWznowieniaDzialalnosci").text,
          fiz_dataZaistnieniaZmianyDzialalnosci = (rootXML \ "dane" \ "fiz_dataZaistnieniaZmianyDzialalnosci").text,
          fiz_dataZakonczeniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataZakonczeniaDzialalnosci").text,
          fiz_dataSkresleniazRegonDzialalnosci = (rootXML \ "dane" \ "fiz_dataSkresleniazRegonDzialalnosci").text,
          fiz_adSiedzKraj_Symbol = (rootXML \ "dane" \ "fiz_adSiedzKraj_Symbol").text,
          fiz_adSiedzWojewodztwo_Symbol = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Symbol").text,
          fiz_adSiedzPowiat_Symbol = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Symbol").text,
          fiz_adSiedzGmina_Symbol = (rootXML \ "dane" \ "fiz_adSiedzGmina_Symbol").text,
          fiz_adSiedzNumerNieruchomosci = (rootXML \ "dane" \ "fiz_adSiedzNumerNieruchomosci").text,
          fiz_adSiedzKodPocztowy = (rootXML \ "dane" \ "fiz_adSiedzKodPocztowy").text,
          fiz_adSiedzNumerLokalu = (rootXML \ "dane" \ "fiz_adSiedzNumerLokalu").text,
          fiz_adSiedzNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "fiz_adSiedzNietypoweMiejsceLokalizacji").text,
          fiz_numerTelefonu = (rootXML \ "dane" \ "fiz_numerTelefonu").text,
          fiz_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "fiz_numerWewnetrznyTelefonu").text,
          fiz_numerFaksu = (rootXML \ "dane" \ "fiz_numerFaksu").text,
          fiz_adresEmail = (rootXML \ "dane" \ "fiz_adresEmail").text,
          fiz_adresStronyinternetowej = (rootXML \ "dane" \ "fiz_adresStronyinternetowej").text,
          fiz_adresEmail2 = (rootXML \ "dane" \ "fiz_adresEmail2").text,
          fiz_adSiedzKraj_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzKraj_Nazwa").text,
          fiz_adSiedzWojewodztwo_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Nazwa").text,
          fiz_adSiedzPowiat_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Nazwa").text,
          fiz_adSiedzGmina_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzGmina_Nazwa").text,
          fiz_adSiedzMiejscowosc_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowosc_Nazwa").text,
          fiz_adSiedzMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowoscPoczty_Nazwa").text,
          fiz_adSiedzUlica_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzUlica_Nazwa").text,
          fizP_dataWpisuDoRejestruEwidencji = (rootXML \ "dane" \ "fizP_dataWpisuDoRejestruEwidencji").text,
          fizP_numerwRejestrzeEwidencji = (rootXML \ "dane" \ "fizP_numerwRejestrzeEwidencji").text,
          fizP_OrganRejestrowy_Symbol = (rootXML \ "dane" \ "fizP_OrganRejestrowy_Symbol").text,
          fizP_OrganRejestrowy_Nazwa = (rootXML \ "dane" \ "fizP_OrganRejestrowy_Nazwa").text,
          fizP_RodzajRejestru_Symbol = (rootXML \ "dane" \ "fizP_RodzajRejestru_Symbol").text,
          fizP_RodzajRejestru_Nazwa = (rootXML \ "dane" \ "fizP_RodzajRejestru_Nazwa").text)
      case PublDaneRaportDzialalnoscFizycznejWKrupgn =>
        PublDaneRaportDzialalnoscFizycznejWKrupgnReport(
          fiz_regon9 = (rootXML \ "dane" \ "fiz_regon9").text,
          fiz_nazwa = (rootXML \ "dane" \ "fiz_nazwa").text,
          fiz_nazwaSkrocon = (rootXML \ "dane" \ "fiz_nazwaSkrocon").text,
          fiz_dataPowstania = (rootXML \ "dane" \ "fiz_dataPowstania").text,
          fiz_dataRozpoczeciaDzialalnosci = (rootXML \ "dane" \ "fiz_dataRozpoczeciaDzialalnosci").text,
          fiz_dataWpisuDoREGONDzialalnosci = (rootXML \ "dane" \ "fiz_dataWpisuDoREGONDzialalnosci").text,
          fiz_dataZawieszeniaDzialalnosc = (rootXML \ "dane" \ "fiz_dataZawieszeniaDzialalnosc").text,
          fiz_dataWznowieniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataWznowieniaDzialalnosci").text,
          fiz_dataZaistnieniaZmianyDzialalnosci = (rootXML \ "dane" \ "fiz_dataZaistnieniaZmianyDzialalnosci").text,
          fiz_dataZakonczeniaDzialalnosci = (rootXML \ "dane" \ "fiz_dataZakonczeniaDzialalnosci").text,
          fiz_dataSkresleniazRegonDzialalnosci = (rootXML \ "dane" \ "fiz_dataSkresleniazRegonDzialalnosci").text,
          fiz_adSiedzKraj_Symbol = (rootXML \ "dane" \ "fiz_adSiedzKraj_Symbol").text,
          fiz_adSiedzWojewodztwo_Symbol = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Symbol").text,
          fiz_adSiedzPowiat_Symbol = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Symbol").text,
          fiz_adSiedzGmina_Symbol = (rootXML \ "dane" \ "fiz_adSiedzGmina_Symbol").text,
          fiz_adSiedzNumerNieruchomosci = (rootXML \ "dane" \ "fiz_adSiedzNumerNieruchomosci").text,
          fiz_adSiedzKodPocztowy = (rootXML \ "dane" \ "fiz_adSiedzKodPocztowy").text,
          fiz_adSiedzNumerLokalu = (rootXML \ "dane" \ "fiz_adSiedzNumerLokalu").text,
          fiz_adSiedzNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "fiz_adSiedzNietypoweMiejsceLokalizacji").text,
          fiz_numerTelefonu = (rootXML \ "dane" \ "fiz_numerTelefonu").text,
          fiz_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "fiz_numerWewnetrznyTelefonu").text,
          fiz_numerFaksu = (rootXML \ "dane" \ "fiz_numerFaksu").text,
          fiz_adresEmail = (rootXML \ "dane" \ "fiz_adresEmail").text,
          fiz_adresStronyinternetowej = (rootXML \ "dane" \ "fiz_adresStronyinternetowej").text,
          fiz_adresEmail2 = (rootXML \ "dane" \ "fiz_adresEmail2").text,
          fiz_adSiedzKraj_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzKraj_Nazwa").text,
          fiz_adSiedzWojewodztwo_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzWojewodztwo_Nazwa").text,
          fiz_adSiedzPowiat_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzPowiat_Nazwa").text,
          fiz_adSiedzGmina_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzGmina_Nazwa").text,
          fiz_adSiedzMiejscowosc_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowosc_Nazwa").text,
          fiz_adSiedzMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzMiejscowoscPoczty_Nazwa").text,
          fiz_adSiedzUlica_Nazwa = (rootXML \ "dane" \ "fiz_adSiedzUlica_Nazwa").text)
      case PublDaneRaportPrawna => PublDaneRaportPrawnaReport(
        praw_regon14 = (rootXML \ "dane" \ "praw_regon14").text,
        praw_nip = (rootXML \ "dane" \ "praw_nip").text,
        praw_nazwa = (rootXML \ "dane" \ "praw_nazwa").text,
        praw_nazwaSkrocona = (rootXML \ "dane" \ "praw_nazwaSkrocona").text,
        praw_numerWrejestrzeEwidencji = (rootXML \ "dane" \ "praw_numerWrejestrzeEwidencji").text,
        praw_dataPowstania = (rootXML \ "dane" \ "praw_dataPowstania").text,
        praw_dataRozpoczeciaDzialalnosci = (rootXML \ "dane" \ "praw_dataRozpoczeciaDzialalnosci").text,
        praw_dataWpisuDoREGON = (rootXML \ "dane" \ "praw_dataWpisuDoREGON").text,
        praw_dataZawieszeniaDzialalnosci = (rootXML \ "dane" \ "praw_dataZawieszeniaDzialalnosci").text,
        praw_dataWznowieniaDzialalnosci = (rootXML \ "dane" \ "praw_dataWznowieniaDzialalnosci").text,
        praw_dataZaistnieniaZmiany = (rootXML \ "dane" \ "praw_dataZaistnieniaZmiany").text,
        praw_dataZakonczeniaDzialalnosci = (rootXML \ "dane" \ "praw_dataZakonczeniaDzialalnosci").text,
        praw_dataSkresleniazRegon = (rootXML \ "dane" \ "praw_dataSkresleniazRegon").text,
        praw_adSiedzKraj_Symbol = (rootXML \ "dane" \ "praw_adSiedzKraj_Symbol").text,
        praw_adSiedzWojewodztwo_Symbol = (rootXML \ "dane" \ "fizC_adresEmail").text,
        praw_adSiedzPowiat_Symbol = (rootXML \ "dane" \ "praw_adSiedzPowiat_Symbol").text,
        praw_adSiedzGmina_Symbol = (rootXML \ "dane" \ "praw_adSiedzGmina_Symbol").text,
        praw_adSiedzKodPocztowy = (rootXML \ "dane" \ "praw_adSiedzKodPocztowy").text,
        praw_adSiedzMiejscowoscPoczty_Symbol = (rootXML \ "dane" \ "praw_adSiedzMiejscowoscPoczty_Symbol").text,
        praw_adSiedzMiejscowosc_Symbol = (rootXML \ "dane" \ "praw_adSiedzMiejscowosc_Symbol").text,
        praw_adSiedzUlica_Symbol = (rootXML \ "dane" \ "praw_adSiedzUlica_Symbol").text,
        praw_adSiedzNumerNieruchomosci = (rootXML \ "dane" \ "praw_adSiedzNumerNieruchomosci").text,
        praw_adSiedzNumerLokalu = (rootXML \ "dane" \ "praw_adSiedzNumerLokalu").text,
        praw_adSiedzNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "praw_adSiedzNietypoweMiejsceLokalizacji").text,
        praw_numerTelefonu = (rootXML \ "dane" \ "praw_numerTelefonu").text,
        praw_numerWewnetrznyTelefonu = (rootXML \ "dane" \ "praw_numerWewnetrznyTelefonu").text,
        praw_numerFaksu = (rootXML \ "dane" \ "praw_numerFaksu").text,
        praw_adresEmail = (rootXML \ "dane" \ "praw_adresEmail").text,
        praw_adresStronyinternetowej = (rootXML \ "dane" \ "praw_adresStronyinternetowej").text,
        praw_adresEmail2 = (rootXML \ "dane" \ "praw_adresEmail2").text,
        praw_adKorKraj_Symbol = (rootXML \ "dane" \ "praw_adKorKraj_Symbol").text,
        praw_adKorWojewodztwo_Symbol = (rootXML \ "dane" \ "praw_adKorWojewodztwo_Symbol").text,
        praw_adKorPowiat_Symbol = (rootXML \ "dane" \ "praw_adKorPowiat_Symbol").text,
        praw_adKorGmina_Symbol = (rootXML \ "dane" \ "praw_adKorGmina_Symbol").text,
        praw_adKorKodPocztowy = (rootXML \ "dane" \ "praw_adKorKodPocztowy").text,
        praw_adKorMiejscowoscPoczty_Symbol = (rootXML \ "dane" \ "praw_adKorMiejscowoscPoczty_Symbol").text,
        praw_adKorMiejscowosc_Symbol = (rootXML \ "dane" \ "praw_adKorMiejscowosc_Symbol").text,
        praw_adKorUlica_Symbol = (rootXML \ "dane" \ "praw_adKorUlica_Symbol").text,
        praw_adKorNumerNieruchomosci = (rootXML \ "dane" \ "praw_adKorNumerNieruchomosci").text,
        praw_adKorNumerLokalu = (rootXML \ "dane" \ "praw_adKorNumerLokalu").text,
        praw_adKorNietypoweMiejsceLokalizacji = (rootXML \ "dane" \ "praw_adKorNietypoweMiejsceLokalizacji").text,
        praw_adKorNazwaPodmiotuDoKorespondencji = (rootXML \ "dane" \ "praw_adKorNazwaPodmiotuDoKorespondencji").text,
        praw_adSiedzKraj_Nazwa = (rootXML \ "dane" \ "praw_adSiedzKraj_Nazwa").text,
        praw_adSiedzWojewodztwo_Nazwa = (rootXML \ "dane" \ "praw_adSiedzWojewodztwo_Nazwa").text,
        praw_adSiedzPowiat_Nazwa = (rootXML \ "dane" \ "praw_adSiedzPowiat_Nazwa").text,
        praw_adSiedzGmina_Nazwa = (rootXML \ "dane" \ "praw_adSiedzGmina_Nazwa").text,
        praw_adSiedzMiejscowosc_Nazwa = (rootXML \ "dane" \ "praw_adSiedzMiejscowosc_Nazwa").text,
        praw_adSiedzMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "praw_adSiedzMiejscowoscPoczty_Nazwa").text,
        praw_adSiedzUlica_Nazwa = (rootXML \ "dane" \ "praw_adSiedzUlica_Nazwa").text,
        praw_adKorKraj_Nazwa = (rootXML \ "dane" \ "praw_adKorKraj_Nazwa").text,
        praw_adKorWojewodztwo_Nazwa = (rootXML \ "dane" \ "praw_adKorWojewodztwo_Nazwa").text,
        praw_adKorPowiat_Nazwa = (rootXML \ "dane" \ "praw_adKorPowiat_Nazwa").text,
        praw_adKorGmina_Nazwa = (rootXML \ "dane" \ "praw_adKorGmina_Nazwa").text,
        praw_adKorMiejscowosc_Nazwa = (rootXML \ "dane" \ "praw_adKorMiejscowosc_Nazwa").text,
        praw_adKorMiejscowoscPoczty_Nazwa = (rootXML \ "dane" \ "praw_adKorMiejscowoscPoczty_Nazwa").text,
        praw_adKorUlica_Nazwa = (rootXML \ "dane" \ "praw_adKorUlica_Nazwa").text,
        praw_podstawowaFormaPrawna_Symbol = (rootXML \ "dane" \ "praw_podstawowaFormaPrawna_Symbol").text,
        praw_szczegolnaFormaPrawna_Symbol = (rootXML \ "dane" \ "praw_szczegolnaFormaPrawna_Symbol").text,
        praw_formaFinansowania_Symbol = (rootXML \ "dane" \ "praw_formaFinansowania_Symbol").text,
        praw_formaWlasnosci_Symbol = (rootXML \ "dane" \ "praw_formaWlasnosci_Symbol").text,
        praw_organZalozycielski_Symbol = (rootXML \ "dane" \ "praw_organZalozycielski_Symbol").text,
        praw_organRejestrowy_Symbol = (rootXML \ "dane" \ "praw_organRejestrowy_Symbol").text,
        praw_rodzajRejestruEwidencji_Symbol = (rootXML \ "dane" \ "praw_rodzajRejestruEwidencji_Symbol").text,
        praw_podstawowaFormaPrawna_Nazwa = (rootXML \ "dane" \ "praw_podstawowaFormaPrawna_Nazwa").text,
        praw_szczegolnaFormaPrawna_Nazwa = (rootXML \ "dane" \ "praw_szczegolnaFormaPrawna_Nazwa").text,
        praw_formaFinansowania_Nazwa = (rootXML \ "dane" \ "praw_formaFinansowania_Nazwa").text,
        praw_formaWlasnosci_Nazwa = (rootXML \ "dane" \ "praw_formaWlasnosci_Nazwa").text,
        praw_organZalozycielski_Nazwa = (rootXML \ "dane" \ "praw_organZalozycielski_Nazwa").text,
        praw_organRejestrowy_Nazwa = (rootXML \ "dane" \ "praw_organRejestrowy_Nazwa").text,
        praw_rodzajRejestruEwidencji_Nazwa = (rootXML \ "dane" \ "praw_rodzajRejestruEwidencji_Nazwa").text,
        praw_jednostekLokalnych = (rootXML \ "dane" \ "praw_jednostekLokalnych").text)
    }

}

