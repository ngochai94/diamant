package com.dhai.entity

sealed trait Card {

}

case class PointCard(id: Int, remain: Int) extends Card

case class GoldCard(id: Int, remain: Int) extends Card

case class HazardCard(id: Int) extends Card

object Card {
  private[this] val pointCards: List[Card] = List(1, 2, 3, 4, 5, 5, 7, 7, 9, 11, 11, 13, 14, 15, 17)
    .map(PointCard(_, 0))

  private[this] val goldCards: List[Card] = (1 to 5).toList.map(GoldCard(_, 0))

  private[this] val hazardCards: List[Card] = for {
    _ <- (1 to 3).toList
    id <- 1 to 5
  } yield {
    HazardCard(id)
  }

  val allCards: List[Card] = pointCards ++ goldCards ++ hazardCards
}
