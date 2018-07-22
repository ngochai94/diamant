package com.dhai.service

import akka.pattern.ask
import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.stream.{ FlowShape, OverflowStrategy }
import akka.stream.scaladsl._
import akka.util.Timeout
import scala.concurrent.duration._
import com.dhai.actor.RoomServiceActorMessage._
import com.dhai.actor.{ RoomServiceActor, RoomServiceActorMessage }
import com.dhai.entity.{ GameState, Room }
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

class RoomService(implicit actorSystem: ActorSystem) {
  self =>
  private[this] var rooms = Map[String, Room]()
  private[this] val roomServiceActor = actorSystem.actorOf(Props(new RoomServiceActor(self)))

  def newRoom(roomId: String, participants: List[String]): Room = {
    val room = new Room(roomId.toString, GameState(participants))
    rooms += roomId.toString -> room
    room
  }

  private[this] def findRoomById(roomId: String): Room = rooms(roomId)

  def findRoomByUserId(userId: String): Room = {
    val duration = 5 seconds
    implicit val timeout: Timeout = Timeout(duration)
    val result = ask(roomServiceActor, GetRoom(userId)).mapTo[String].map(findRoomById)
    Await.result(result, duration)
  }

  def provideSocketGateway(userId: String): Flow[Message, Message, Any] = {
    def parse(s: String): RoomServiceActorMessage = {
      val args = s.split("\\|").toList
      args match {
        case "new" :: _ => NewRoom(userId)
        case "join" :: roomId :: _ => JoinRoom(userId, roomId)
        case "leave" :: _ => LeaveRoom(userId)
        case "ready" :: _ => Ready(userId)
        case _ => Unknown(userId, s)
      }
    }

    Flow.fromGraph(GraphDSL.create(Source.actorRef[String](64, OverflowStrategy.fail)) { implicit builder => forwarder =>
      import GraphDSL.Implicits._

      val fromWebSocket = builder.add(Flow[Message].collect {
        case TextMessage.Strict(txt) => parse(txt)
      })

      val toWebSocket = builder.add(Flow[String].collect {
        case s => TextMessage(s)
      })

      val merge = builder.add(Merge[RoomServiceActorMessage](2))

      val actorJoinedSource = builder.materializedValue.map(Subscribe(userId, _))

      val roomActorSink = Sink
        .actorRef[RoomServiceActorMessage](roomServiceActor, Unsubscribe(userId))

      actorJoinedSource ~> merge
      fromWebSocket ~> merge ~> roomActorSink
      forwarder ~> toWebSocket

      FlowShape(fromWebSocket.in, toWebSocket.out)
    }).keepAlive(10 second, () => TextMessage("keep alive"))
  }
}
