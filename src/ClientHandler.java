import java.net.Socket;

/**
 * ClientHandler
 * <p>
 * Java class that implements Runnable used to create new Threads for new connections to the Server
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    //Constructor for new ClientHandler
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //TODO: Finish run method
    public void run() {

    }


    //TODO: Create exitProgram method


}
