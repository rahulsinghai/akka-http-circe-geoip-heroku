package com.singhaiuklimited

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.singhaiuklimited.routing.Service
import com.typesafe.config.ConfigFactory

//import scala.io.StdIn
import scala.util.Try

object Boot extends App with Service {
  override implicit val system = ActorSystem("akka-http-circe-geoip-heroku")
  // needed for the future flatMap/onComplete in the end
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  system.registerOnTermination {
    system.log.info("Akka HTTP microservice shutdown.")
  }

  private val httpInterface: String = Try(config.getString("http.interface")).getOrElse("0.0.0.0")
  private val httpPort: Int = Try(config.getInt("http.port")).getOrElse(80)
  private val bindingFuture = Http().bindAndHandle(handler = logRequestResult("log")(routes), interface = httpInterface, port = httpPort)

  logger.info(s"Server online at http://$httpInterface:$httpPort/")
  /* Commented out as readLine was causing crash in Heroku
  logger.info("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
  */
}
