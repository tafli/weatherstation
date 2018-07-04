package tafli.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tinkerforge.IPConnection
import tafli.actors.StackActor._
import tafli.{Bricklet, TFConnector}

object StackActor {
  def props(): Props = Props(new StackActor)

  val actor: ActorRef = RootActor.system.actorOf(props())

  case class Tick()

  case class Enumerate(bricklet: Bricklet, iPConnection: IPConnection)

  case class GetBricklets()

  case class GetBrickletsByIdentifier(identifier: Int)

  case class GetBrickletByUid(uid: String)

  case class GetIpConnectionByUid(uid: String)

}

class StackActor extends Actor with ActorLogging {
  var brickletMap: Map[String, (Bricklet, IPConnection)] = Map()

  override def preStart(): Unit = {
    log.info("StackActor started")
    self ! Tick
  }

  override def postStop(): Unit = log.info("StackActor stopped")

  def receive: Receive = {
    case Tick => {
      log.debug("Enumerate TF stack...")
      brickletMap = Map()
      TFConnector.enumerate()
    }
    case Enumerate(bricklet, iPConnection) => {
      log.debug(s"Adding bricklet with UID [${bricklet.uid}]")
      brickletMap = brickletMap + (bricklet.uid -> (bricklet, iPConnection))
    }
    case GetBricklets => {
      log.debug(s"Someone is asking for all bricklets! Got [${brickletMap.size}] bricklets.")
      sender ! brickletMap.values.map(_._1).toSet
    }
    case GetBrickletsByIdentifier(identifier) =>
      sender ! brickletMap.filter(_._2._1.deviceIdentifier == identifier).map(_._2._1).toSet
    case GetBrickletByUid(uid) =>
      log.debug(s"Retrieve UID [$uid]")
      sender ! brickletMap(uid)._1
    case GetIpConnectionByUid(uid) => sender ! brickletMap(uid)._2

    case _ => log.warning("Received invalid message")
  }
}