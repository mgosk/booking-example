package initialize

import java.text.SimpleDateFormat

import com.google.inject.{Inject, Singleton}
import customer.{CustomersRepository, CustomersService}
import customer.model.Customer
import hotel.model.{Hotel, HotelId, Reservation, Room}
import hotel.{HotelsRepository, HotelsService, ReservationsService}
import play.api.Logger

import scala.concurrent.ExecutionContext

@Singleton
class InitializationService @Inject()(hotelsService: HotelsService, reservationsService: ReservationsService,customersService: CustomersService)(implicit executionContext: ExecutionContext) {

  val df = new SimpleDateFormat("yyyy-MM-dd")

  val customer1 = Customer("Bob")
  val customer2 = Customer("Alice")
  val customer3 = Customer("Ted")
  val hotel1 = Hotel(HotelId.fromString("f33cc95e-5070-4b95-bd0e-28a5b46a91d7"), "Hilton", "Warsaw", Seq(Room(101, 66)))
  val hotel2 = Hotel(HotelId.fromString("22dc2562-4651-47b4-b96f-4a37da45b014"), "Mariot", "Warsaw", Seq(Room(122, 34), Room(666, 123)))
  val hotel3 = Hotel(HotelId.fromString("7e2c2969-70a5-4f1e-a6ff-7d493223910c"), "Hilton", "Cracow", Seq(Room(1, 1), Room(2, 312)))
  val reservation1 = Reservation(HotelId.fromString("f33cc95e-5070-4b95-bd0e-28a5b46a91d7"), 101, df.parse("2016-03-15"), df.parse("2016-03-20"), "Bob")
  val reservation2 = Reservation(HotelId.fromString("f33cc95e-5070-4b95-bd0e-28a5b46a91d7"), 101, df.parse("2016-03-31"), df.parse("2016-04-15"), "Bob")

  for {
    c1 <- customersService.create(customer1)
    c2 <- customersService.create(customer2)
    c3 <- customersService.create(customer3)
    h1 <- hotelsService.create(hotel1)
    h2 <- hotelsService.create(hotel2)
    h3 <- hotelsService.create(hotel3)
    r1 <- reservationsService.create(reservation1)
    r2 <- reservationsService.create(reservation2)
  } yield {
    Logger.info("Initialization completed")
  }

}
