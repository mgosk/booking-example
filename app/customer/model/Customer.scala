package customer.model

import customer.protocol.CreateCustomerRequest
import play.api.libs.json.Json

case class Customer(id: CustomerId, login: String)

object Customer {

  implicit val format = Json.format[Customer]

  implicit def fromRequest(customer: CreateCustomerRequest) = Customer(
    id = CustomerId.random,
    login = customer.login)

}