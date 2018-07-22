package com.dhai.entity

import scala.util.Random

case class GameState(
  round: Int,
  userState: Map[String, UserState],
  activeCards: List[Card],
  inactiveCards: List[Card],
  deck: List[Card],
  finished: Boolean = false) {
  def consume(action: Action): (GameState, List[Transition]) = {
    if (finished || !userState.get(action.user).exists(_.status == Pending)) (this, Nil)
    else {
      val newUserState = userState + (action match {
        case Action(user, Stop) => user -> userState(user).copy(status = StopDecided)
        case Action(user, Continue) => user -> userState(user).copy(status = ContinueDecided)
      })
      println(newUserState)

      val (newState, changes) = getNextStates(newUserState)
      (newState, changes :+ UserDecided(action.user))
    }
  }

  private[this] def getNextStates(userState: Map[String, UserState]): (GameState, List[Transition]) = {
    if (userState.values.exists(_.status == Pending)) (this.copy(userState = userState), Nil)
    else {
      val stoppingUsers = userState.filter(_._2.status == StopDecided)
      val continueUsers = userState.filter(_._2.status == ContinueDecided)
      val revealed = Revealed(stoppingUsers.keys.toList, continueUsers.keys.toList)

      val shares = getShares(stoppingUsers.size)
      val stoppedUsers = stoppingUsers
        .mapValues(state => UserState(Inactive, state.pendingScore + state.score + shares, 0))

      val newActiveCards = activeCards.map {
        case PointCard(id, remain) =>
          if (stoppingUsers.isEmpty) PointCard(id, remain)
          else PointCard(id, 0)
        case GoldCard(id, remain) =>
          if (stoppingUsers.size == 1) GoldCard(id, 0)
          else GoldCard(id, remain)
        case card => card
      }

      if (continueUsers.isEmpty) {
        val newGameState = GameState(
          round + 1,
          (userState ++ stoppedUsers).mapValues(_.copy(status = Pending, pendingScore = 0)),
          Nil,
          inactiveCards,
          Random.shuffle(activeCards ++ deck))
        // TODO: add transitions
        if (newGameState.round > 5) (newGameState.copy(finished = true), List(revealed))
        else (newGameState, List(revealed))
      } else {
        val newUserState = userState ++ stoppedUsers ++ continueUsers
          .mapValues(_.copy(status = Pending))
        val (newState, changes) = dealCard(newUserState, newActiveCards)
        (newState, changes :+ revealed)
      }
    }
  }

  private[this] def dealCard(
    userState: Map[String, UserState],
    activeCards: List[Card]): (GameState, List[Transition]) = {
    val newCard = deck.head // deck can't be empty
    val newDeck = deck.tail
    val activeUsers = userState.filter(_._2.status == Pending)

    newCard match {
      case PointCard(id, _) =>
        val earnedEach = id / activeUsers.size
        val newActiveCards = activeCards :+ PointCard(id, id % activeUsers.size)
        val newUserState = userState ++ activeUsers
          .mapValues(state => state.copy(pendingScore = state.pendingScore + earnedEach))
        // TODO: add transitions
        (GameState(round, newUserState, newActiveCards, inactiveCards, newDeck), Nil)

      case GoldCard(id, _) =>
        val activeGoldCards = activeCards.map {
          case GoldCard(_, _) => 1
          case _ => 0
        }.sum
        val remain = if (activeGoldCards > 1) 10 else 5
        val newActiveCards = activeCards :+ GoldCard(id, remain)
        // TODO: add transitions
        (GameState(round, userState, newActiveCards, inactiveCards, newDeck), Nil)

      case _ if !activeCards.contains(newCard) =>
        val newActiveCards = activeCards :+ newCard
        (GameState(round, userState, newActiveCards, inactiveCards, newDeck), Nil)

      case _ =>
        val newGameState = GameState(
          round + 1,
          userState.mapValues(_.copy(status = Pending, pendingScore = 0)),
          Nil,
          inactiveCards :+ newCard,
          Random.shuffle(activeCards ++ newDeck))
        // TODO: add transitions
        if (newGameState.round > 5) (newGameState.copy(finished = true), Nil)
        else (newGameState, Nil)
    }
  }

  private[this] def getShares(numUser: Int): Int = {
    val totalPoint = activeCards.collect {
      case PointCard(_, remain) => remain
    }.sum

    val goldPoint = activeCards.collect {
      case GoldCard(_, remain) => remain
    }.sum

    totalPoint / Math.max(1, numUser) + (numUser match {
      case 1 => goldPoint
      case _ => 0
    })
  }
}

object GameState {
  def apply(users: List[String]): GameState = {
    val userState = users.map { user =>
      user -> UserState(Pending, 0, 0)
    }.toMap
    val deck = Random.shuffle(Card.allCards)
    GameState(1, userState, Nil, Nil, deck)
  }
}

class Transition(kind: String)

case class UserDecided(user: String) extends Transition("decide")

case class Revealed(stopping: List[String], continue: List[String]) extends Transition("reveal")

case class CardDealed(card: Card) extends Transition("card_deal")
