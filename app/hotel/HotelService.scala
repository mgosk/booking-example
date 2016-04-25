package hotel

import com.google.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext

@Singleton
class HotelService @Inject() (hotelRepository: HotelRepository)(implicit executionContext: ExecutionContext) {



}
