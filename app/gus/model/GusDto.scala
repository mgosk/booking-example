package gus.model

import auth.model.{CompanyForm, OrganizationFormDto}
import core.CustomDateTimeFormat._
import play.api.libs.json._
import CompanyForm.FizycznaDzialalnosc

sealed abstract class GusSearchResponse

object GusSearchResponse {

  case class CaptchaImage(image: String) extends GusSearchResponse

  case class CaptchaImageRepeat(image: String) extends GusSearchResponse

  case class ContractorSearchResult(data: GusReport) extends GusSearchResponse

  case class OrganizationDataSearchResult(data: GusReport) extends GusSearchResponse

  case class SimpleSearchResult(data: GusReport) extends GusSearchResponse

  case class NotFound() extends GusSearchResponse

  case class ExternalError() extends GusSearchResponse

  implicit val captchaImageWrites = new Writes[GusSearchResponse] {
    override def writes(response: GusSearchResponse): JsValue = response match {
      case captcha: CaptchaImage => Json.toJson(
        Map("type" -> "captcha",
          "image" -> captcha.image))
      case captcha: CaptchaImageRepeat => Json.toJson(
        Map("type" -> "captchaRepeat",
          "image" -> captcha.image))
      case result: ContractorSearchResult => Json.toJson(
        Map("type" -> "contractorResponse",
          "name" -> result.data.name,
          "address1" -> result.data.address1,
          "address2" -> "",
          "postalCode" -> (if (result.data.postalCode.length == 5) s"${result.data.postalCode.take(2)}-${result.data.postalCode.takeRight(3)}" else result.data.postalCode),
          "postalCity" -> result.data.postalCity))
      case result: OrganizationDataSearchResult =>
        Json.obj("type" -> JsString("organizationDataResponse"),
          "name" -> JsString(result.data.name),
          "regon" -> JsString(result.data.regon),
          "country" -> JsString((if (result.data.country.isEmpty) "Polska" else result.data.country)),
          "voivodeship" -> JsString(result.data.voivodeship),
          "district" -> JsString(result.data.district),
          "commune" -> JsString(result.data.commune),
          "city" -> JsString(result.data.city),
          "postalCode" -> JsString((if (result.data.postalCode.length == 5) s"${result.data.postalCode.take(2)}-${result.data.postalCode.takeRight(3)}" else result.data.postalCode)),
          "postalCity" -> JsString(result.data.postalCity),
          "street" -> JsString(result.data.street),
          "buildingNr" -> JsString(result.data.buildingNr),
          "apartmentNr" -> JsString(result.data.apartmentNr),
          "untypicalPlace" -> JsString(result.data.untypicalPlace),
          "creationDate" -> result.data.creationDate,
          "startDate" -> result.data.startDate,
          "organizationForm" -> OrganizationFormDto(FizycznaDzialalnosc.code,FizycznaDzialalnosc.name))
      case result: SimpleSearchResult => Json.toJson(
        Map("type" -> "simpleResponse",
          "name" -> result.data.name))
      case _: NotFound => Json.toJson(
        Map("type" -> "notFound"))
      case _: ExternalError => Json.toJson(
        Map("type" -> "externalError"))
    }
  }

}

case class SzukajReponse(regon: String,
  nazwa: String,
  wojewodztwo: String,
  powiat: String,
  gmina: String,
  miejscowosc: String,
  kodPocztowy: String,
  ulica: String,
  typ: String,
  silosID: String)
