package auth.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class CompanyForm(val code: String, val name: String)

object CompanyForm {

  case object Fizyczna extends CompanyForm("fizyczna", "Osoba fizyczna")
  case object FizycznaDzialalnosc extends CompanyForm("fizycznaDzialnosc", "Osoba fizyczna prowadząca działalność gospodarczą")
  case object Prawna extends CompanyForm("prawna", "Osoba prawna")
  case object JednostkaOrganizacyjna extends CompanyForm("jednostkaOrganizacyjna", "Jednostka organizacyjna niemająca osobowości prawnej")
  case object Inna extends CompanyForm("inna", "Inna")

  def values: Set[CompanyForm] = sealerate.values[CompanyForm]

  def apply(code: String): CompanyForm = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct CompanyForm from: $code"))

  implicit val statusWrites = new Writes[CompanyForm] {
    override def writes(vatRate: CompanyForm): JsValue = JsString(vatRate.code)
  }

  implicit val statusReads = new Reads[CompanyForm] {
    override def reads(json: JsValue): JsResult[CompanyForm] = json match {
      case JsString(string) => JsSuccess(CompanyForm(string))
      case _ => JsError("validate.error.invalidOrganizationForm")
    }
  }

}