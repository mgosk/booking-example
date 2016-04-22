package auth.services

import java.util.UUID
import javax.inject.Inject

import auth.managers.{ CompanyDataManager, TokensManager, UsersManager }
import auth.model._
import com.restfb.{ DefaultFacebookClient, Parameter }
import com.restfb.types.{ User => FbUser }
import com.typesafe.config.ConfigFactory
import core.MailSender
import core.model.ErrorWrapper
import gus.GusService
import gus.model.GusSearchResponse.OrganizationDataSearchResult
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import play.api.Play.current
import play.api.libs.ws.WS
import play.api.{ Logger, Play }
import auth.model.CompanyForm
import sale.NumberingSeriesManager

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex
import scala.util.{ Success, Try }

class AuthService @Inject() (mailSender: MailSender, usersManager: UsersManager, tokensManager: TokensManager, numberingSeriesManager: NumberingSeriesManager, gusService: GusService, organizationDataManager: CompanyDataManager) {

  def confirmAccountUrl(confirmationToken: String) = Play.current.configuration.getString(path = "application.url").get + s"/auth/confirm-account?token=$confirmationToken"

  val passwordResetUrl = Play.current.configuration.getString(path = "application.url").get + "/auth/reset-password"
  val conf = ConfigFactory.load();
  val clientId = conf.getString("facebook.appId")
  val clientSecret = conf.getString("facebook.secret")
  val accessTokenUrl = "https://graph.facebook.com/oauth/access_token"
  val graphApiUrl = "https://graph.facebook.com/me"

  def login(email: String, password: String): Future[Either[ErrorWrapper, User]] = {
    usersManager.authenticate(email, password).map {
      case Some(user) if user.state == UserState.Active =>
        Right(user)
      case Some(user) if user.state == UserState.New =>
        Left(ErrorWrapper("auth.login.inactive", "Confirm account before login"))
      case None =>
        Left(ErrorWrapper("auth.login.password-incorrect", "Your password is incorrect"))
    }
  }

  def register(email: String, password: String, acceptTerms: Boolean, newsletter: Boolean = false, nip: Option[String]): Future[User] = {
    usersManager.getByEmail(email).flatMap {
      case Some(user) =>
        mailSender.sendEmailAsync("Rejestracja w FaktIt", user.email.get, auth.mailTemplates.html.signupExist.apply(passwordResetUrl).toString)
        Future.successful(user)
      case None =>
        val userId = UserId.random
        val companyId = CompanyId.random
        val newUser = User(userId, Some(email), Some(BCrypt.hashpw(password, BCrypt.gensalt())),
          UserState.New, isAdmin = false, createdAt = DateTime.now, hasOrganizationData = false,
          acceptTerms = acceptTerms, newsletter = newsletter, companies = Seq(companyId))
        usersManager.create(newUser).flatMap { user =>
          configureCompany()(companyId).flatMap { a =>
            Logger.info(s"User saved in system with id ${userId}")
            if (nip.isDefined) {
              fillOrganizationData(newUser, nip.get, companyId)
            }
            val confirmationToken = Token.accountConfirmation(userId)
            tokensManager.create(confirmationToken).map { token =>
              mailSender.sendEmailAsync("Rejestracja w FaktIt", user.email.get, auth.mailTemplates.html.signupNew.apply(confirmAccountUrl(confirmationToken.token)).toString)
              user
            }
          }
        }
    }
  }

  def confirmAccount(tokenValue: String): Future[Option[User]] = {
    tokensManager.find(tokenValue, TokenKind.AccountConfirmation) flatMap {
      case Some(token) =>
        tokensManager.delete(token.token).flatMap { num =>
          usersManager.getExisting(token.userId) flatMap { user =>
            usersManager.update(user.copy(state = UserState.Active)).flatMap { updated =>
              tokensManager.delete(tokenValue).map { num =>
                Some(updated)
              }
            }
          }
        }
      case None =>
        Future.successful(None)
    }
  }

  def facebook(clientId: String, code: String, redirectUri: String): Future[Either[ErrorWrapper, User]] = {
    val xx =
      WS.url(accessTokenUrl)
        .withQueryString(
          "redirect_uri" -> redirectUri,
          "code" -> code,
          "client_id" -> clientId,
          "client_secret" -> clientSecret).get
    xx.flatMap { response =>
      val regex = new Regex("access_token=(.*)&expires=(.*)")
      response.body match {
        case regex(accessToken, expires) => {
          getFbUserDetails(accessToken) match {
            case Success(fbUser) =>
              println(fbUser)
              usersManager.getByFacebookId(fbUser.getId) flatMap {
                case Some(user) =>
                  Future.successful(Right(user))
                case None =>
                  val emailOpt = if (fbUser.getEmail != null) Some(fbUser.getEmail) else None
                  emailOpt match {
                    case Some(email) =>
                      usersManager.getByEmail(email).flatMap {
                        case Some(user) =>
                          usersManager.update(user.copy(facebookId = Some(fbUser.getId))).map { updated =>
                            Right(updated)
                          }
                        case None =>
                          createUser(fbUser, emailOpt).map { user => Right(user) }
                      }
                    case None =>
                      createUser(fbUser, emailOpt).map { user => Right(user) }
                  }
              }
            case _ => Future.successful(Left(ErrorWrapper("externalError", "Błąd usługi zawnętrznej")))
          }
        }
      }
    }
  }

