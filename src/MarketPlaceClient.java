import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

/**
 * MarketPlaceClient
 * <p>
 * Java class that starts a new client and starts a thread for that client. Starts GUI also
 *
 * @author Ayush Bindal, Lab #L08
 * @version 12/05/2023
 * <p>
 */

public class MarketPlaceClient {
        public static void main(String[] args) {
            Socket clientSocket; //New Client Socket
            GUI clientGUI;
            try {
                clientSocket = new Socket("localhost", 6969); //Connects to Port: 6969
                clientGUI = new GUI(clientSocket); //Creates new GUI
                Thread newThread = new Thread(clientGUI); //Creates new Thread
                SwingUtilities.invokeLater(() -> {
                    clientGUI.setVisible(true); //Shows GUI
                });
                newThread.start(); //Starts Thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

