package auth

import javax.inject.Inject

import auth.managers.UsersManager
import auth.model.{CompanyData, CompanyDataDto, CompanyForm, OrganizationFormDto}
import auth.repositories.UsersRepository
import auth.services.{AuthService, CompanyDataService}
import core.AppController
import core.config.AuthorityImpl
import core.model.ErrorWrapper
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.BodyParsers
import auth.model.CompanyForm
import auth.model.OrganizationFormDto

import scala.concurrent.Future

class ProfileController @Inject() (override val authService: AuthService, usersRepository: UsersRepository, organizationDataService: CompanyDataService, usersManager: UsersManager) extends AppController {

  def organizationFormsList() = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    Future.successful(Ok(Json.toJson(CompanyForm.values.map(o => OrganizationFormDto(o.code, o.name)))))
  }

  def updateOrganizationData() = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>
    request.body.validate[CompanyDataDto] match {
      case s: JsSuccess[CompanyDataDto] =>
        val organizationData = CompanyData.fromRequest(s.get)
        organizationDataService.upsert(organizationData).flatMap { organizationData =>
          usersManager.update(loggedIn.copy(hasOrganizationData = true)).map { user =>
            Ok(Json.toJson(CompanyDataDto.fromBe(organizationData)))
          }
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper("invalidJson", JsError.toJson(e).toString))))
    }
  }

  def getOrganizationData() = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    organizationDataService.find().map {
      case Some(organizationData) =>
        Ok(Json.toJson(CompanyDataDto.fromBe(organizationData)))
      case None =>
        Ok(Json.toJson(Json.obj()))
    }

  }

}
