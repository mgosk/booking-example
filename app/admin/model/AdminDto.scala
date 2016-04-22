package admin.model

import auth.model.UserState
import play.api.libs.json.Json

case class UserResponse(email: Option[String], state: UserState, fbId: Option[String], hasOrganizationData: Boolean)

object UserResponse {

  implicit val userResponseWrites = Json.writes[UserResponse]

}
//TODO add pagination etc
case class UsersList(data: Seq[UserResponse])

object UsersList {

  implicit val usersListWrites = Json.writes[UsersList]

}
