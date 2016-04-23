package customer

import com.google.inject.Singleton
import customer.model.Customer

import scala.concurrent.Future

@Singleton
class CustomerRepository {

  var set = scala.collection.mutable.Set[Customer]()

  def create(customer: Customer): Future[Boolean] = Future.successful {
    set add customer
  }

}
