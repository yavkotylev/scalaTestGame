import scala.util.Random

class Pair (val firstPlayer: ClientWorker, val secondPlayer: ClientWorker) {
  firstPlayer.pair = this
  secondPlayer.pair = this
  var flag = false
  var endGame = false
  new Thread(new Runnable {
    override def run(): Unit = startPlay()
  }).start()


  def hearMessage(player: ClientWorker, message: String): Unit = {
    if (flag == false) {
      player.writeMessage("You press the button too soon! You lost")

      if (player == firstPlayer) secondPlayer.writeMessage("Your rival press the button too soon! You win!") else firstPlayer.writeMessage("Your rival press the button too soon! You win!")

    }
    else {
      if (endGame == false) {
        if (message.equals(" ")) {
          player.writeMessage("You win!")
          if (player == firstPlayer) secondPlayer.writeMessage("You lose!") else firstPlayer.writeMessage("You lose!")
        }
        else {
          player.writeMessage("You lose!")
          if (player == firstPlayer) secondPlayer.writeMessage("You win") else firstPlayer.writeMessage("You win")
        }
      }
    }
    notifyAllPlayers("Game over!")
    endGame = true
    firstPlayer.isClosable = true
    secondPlayer.isClosable = true
  }

  def startPlay(): Unit = {
    notifyAllPlayers("The enemy has been found. Press the space + enter when you see the number three.")
    for (i <- 1 to 3) {
      if (endGame == false) {
        val random = new Random();
        Thread.sleep(random.nextInt(2001) + 2000)
        if (endGame == false)  notifyAllPlayers(i.toString())
      }
    }
    flag = true
  }

  def notifyAllPlayers(message: String): Unit = {
    firstPlayer.writeMessage(message)
    secondPlayer.writeMessage(message)
  }
}