package com.dhai.entity

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.stream.{ FlowShape, OverflowStrategy }
import akka.stream.scaladsl.{ Broadcast => _, _ }
import com.dhai.actor.{ RoomActor, RoomActorMessage }
import com.dhai.actor.RoomActorMessage._
import scala.concurrent.duration._

class Room(roomId: String, initialGameState: GameState)(implicit actorSystem: ActorSystem) {
  private[this] val roomActor = actorSystem.actorOf(Props(new RoomActor(initialGameState)))

  def provideSocketGateway(userId: String): Flow[Message, Message, Any] = {
    def parse(s: String): RoomActorMessage = {
      s match {
        case "stop" => Broadcast(Action(userId, Stop))
        case "continue" => Broadcast(Action(userId, Continue))
        case _ => Unknown(userId, s)
      }
    }

    Flow.fromGraph(GraphDSL.create(Source.actorRef[String](64, OverflowStrategy.fail)) { implicit builder => userActorSource =>
      import GraphDSL.Implicits._

      val fromWebSocket = builder.add(Flow[Message].collect {
        case TextMessage.Strict(txt) => parse(txt)
      })

      val toWebSocket = builder.add(Flow[String].collect {
        case s => TextMessage(s)
      })

      val merge = builder.add(Merge[RoomActorMessage](2))

      val actorJoinedSource = builder.materializedValue.map(UserJoin(userId, _))

      val roomActorSink = Sink.actorRef[RoomActorMessage](roomActor, UserLeave(userId))

      actorJoinedSource ~> merge
      fromWebSocket ~> merge ~> roomActorSink
      userActorSource ~> toWebSocket

      FlowShape(fromWebSocket.in, toWebSocket.out)
    }).keepAlive(10 second, () => TextMessage("keep alive"))
  }

}

