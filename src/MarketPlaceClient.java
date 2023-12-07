import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

/**
 * MarketPlace
 * <p>
 * Java class that starts a new client and starts a thread for that client
 *
 * @author Ayush Bindal, Lab #L08
 * @version 12/05/2023
 * <p>
 */

public class MarketPlaceClient {
        public static void main(String[] args) {
            Socket clientSocket;
            GUI clientGUI;
            try {
                clientSocket = new Socket("localhost", 6969);
                clientGUI = new GUI(clientSocket);
                Thread newThread = new Thread(clientGUI);
                SwingUtilities.invokeLater(() -> {
                    clientGUI.setVisible(true);
                });
                newThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

