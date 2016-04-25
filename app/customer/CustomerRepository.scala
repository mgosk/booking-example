package customer

import com.google.inject.Singleton
import customer.model.Customer

import scala.concurrent.Future

// function bodies are wrapped in Future.successful to easy be replaced by reactive database driver
@Singleton
class CustomerRepository {

  var set = scala.collection.mutable.Set[Customer]()

  def create(customer: Customer): Future[Boolean] = Future.successful {
    set add customer
  }

  def find(login: String) = Future.successful {
    set.find(_.login == login)
  }

}
