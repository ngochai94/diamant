package com.dhai.actor

import akka.actor.{ Actor, ActorRef }
import RoomServiceActorMessage._
import com.dhai.client.LobbyDisplay
import com.dhai.service.RoomService
import scala.util.Random
import io.circe.generic.auto._
import io.circe.syntax._

class RoomServiceActor(roomService: RoomService) extends Actor {
  private[this] var subscribers = Map[String, ActorRef]()
  private[this] var usersRoom = Map[String, String]() // user to room
  private[this] var usersReady = Set[String]()
  private[this] var userInGame = Set[String]()

  override def receive: Receive = {
    case Subscribe(userId, actorRef) =>
      println(s"Added subscriber $userId from lobby")
      subscribers += userId -> actorRef
      update()

    case Unsubscribe(userId) =>
      println(s"Removed subscriber $userId from lobby")
      subscribers -= userId
      if (!userInGame.contains(userId)) usersRoom -= userId

    case NewRoom(userId) =>
      if (usersRoom.keys.forall(_ != userId)) {
        val roomId = newRoomId()
        usersRoom += userId -> roomId
        println(s"Created new room $roomId by $userId")
        update()
      }

    case JoinRoom(userId, roomId) =>
      if (usersRoom.keys.forall(_ != userId) && usersRoom.values.exists(_ == roomId)) {
        usersRoom += userId -> roomId
        println(s"User $userId joined room $roomId")
        update()
      }

    case LeaveRoom(userId) =>
      usersRoom -= userId
      println(s"User $userId left some room")
      update()

    case GetRoom(userId) =>
      sender ! usersRoom.getOrElse(userId, "")

    case Ready(userId) =>
      usersRoom.get(userId) foreach { roomId =>
        usersReady += userId
        println(s"User $userId in room $roomId is ready")

        val usersInRoom = usersRoom.filter {
          case (_, room) => room == roomId
        }.keys.toList

        if (usersInRoom.size > 1 && usersInRoom.forall(usersReady.contains)) {
          usersInRoom.foreach { userId =>
            usersReady -= userId
            userInGame += userId
          }

          // start game
          println("Starting game")
          println(usersInRoom)
          roomService.newRoom(roomId, usersInRoom)
        }
        update()
      }

    case Unknown(userId, msg) =>
      println(s"$userId sent $msg")
  }

  private[this] def newRoomId(): String = Random.alphanumeric.take(10).mkString

  private[this] def update(): Unit = {
    println(s"Broadcasting to ${subscribers.keys}")
    subscribers.foreach {
      case (user, actorRef) =>
        actorRef ! LobbyDisplay(user, usersRoom, usersReady, userInGame.contains(user)).asJson.noSpaces
    }
  }
}

sealed trait RoomServiceActorMessage

object RoomServiceActorMessage {

  case class Subscribe(userId: String, actorRef: ActorRef) extends RoomServiceActorMessage

  case class Unsubscribe(userId: String) extends RoomServiceActorMessage

  case class NewRoom(userId: String) extends RoomServiceActorMessage

  case class JoinRoom(userId: String, roomId: String) extends RoomServiceActorMessage

  case class LeaveRoom(userId: String) extends RoomServiceActorMessage

  case class GetRoom(userId: String) extends RoomServiceActorMessage

  case class Ready(userId: String) extends RoomServiceActorMessage

  case class Unknown(userId: String, msg: String) extends RoomServiceActorMessage

}
