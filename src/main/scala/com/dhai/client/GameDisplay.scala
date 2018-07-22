package com.dhai.client

import com.dhai.entity._

case class GameDisplay(
  round: Int,
  users: List[PlayerDisplay],
  currentUser: Option[PlayerDisplay],
  earning: Int,
  inactiveCards: List[CardDisplay],
  cards: List[CardDisplay],
  `type`: String = "SERVER_ROOM")

object GameDisplay {
  def apply(currentUser: String, gameState: GameState): GameDisplay = {
    val users = gameState.userState.map {
      case (user, userState) =>
        val status = userState.status match {
          case Inactive => "inactive"
          case Pending => "pending"
          case _ => "done"
        }
        PlayerDisplay(user, userState.score, status)
    }.toList

    val currentUserState = gameState.userState.find(_._1 == currentUser).map {
      case (user, userState) =>
        val status = userState.status match {
          case Inactive => "inactive"
          case Pending => "pending"
          case StopDecided => "stop"
          case ContinueDecided => "continue"
        }
        PlayerDisplay(user, userState.score, status)
    }

    val earning = gameState.userState.find(_._1 == currentUser).map(_._2.pendingScore).getOrElse(0)

    val inactiveCards = gameState.inactiveCards.collect {
      case HazardCard(id) => CardDisplay(s"hazard$id", 0)
    }

    val cards = gameState.activeCards.map {
      case PointCard(id, remain) => CardDisplay(s"$id", remain)
      case GoldCard(id, remain) => CardDisplay(s"gold$id", remain)
      case HazardCard(id) => CardDisplay(s"hazard$id", 0)
    }

    GameDisplay(gameState.round, users, currentUserState, earning, inactiveCards, cards)
  }
}
