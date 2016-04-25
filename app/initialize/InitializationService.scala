package initialize

import com.google.inject.{ Inject, Singleton }
import customer.CustomerRepository
import customer.model.Customer
import hotel.HotelRepository
import hotel.model.{ Hotel, HotelId, Room }
import play.api.Logger

import scala.concurrent.ExecutionContext

@Singleton
class InitializationService @Inject() (customerRepository: CustomerRepository, hotelRepository: HotelRepository)(implicit executionContext: ExecutionContext) {

  val customer1 = Customer("Bob")
  val customer2 = Customer("Alice")
  val hotel1 = Hotel(HotelId.random, "Hilton", "Warsaw", Seq(Room(101, 66)))
  val hotel2 = Hotel(HotelId.random, "Mariot", "Warsaw", Seq(Room(122, 1)))
  for {
    c1 <- customerRepository.create(customer1)
    c2 <- customerRepository.create(customer2)
    h1 <- hotelRepository.create(hotel1)
    h2 <- hotelRepository.create(hotel2)
  } yield {
    Logger.info("Initialization completed")
  }

}
