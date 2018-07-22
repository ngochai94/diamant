package com.dhai

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.dhai.service.RoomService
import scala.io.StdIn

object Main extends App {
  //  import com.dhai.entity._
  //  val gameState = GameState(List("Hai", "Alice"))
  //  val (state2, _) = gameState.consume(Action("Hai", Stop))
  //  val (state3, _) = state2.consume(Action("Alice", Continue))
  //  println(state3)

  implicit val actorSystem: ActorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer: ActorMaterializer = ActorMaterializer()

  val roomService = new RoomService()

  val interface = "0.0.0.0"
  val port = 80

  val route = pathPrefix("ws" / "lobby") {
    parameters('user) { userId =>
      handleWebSocketMessages(roomService.provideSocketGateway(userId))
    }
  } ~ pathPrefix("ws" / "room") {
    parameters('user) { userId =>
      handleWebSocketMessages(roomService.findRoomByUserId(userId).provideSocketGateway(userId))
    }
  } ~ get {
    (pathEndOrSingleSlash & redirectToTrailingSlashIfMissing(StatusCodes.TemporaryRedirect)) {
      getFromFile("src/main/js/build/index.html")
    } ~ {
      getFromDirectory("src/main/js/build")
    }
  }

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate)
}

