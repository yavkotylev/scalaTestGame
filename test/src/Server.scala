
import java.net.{Socket, ServerSocket}

import scala.collection.mutable.ArrayBuffer

object Server {
  def main(args: Array[String]) {
    new Server
  }
  var listeners = new ArrayBuffer[ClientWorker]
  var newListeners = new ArrayBuffer[ClientWorker]
  var pairs = new ArrayBuffer[Pair]
}

class Server {
  var server: ServerSocket = null
  new Thread(new Runnable {
    override def run(): Unit = listenSocket()
  }).start()

  def waitToClose (client: ClientWorker): Unit ={
    val thread = new Thread(client)
    thread.start()
    addPlayer(client)
    while (client.isClosable == false){
      Thread.sleep(100)
    }
    try {
      client.client.close()
    }catch {
      case ex: Exception => ex.printStackTrace()
    }
  }

  def listenSocket(): Unit = {
    try {
      server = new ServerSocket(1234)
      while (true) {
        val t = new ClientWorker(server.accept())
        new Thread(new Runnable {
          override def run(): Unit = waitToClose(t)
        }).start()
      }
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
  }

  def addPlayer(player: ClientWorker): Unit = {
    Server.newListeners += player
    if (Server.newListeners.length == 2) {
      Server.pairs += new Pair(Server.newListeners(0), Server.newListeners(1))
      Server.newListeners.trimEnd(2)
    }
  }


}






