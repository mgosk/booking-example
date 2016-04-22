package gus

import javax.inject.Inject

import auth.services.AuthService
import core.config.AuthorityImpl
import core.{AppController}
import play.api.libs.json.Json

class GusController @Inject()(override val authService: AuthService, gusService: GusService) extends AppController {

  def findCompanyByNip(nip: String, captcha: Option[String], format: String) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    gusService.findCompanyByNip(nip, captcha, format).map { response =>
      Ok(Json.toJson(response))
    }
  }
}
