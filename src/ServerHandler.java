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
    BufferedReader reader;
    PrintWriter writer;

    //Constructor for new ClientHandler
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Finish run method
    public void run() {

    }


    //TODO: Create exitProgram method


}
