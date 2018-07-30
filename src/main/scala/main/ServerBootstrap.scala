package main

import akka.actor.ActorSystem

import scala.util.{Failure, Success, Try}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import rest.Routes
import utils.HasConfig

import scala.concurrent.Await

object ServerBootstrap extends App
  with LazyLogging
  with HasConfig
  with Routes {

  implicit val actorSystem = ActorSystem("gcp-play-server")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = actorSystem.dispatcher

  // server config
  val serverHost = config.getString("http.interface")
  val serverPort = config.getInt("http.port")

  logger.info(s"Starting up server on $serverHost:$serverPort")

  val bindingFuture = Http().bindAndHandle(routes, serverHost, serverPort)

  bindingFuture.onComplete {
    case Success(value) => value
    case Failure(e) =>
      logger.error("exception detected", e)
      closeSystem()
      sys.exit(1)
  }

  sys.addShutdownHook {
    closeSystem()
  }

  private[this] def closeSystem(): Unit = {
    import scala.concurrent.duration._
    logger.info("closing akka system...")
    Try {
      Await.ready(actorSystem.terminate(), 15.seconds)
    }
    logger.info("akka system closed")
  }
}
