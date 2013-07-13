import java.io._
import scala.io._
import java.net.{InetAddress,ServerSocket,Socket,SocketException}
import java.util.Random

object ChattyServer {

	def main( args: Array[String]) {
		println("Starting Chatty Server")
		try{
			val listener = new ServerSocket(9999)
			println("Listening....")
			while(true){
				new ChattyThread(listener.accept()).start()
			}
			listener.close();
		}catch {
	      case e: IOException =>
	        System.err.println("Could not listen on port: 9999.");
	        System.exit(-1)
	    }
	}

}

case class ChattyThread(socket: Socket) extends Thread("ChattyThread") {

  override def run(): Unit = {
  	
	try {
    	val out = new PrintStream(socket.getOutputStream())
    	out.println("Welcome to Chatty, type quit to leave")
        val m = new Markov("source.txt")
    	var keepListening = true
		while (keepListening) {
			
      		val in = new BufferedSource(socket.getInputStream()).getLines()
      		var input = in.next()
      		if( input == "quit"){
      			out.println("Goodbye!")
      			keepListening = false
      		}else{
                var response = m.startWithFor(input, 200)
                out.println(response)
    		}
      	}

      	out.close();
      	socket.close()
    }
    catch {
      case e: SocketException =>
        () // avoid stack trace when stopping a client with Ctrl-C
      case e: IOException =>
        e.printStackTrace();
    }
  }

}