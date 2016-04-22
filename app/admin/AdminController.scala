package admin

import javax.inject.Inject

import admin.model.{UsersList, UserResponse}
import auth.services.AuthService
import core.AppController
import core.config.AuthorityImpl
import play.api.libs.json.Json

class AdminController @Inject() (override val authService: AuthService) extends AppController {

  def usersList() = AsyncStack(AuthorityKey -> AuthorityImpl(isAdmin = true)) { implicit request =>
    authService.getAllUser().map { seq =>
      Ok(Json.toJson(UsersList(seq.map(u => UserResponse(u.email, u.state, u.facebookId,u.hasOrganizationData)))))
    }
  }

}
