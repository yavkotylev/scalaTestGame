import java.io.{InputStreamReader, BufferedReader, PrintWriter}
import java.net.Socket
import java.util.Scanner

class ClientWorker (val client: Socket) extends Runnable {
  var pair: Pair = null
  var out: PrintWriter = null
  var in: BufferedReader = null
  var isClosable = false
  def run(): Unit = {

    try {
      out = new PrintWriter(client.getOutputStream, true)
      in = new BufferedReader(new InputStreamReader(client.getInputStream))
      out.println("Hello, I will try to find an opponent for you!")
      while (pair == null || pair.endGame == false) {
        val scanner = new Scanner(in)
        while (scanner.hasNextLine) {
          val str = scanner.nextLine()
          out.println("You are write: \"" + str + "\"")
          out.flush()
          if (pair != null) {
            if (pair.firstPlayer == this) {
              pair.secondPlayer.out.println("Your rival wrote: \"" + str + "\"")
              pair.secondPlayer.out.flush()
            }
            else {
              pair.firstPlayer.out.println("Your rival wrote: \"" + str + "\"")
              pair.firstPlayer.out.flush()
            }
            pair.hearMessage(this, str)
          }
        }
      }
      isClosable = true
      out.close()
      in.close()
    } catch {
      case ex: Exception => ex.printStackTrace()
    }

  }

  def writeMessage(message: String): Unit = {
    out.flush()
    out.println(message)
    out.flush()
  }


}