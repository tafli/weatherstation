package tafli.actors

import akka.actor.Props

object MasterBrickActor {
  def props: Props = Props(new MasterBrickActor)

  case class BrickUid(uid: String)

  case class BrickData(api: String, voltage: Int, temp: Double)

}


