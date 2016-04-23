package customer

import com.google.inject.Inject
import core.ErrorWrapper
import customer.protocol.CreateCustomerRequest
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}


class CustomerController @Inject()(customerRepository: CustomerRepository)(implicit executionContext: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateCustomerRequest] match {
      case s: JsSuccess[CreateCustomerRequest] =>
        customerRepository.create(s.get).map { customer =>
          Ok(Json.toJson(customer))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }


}
