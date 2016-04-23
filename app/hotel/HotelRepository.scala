package hotel

import com.google.inject.Singleton
import hotel.model.{Room, Hotel, HotelId}

import scala.concurrent.Future


@Singleton
// function bodies are wrapped in Future.successful to easy be replaced by reactive database driver
class HotelRepository {

  val collection = scala.collection.mutable.Map.empty[HotelId, Hotel]

  def create(hotel: Hotel): Future[Hotel] = Future.successful {
    collection += (hotel.id -> hotel)
    hotel
  }

  def get(id: HotelId): Future[Option[Hotel]] = Future.successful {
    collection.get(id)
  }


  def searchForRooms(city: String, minPrice: Option[Long], maxPrice: Option[Long]): Future[Map[HotelId, Seq[Room]]] = Future.successful {
    collection.filter { case (id: HotelId, hotel: Hotel) => hotel.city.toLowerCase == city.toLowerCase }
      .map(tuple => (tuple._1, tuple._2.rooms.filter(room => (minPrice.isEmpty || minPrice.get <= room.price) && (maxPrice.isEmpty || room.price <= maxPrice.get))))
      .toMap
  }


  def update(hotel: Hotel): Future[Option[Hotel]] = Future.successful {
    collection.put(hotel.id, hotel)
  }
}