  private def createUser(fbUser: FbUser, emailOpt: Option[String]): Future[User] = {
    val userId = UserId.random
    val companyId = CompanyId.random
    val newUser = User(userId, emailOpt, None, UserState.Active, isAdmin = false,
      facebookId = Some(fbUser.getId), createdAt = DateTime.now, hasOrganizationData = false, acceptTerms = false, newsletter = false, companies = Seq(companyId))
    usersManager.create(newUser).flatMap { user =>
      configureCompany()(companyId).map { x =>
        newUser
      }
    }
  }

  def resetPasswordRequest(email: String): Future[Option[User]] = {
    def url(confirmationToken: String) = Play.current.configuration.getString(path = "application.url").get + s"/auth/reset-password-confirmation?token=$confirmationToken"
    usersManager.getByEmail(email).flatMap {
      case Some(user) =>
        val passwordResetToken = Token.passwordReset(user.id)
        tokensManager.create(passwordResetToken).map { token =>
          mailSender.sendEmailAsync("Reset hasła w systemie FaktIt", user.email.get, auth.mailTemplates.html.resetPassword.apply(url(token.token)).toString)
          Some(user)
        }
      case None =>
        Future.successful(None)
    }
  }

  def changePasswordByToken(token: String, newPassword: String): Future[Either[ErrorWrapper, User]] = {
    tokensManager.find(token, TokenKind.PasswordReset) flatMap {
      case Some(token) =>
        tokensManager.delete(token.token).flatMap { num =>
          usersManager.getExisting(token.userId) flatMap { user =>
            usersManager.update(user.updatePassword(newPassword)).map { updated =>
              Right(updated)
            }
          }
        }
      case None =>
        Future.successful(Left(ErrorWrapper("notFound", "Token nie znaleziony")))
    }
  }

  def changePasswordForLoggedUser(oldPassword: Option[String], newPassword: String)(implicit user: User): Future[Either[ErrorWrapper, User]] = {
    user.email match {
      case Some(email) =>
        (user.password, oldPassword) match {
          case (Some(originalPassword), Some(oldPassword)) =>
            usersManager.authenticate(email, oldPassword).flatMap {
              case Some(user) if user.state == UserState.Active =>
                usersManager.update(user.copy(password = Some(BCrypt.hashpw(newPassword, BCrypt.gensalt())))).map { user => Right(user) }
              case None =>
                Future.successful(Left(ErrorWrapper("passwordIncorrect", "Your old password is incorrect", true)))
            }
          case (None, None) =>
            usersManager.update(user.copy(password = Some(BCrypt.hashpw(newPassword, BCrypt.gensalt())))).map { user => Right(user) }
          case _ =>
            Future.successful(Left(ErrorWrapper("passwordIncorrect", "Your old password is incorrect 2")))
        }
      case None =>
        Future.successful(Left(ErrorWrapper("passwordAuthenticationDenied", "Password auth not allowed, mail not defined ")))
    }
  }

  def getUserById(id: UserId) = usersManager.get(id)

  def getAllUser() = usersManager.getAll()

  private def configureCompany()(implicit companyId: CompanyId): Future[CompanyId] = {
    numberingSeriesManager.initForCompany()(companyId).map { seq =>
      companyId
    }
  }

  private def getFbUserDetails(accessToken: String): Try[FbUser] = {
    Try {
      val client = new DefaultFacebookClient(accessToken, clientSecret)
      client.fetchObject("me", classOf[FbUser], Parameter.`with`("fields", "email,first_name,last_name"))
    }
  }

  private def fillOrganizationData(user: User, nip: String, companyId: CompanyId) = {
    gusService.findCompanyByNip(nip, None, "organizationData").map {
      case data: OrganizationDataSearchResult =>
        organizationDataManager.upsert {
          CompanyData(
            id = companyId,
            form = CompanyForm(data.data.organizationForm),
            name = data.data.name,
            nip = nip,
            regon = data.data.regon,
            country = (if (data.data.country.isEmpty) "Polska" else data.data.country),
            voivodeship = data.data.voivodeship,
            district = data.data.district,
            commune = data.data.commune,
            city = data.data.city,
            postalCode = if (data.data.postalCode.length == 5) s"${data.data.postalCode.take(2)}-${data.data.postalCode.takeRight(3)}" else data.data.postalCode,
            postalCity = data.data.postalCity,
            street = Some(data.data.street),
            buildingNr = data.data.buildingNr,
            apartmentNr = Some(data.data.apartmentNr),
            untypicalPlace = Some(data.data.untypicalPlace),
            creationDate = data.data.creationDate,
            startDate = data.data.startDate)
        }.map { result =>
          usersManager.update(user.copy(hasOrganizationData = true)).map { updated =>
          }
        }
      case _ => //do nothing
    }
  }

}
