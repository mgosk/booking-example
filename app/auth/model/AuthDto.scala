package auth.model

import java.util.UUID

import play.api.libs.json.Json

case class LoginRequest(email: String, password: String)

object LoginRequest {

  implicit val loginRequestReads = Json.reads[LoginRequest]

}

case class SignupRequest(email: String, password: String, nip: Option[String] = None, acceptTerms: Boolean)

object SignupRequest {

  implicit val loginRequestReads = Json.reads[SignupRequest]

}

case class AccountResponse(userId: UserId,
  companyId: CompanyId,
  email: Option[String],
  isAdmin: Boolean = false,
  hasOrganizationData: Boolean,
  hasPassword: Boolean,
  roles: Seq[String])

object AccountResponse {

  implicit val accountResponseWrites = Json.writes[AccountResponse]

  def fromUser(user: User): AccountResponse = {
    val roles = if (user.facebookId.isDefined && user.facebookId.get == "936469789734806") Seq("acc", "advancedAcc") else Seq.empty[String]
    AccountResponse(
      userId = user.id,
      companyId = user.companies.head,
      email = user.email,
      isAdmin = user.isAdmin,
      hasOrganizationData = user.hasOrganizationData,
      hasPassword = user.password.isDefined,
      roles = roles)
  }

}

case class FacebookRequest(clientId: String, code: String, redirectUri: String)

object FacebookRequest {

  implicit val facebookRequestReads = Json.reads[FacebookRequest]

}

case class ResetPasswordRequest(email: String)

object ResetPasswordRequest {
  implicit val resetPasswordRequestReads = Json.reads[ResetPasswordRequest]
}

case class ResetPasswordConfirmation(token: String, newPassword: String)

object ResetPasswordConfirmation {

  implicit val resetPasswordConfirmationReads = Json.reads[ResetPasswordConfirmation]

}

case class ChangePasswordRequest(oldPassword: Option[String], newPassword: String)

object ChangePasswordRequest {

  implicit val changePasswordRequestReads = Json.reads[ChangePasswordRequest]

}
