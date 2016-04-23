package customer.model

import customer.protocol.CreateCustomerRequest
import play.api.libs.json.Json

// for simplicity login is used as unique identifier

case class Customer(login: String)

object Customer {

  implicit val format = Json.format[Customer]

  implicit def fromRequest(customer: CreateCustomerRequest) = Customer(login = customer.login)

}