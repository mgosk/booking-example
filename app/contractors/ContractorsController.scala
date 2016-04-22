package contractors

import javax.inject.Inject

import auth.model.Identity
import auth.repositories.UsersRepository
import auth.services.AuthService
import contractors.model.{Contractor, ContractorId}
import core.AppController
import core.config.AuthorityImpl
import core.model.ErrorWrapper
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.BodyParsers

import scala.concurrent.Future

class ContractorsController @Inject()(override val authService: AuthService, contractorsService: ContractorsService) extends AppController {

  def create() = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>

    request.body.validate[ContractorRequest] match {
      case s: JsSuccess[ContractorRequest] =>
        val contractor = Contractor.fromRequest(s.get)
        contractorsService.create(contractor).map { contractor =>
          Ok(Json.toJson(contractor))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper("invalidJson", JsError.toJson(e).toString))))
    }
  }

  def update(id: ContractorId) = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>
    request.body.validate[ContractorRequest] match {
      case s: JsSuccess[ContractorRequest] =>
        val contractor = Contractor.fromRequest(s.get, Some(id))
        contractorsService.update(contractor).map { contractor =>
          Ok(Json.toJson(contractor))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper("invalidJson", JsError.toJson(e).toString))))
    }
  }

  def list(sortKey: String, sortReverse: Boolean) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    contractorsService.find(sortKey, sortReverse).map { seq =>
      Ok(Json.toJson(ContractorsList(seq.map(ContractorsListRecord.fromContractor(_)))))
    }
  }

  def get(id: ContractorId, format: Option[String]) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    contractorsService.find(id).map {
      case Some(contractor) =>
        println(format)
        format match {
          case Some("raw") =>  Ok(Json.toJson(contractor))
          case _ => Ok(Json.toJson(ContractorResponse.fromBe(contractor)))
        }
      case None => BadRequest("")
    }
  }

  def remove(id: ContractorId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    contractorsService.find(id).flatMap {
      case Some(contractor) =>
        contractorsService.remove(contractor).map { contractor =>
          Ok(Json.toJson(contractor))
        }
      case None => Future.successful(BadRequest(""))
    }
  }

  def autocompleter(term: String) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    contractorsService.findByTerm(term).map { seq =>
      Ok(Json.toJson(AutocompleterContractorsList(seq)))
    }
  }

}
