package core

import java.text.SimpleDateFormat
import java.util.Date

import play.api.mvc.QueryStringBindable

object Binders {
  val df = new SimpleDateFormat("yyyy-MM-dd")

  implicit object DateQueryBindable extends QueryStringBindable[Date] {
    def bind(key: String, params: Map[String, Seq[String]]) = params.get(key).flatMap(_.headOption).map { value =>
      try {
        Right( df.parse(value))
      } catch {
        case e: Exception =>  Left("Cannot parse parameter " + key + " as Date")
      }
    }

    override def unbind(key: String, value: Date): String = ???
  }

}