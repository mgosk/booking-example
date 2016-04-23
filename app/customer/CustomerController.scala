package customer

import javax.inject.Singleton

import com.google.inject.Inject
import core.ErrorWrapper
import customer.model.Customer
import customer.protocol.CreateCustomerRequest
import hotel.ReservationsService
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerController @Inject() (customerRepository: CustomerRepository, reservationsService: ReservationsService)(implicit executionContext: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateCustomerRequest] match {
      case s: JsSuccess[CreateCustomerRequest] =>
        val customer: Customer = s.get
        customerRepository.create(customer).map {
          case true => Ok(Json.toJson(customer))
          case false => BadRequest(Json.toJson(ErrorWrapper("alreadyExist", "Customer already exist")))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  //TODO extract login from authorization token
  def reservations(login: String) = Action.async { implicit request =>
    reservationsService.getForUser(login).map { seq =>
      Ok(Json.toJson(seq))
    }
  }

}
