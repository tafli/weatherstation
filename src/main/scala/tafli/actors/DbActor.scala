package tafli.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tinkerforge.BrickletOutdoorWeather
import tafli.actors.DbActor.{SaveSensorData, SaveStationData}
import tafli.models.WeatherData

object DbActor {
  def props(): Props = Props(new DbActor)

  val actor: ActorRef = RootActor.system.actorOf(props())

  case class SaveStationData(stationId: Int, data: BrickletOutdoorWeather#StationData)

  case class SaveSensorData(stationId: Int, data: BrickletOutdoorWeather#SensorData)

}

class DbActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case SaveStationData(id, data) =>
      WeatherData.create(id,
        stationType = 0,
        temperature = data.temperature / 10.0,
        humidity = data.humidity,
        windSpeed = Option(data.windSpeed / 10.0),
        gustSpeed = Option(data.gustSpeed / 10.0),
        rain = Option(data.rain / 10.0),
        windDirection = Option(data.windDirection),
        batteryLow = Option(data.batteryLow))

    case SaveSensorData(id, data) =>
      WeatherData.create(id,
        stationType = 1,
        temperature = data.temperature / 10.0,
        humidity = data.humidity)
  }
}