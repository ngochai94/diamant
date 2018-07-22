package com.dhai.client

case class Participant(name: String, ready: Boolean)

case class RoomDisplay(name: String, participants: List[Participant], started: Boolean = false)

