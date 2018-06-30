package tafli

import akka.pattern.ask
import akka.util.Timeout
import com.tinkerforge.{BrickletOutdoorWeather, IPConnection}
import com.typesafe.scalalogging.StrictLogging
import scalikejdbc.config.DBs
import tafli.actors.{DbActor, OutdoorWeatherActor, RootActor, StackActor}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success}


object WeatherStation extends App with StrictLogging {
  logger.info("Initialize DB")
  DBs.setupAll

  logger.info("Initialize Tinkerforge stack")
  val stack = StackActor.actor

  stack ! StackActor.Tick

  logger.info("Starting Weather Station...")

  // Wait until stack is initialized
  Thread.sleep(2000)

  implicit val timeout: Timeout = 2 seconds

  val ow: Future[IPConnection] = (stack ? StackActor.GetIpConnectionByUid(Configuration.outdoorWeatherUID)).mapTo[IPConnection]

  ow.onComplete {
    case Success(ipConnection: IPConnection) =>
      val ow = new BrickletOutdoorWeather(Configuration.outdoorWeatherUID, ipConnection)
      RootActor.system.actorOf(OutdoorWeatherActor.props(ow, DbActor.actor))
    case Failure(ex) => ex.printStackTrace()
  }

  logger.info("Press ENTER to shutdown...")
  StdIn.readLine()

  RootActor.system.terminate()
}