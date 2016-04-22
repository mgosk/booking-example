package core

import auth.model.{Company, Identity}
import core.config.AuthConfigImpl
import jp.t2v.lab.play2.auth.{AuthElement, OptionalAuthElement}
import jp.t2v.lab.play2.stackc.RequestWithAttributes
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext

abstract class AppController extends Controller with AuthConfigImpl with AuthElement {
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
  //fixme someday
  implicit def identity(implicit req: RequestWithAttributes[_]): Identity = Identity(loggedIn, Company(loggedIn.companies.head))

}

abstract class AppControllerWithOptionalUser extends Controller with AuthConfigImpl with OptionalAuthElement {
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

}