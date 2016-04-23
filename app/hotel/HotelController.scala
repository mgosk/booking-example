package hotel

import javax.inject.Inject
import core.ErrorWrapper
import hotel.protocol.CreateHotelRequest
import play.api.libs.json.{ Json, JsError, JsSuccess }
import play.api.mvc.{ Action, Controller }

import scala.concurrent.{ ExecutionContext, Future }

class HotelController @Inject() (hotelService: HotelRepository)(implicit ec: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateHotelRequest] match {
      case s: JsSuccess[CreateHotelRequest] =>
        hotelService.create(s.get).map { hotel =>
          Ok(Json.toJson(hotel))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  def get(city:String,minPrice:Long,maxPrice:Long) = Action.async { implicit request =>
    Future.successful(Ok("asd"))
  }

}
