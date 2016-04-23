package customer

import com.google.inject.{ImplementedBy, Singleton}
import customer.model.{Customer, CustomerId}

import scala.concurrent.Future

@ImplementedBy(classOf[CustomerRepositoryImpl])
trait CustomerRepository {

  def create(Customer: Customer): Future[Customer]

}

@Singleton
class CustomerRepositoryImpl extends CustomerRepository {

  val collection = scala.collection.mutable.Map.empty[CustomerId, Customer]

  def create(Customer: Customer): Future[Customer] = Future.successful {
    collection += (Customer.id -> Customer)
    Customer
  }

}
