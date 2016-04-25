package customer

import com.google.inject.{Inject, Singleton}
import core.ErrorWrapper
import customer.model.Customer

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomersService @Inject()(customerRepository: CustomersRepository)(implicit executionContext: ExecutionContext) {

  def create(customer: Customer): Future[Either[ErrorWrapper, Customer]] = {
    customerRepository.create(customer).map {
      case true => Right(customer)
      case false => Left(ErrorWrapper("alreadyExist", "Customer already exist"))
    }
  }

}
