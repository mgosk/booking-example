package hotel

import com.google.inject.{ImplementedBy, Singleton}
import hotel.model.{Hotel, HotelId}

import scala.concurrent.Future

@ImplementedBy(classOf[HotelRepositoryImpl])
trait HotelRepository {

  def create(hotel: Hotel): Future[Hotel]

}

@Singleton
class HotelRepositoryImpl extends HotelRepository {

  val collection = scala.collection.mutable.Map.empty[HotelId, Hotel]

  def create(hotel: Hotel): Future[Hotel] = Future.successful {
    collection += (hotel.id -> hotel)
    hotel
  }

}
