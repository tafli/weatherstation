package tafli.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tinkerforge.BrickletOutdoorWeather
import tafli.Configuration
import tafli.actors.OutdoorWeatherActor.{ReadFromSensor, ReadFromStation}

import scala.concurrent.duration._

object OutdoorWeatherActor {
  def props(ow: BrickletOutdoorWeather, dbActor: ActorRef): Props = Props(new OutdoorWeatherActor(ow, dbActor))

  case class ReadFromStation(id: Int)

  case class ReadFromSensor(id: Int)

}

class OutdoorWeatherActor(ow: BrickletOutdoorWeather, dbActor: ActorRef) extends Actor with ActorLogging {

  var rainMap: Map[Int, Long] = Map()

  import scala.concurrent.ExecutionContext.Implicits.global

  override def preStart(): Unit = log.info("OutdoorWeatherActor started")

  override def postStop(): Unit = log.info("OutdoorWeatherActor stopped")

  ow.getStationIdentifiers.foreach { id =>
    rainMap = rainMap + (id -> 0)
    RootActor.system.scheduler.schedule(0 seconds, Configuration.outdoorWeatherUpdateInterval, self, ReadFromStation(id))
  }

  ow.getSensorIdentifiers.foreach { id =>
    RootActor.system.scheduler.schedule(0 seconds, Configuration.outdoorWeatherUpdateInterval, self, ReadFromSensor(id))
  }

  override def receive: Receive = {
    case ReadFromStation(id) =>
      log.debug(s"Reading data from station [$id]")
      val data = ow.getStationData(id)
      val currentRain = data.rain
      log.debug(data.toString)

      data.rain = data.rain - rainMap(id)
      dbActor ! DbActor.SaveStationData(id, data)

      rainMap = rainMap - id
      rainMap = rainMap + (id -> currentRain)
    case ReadFromSensor(id) =>
      log.debug(s"Reading data from sensor [$id]")
      val data = ow.getSensorData(id)
      dbActor ! DbActor.SaveSensorData(id, data)
  }
}