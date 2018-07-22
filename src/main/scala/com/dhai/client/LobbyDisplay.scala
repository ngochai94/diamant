package com.dhai.client

case class LobbyDisplay(
  rooms: List[RoomDisplay],
  currentRoom: Option[RoomDisplay],
  `type`: String = "SERVER_LOBBY")

object LobbyDisplay {
  def apply(
    currentUser: String,
    usersRoom: Map[String, String],
    usersReady: Set[String],
    started: Boolean): LobbyDisplay = {
    val roomNames = usersRoom.values.toList.distinct
    val rooms = roomNames.map { roomName =>
      val participants = usersRoom.collect {
        case (user, room) if room == roomName =>
          Participant(user, usersReady.contains(user))
      }
      RoomDisplay(roomName, participants.toList)
    }
    val currentRoom = rooms.find(_.participants.exists(_.name == currentUser)).map(_.copy(started = started))
    LobbyDisplay(rooms, currentRoom)
  }
}

