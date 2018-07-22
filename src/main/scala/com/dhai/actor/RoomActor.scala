package com.dhai.actor

import RoomActorMessage._
import akka.actor.{ Actor, ActorRef }
import com.dhai.client.GameDisplay
import com.dhai.entity.{ Action, GameState }
import io.circe.generic.auto._
import io.circe.syntax._

class RoomActor(initialGameState: GameState) extends Actor {
  private[this] var participants = Map[String, ActorRef]()
  private[this] var gameState = initialGameState

  override def receive: Receive = {
    case UserJoin(userId, actorRef) =>
      println(s"User $userId joins")
      participants += userId -> actorRef
      update()
    case UserLeave(userId) =>
      println(s"User $userId leaves")
      participants -= userId
    case Broadcast(action) =>
      println(s"${action.user} chose: ${action.choice}")
      val next = gameState.consume(action)
      gameState = next._1
      println(gameState)
      update()
    case Unknown(userId, msg) =>
      println(s"$userId sent $msg")
  }

  private[this] def update() = {
    participants.foreach {
      case (user, actorRef) =>
        actorRef ! GameDisplay(user, gameState).asJson.noSpaces
    }
  }
}

sealed trait RoomActorMessage
object RoomActorMessage {
  case class UserJoin(userId: String, actorRef: ActorRef) extends RoomActorMessage
  case class UserLeave(userId: String) extends RoomActorMessage
  case class Broadcast(action: Action) extends RoomActorMessage
  case class Unknown(userId: String, msg: String) extends RoomActorMessage
}
