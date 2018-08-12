package tafli.models

import java.time.ZonedDateTime

import scalikejdbc._

case class WeatherData(
                        id: Option[Int] = None,
                        stationId: Int,
                        stationType: Int,
                        temperature: Double,
                        humidity: Int,
                        windSpeed: Double,
                        gustSpeed: Double,
                        rain: Double,
                        windDirection: Int,
                        batteryLow: Boolean,
                        createdAt: ZonedDateTime) {
  def destroy()(implicit session: DBSession = WeatherData.autoSession): Int = WeatherData.destroy(this)(session)
}


object WeatherData extends SQLSyntaxSupport[WeatherData] {

  val d = WeatherData.syntax("d")

  override val tableName = "weather"

  def apply(d: SyntaxProvider[WeatherData])(rs: WrappedResultSet): WeatherData = apply(d.resultName)(rs)

  def apply(d: ResultName[WeatherData])(rs: WrappedResultSet): WeatherData = new WeatherData(
    stationId = rs.get(d.stationId),
    stationType = rs.get(d.stationType),
    temperature = rs.get(d.temperature),
    humidity = rs.get(d.humidity),
    windSpeed = rs.get(d.windSpeed),
    gustSpeed = rs.get(d.gustSpeed),
    rain = rs.get(d.rain),
    windDirection = rs.get(d.windDirection),
    batteryLow = rs.get(d.batteryLow),
    createdAt = rs.get(d.createdAt)
  )

  def create(
              stationId: Int,
              stationType: Int,
              temperature: Double,
              humidity: Int,
              windSpeed: Option[Double] = None,
              gustSpeed: Option[Double] = None,
              rain: Option[Double] = None,
              windDirection: Option[Int] = None,
              batteryLow: Option[Boolean] = None)(implicit session: DBSession = autoSession): Unit = {
    withSQL {
      insert.into(WeatherData).namedValues(
        column.stationId -> stationId,
        column.stationType -> stationType,
        column.temperature -> temperature,
        column.humidity -> humidity,
        column.windSpeed -> windSpeed,
        column.gustSpeed -> gustSpeed,
        column.rain -> rain,
        column.windDirection -> windDirection,
        column.batteryLow -> batteryLow
      )
    }.update.apply()
  }

  def destroy(entity: WeatherData)(implicit session: DBSession = autoSession): Int = {
    withSQL {
      delete.from(WeatherData).where.eq(column.id, entity.id)
    }.update.apply()
  }

}
