package tafli

import java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, FiniteDuration}

object Configuration {
  val conf: Config = ConfigFactory.load

  val tfConnections: Seq[Connection] = for {
    connection <- conf.getConfigList("tinkerforge.connections").asScala
  } yield Connection(connection.getString("host"), connection.getInt("port"))

  val outdoorWeatherUID: String = conf.getString("tinkerforge.bricklets.outdoorWeather.uid")
  val outdoorWeatherUpdateInterval: FiniteDuration =
    FiniteDuration(Duration(conf.getString("tinkerforge.bricklets.outdoorWeather.updateInterval")).toMillis, TimeUnit.MILLISECONDS)

  def tagIDs = conf.getStringList("tinkerforge.bricklets.nfc.tags").asScala
}

case class Connection(host: String, port: Int)
