import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * MarketPlaceServer
 * <p>
 * Java class that starts the server and accepts connections from clients and starts individual threads for them.
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class MarketPlaceServer {

    public static void main(String[] args) {
        ServerSocket serverSocket; //Creates ServerSocket
        Socket clientSocket; //Creates clientSocket
        ServerHandler serverHandler; //Creates ServerHandler
        try {

            serverSocket = new ServerSocket(6969); //PORT NUMBER: 6969
            serverSocket.setReuseAddress(true);

            //TODO: IF NEEDED put any booting of ArrayList or Files here.

            while(true) {
                clientSocket = serverSocket.accept(); //Continues to accept connections to the server
                JOptionPane.showMessageDialog(null, "Connection to Server Established",
                        "Connection Established", JOptionPane.INFORMATION_MESSAGE);
                serverHandler = new ServerHandler(clientSocket);
                Thread thread = new Thread(serverHandler); //Creates new thread
                thread.start(); //Starts thread

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
