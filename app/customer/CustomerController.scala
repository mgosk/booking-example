package customer

import javax.inject.Singleton

import com.google.inject.Inject
import core.ErrorWrapper
import customer.model.Customer
import customer.protocol.CreateCustomerRequest
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerController @Inject()(customerRepository: CustomerRepository)(implicit executionContext: ExecutionContext) extends Controller {

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

  //for simplicity sake customer id passed as url param
  //TODO extract customerId from authorization toker
  def reservations(login: String) = Action.async { implicit request =>
    Future.successful(Ok("asd"))
  }


}
