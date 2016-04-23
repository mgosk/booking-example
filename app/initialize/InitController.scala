package initialize

import com.google.inject.Inject
import customer.CustomerRepository
import customer.model.Customer
import hotel.HotelRepository
import hotel.model.{Room, HotelId, Hotel}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

class InitController @Inject()(customerRepository: CustomerRepository, hotelRepository: HotelRepository)(implicit executionContext: ExecutionContext) extends Controller {

  def testData() = Action.async { implicit request =>
    val customer1 = Customer("Bob")
    val customer2 = Customer("Alice")
    val hotel1 = Hotel(HotelId.random, "Hilton", "Warsaw", Seq(Room(101, 66)))
    val hotel2 = Hotel(HotelId.random, "Mariot", "Warsaw", Seq())
    for {
      c1 <- customerRepository.create(customer1)
      c2 <- customerRepository.create(customer2)
      h1 <- hotelRepository.create(hotel1)
      h2 <- hotelRepository.create(hotel2)
    } yield {
      Ok("Created")
    }
  }

}
