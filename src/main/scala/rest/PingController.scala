package rest

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import utils.RequestLimiter

trait PingController extends RequestLimiter {

  val pingRoute =
    path("ping") {
      get {
        throttle {
          complete(
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong")
          )
        }
      }
    }

}
