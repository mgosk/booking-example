package hotel

import hotel.model.{Hotel, HotelId}

import scala.concurrent.Future


// function body is wrapped in Future.successful to easy be replaced by reactive database driver
class HotelRepository {

  val collection = scala.collection.mutable.Map.empty[HotelId, Hotel]

  def create(hotel: Hotel): Future[Hotel] = Future.successful {
    collection += (hotel.id -> hotel)
    hotel
  }

  def get(id: HotelId): Future[Option[Hotel]] = Future.successful {
    collection.get(id)
  }

  def update(hotel: Hotel): Future[Option[Hotel]] = Future.successful {
    collection.put(hotel.id, hotel)
  }
}
