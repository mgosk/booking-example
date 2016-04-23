package customer.protocol

import play.api.libs.json.Json

case class CreateCustomerRequest(login: String)

object CreateCustomerRequest {

  implicit val reads = Json.reads[CreateCustomerRequest]

}