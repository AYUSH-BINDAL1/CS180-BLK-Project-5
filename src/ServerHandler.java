import javax.swing.*;
import java.io.*;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * ClientHandler
 * <p>
 * Java class that implements Runnable used to create new Threads for new connections to the Server
 *
 * @author Ayush Bindal, Lab #L08
 * @version 12/02/2023
 * <p>
 */

public class ServerHandler implements Runnable {

    private Socket clientSocket; //Individual Client Socket
    BufferedReader reader; //Reader that reads from the client socket to the server
    BufferedWriter writer; //Writer that writes from the server to the client socket
    private static Object LOCK; //LOCK for synchronizing

    //Constructor for new ClientHandler
    public ServerHandler(Socket clientSocket, Object LOCK) {
        this.clientSocket = clientSocket; //Sets up socket
        ServerHandler.LOCK = LOCK; //Sets up LOCK for synchronization
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates reader
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); //Creates writer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Finish run method
    public void run() {

    }


    public void exitProgram() {
        JOptionPane.showMessageDialog(null, "Thanks for using the Marketplace!", "Goodbye",
                JOptionPane.INFORMATION_MESSAGE);
        try {
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
