package customer

import javax.inject.Singleton

import com.google.inject.Inject
import core.ErrorWrapper
import customer.model.Customer
import customer.protocol.CreateCustomerRequest
import hotel.ReservationsService
import play.api.libs.json.{ JsError, JsSuccess, Json }
import play.api.mvc.{ Action, Controller }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class CustomersController @Inject()(customersService: CustomersService, reservationsService: ReservationsService)(implicit executionContext: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateCustomerRequest] match {
      case s: JsSuccess[CreateCustomerRequest] =>
        val customer: Customer = s.get
        customersService.create(customer).map {
          case Right(customer) => Ok(Json.toJson(customer))
          case Left(error) => BadRequest(Json.toJson(error))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  //TODO extract login from authorization token
  def reservations(login: String) = Action.async { implicit request =>
    reservationsService.getReservationsForUser(login).map { seq =>
      Ok(Json.toJson(seq))
    }
  }

}
