import javax.swing.*;
import java.io.File;
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
 * @version 12/11/2023
 * <p>
 */

public class MarketPlaceServer {

    //Creates "global" Object LOCKS for synchronizing specific files
    public static final Object USERINFOLOCK = new Object();

    public static final Object SHOPPINGCARTLOCK = new Object();
    public static final Object PURCHASEHISTORYLOCK = new Object();
    public static final Object PRODUCTLOCK = new Object();


    public static void main(String[] args) {
        ServerSocket serverSocket; //Creates ServerSocket
        Socket clientSocket; //Creates clientSocket
        ServerHandler serverHandler; //Creates ServerHandler

        File usernameFile = new File("Username.txt");
        File shoppingCartFile = new File("ShoppingCart.txt");
        File purchaseHistoryFile = new File("PurchaseHistory.txt");
        File productFile = new File("Product.txt");
        try {
            if (!usernameFile.exists()) {
                usernameFile.createNewFile();
            }
            if (!shoppingCartFile.exists()) {
                shoppingCartFile.createNewFile();
            }
            if (!purchaseHistoryFile.exists()) {
                purchaseHistoryFile.createNewFile();
            }
            if (!productFile.exists()) {
                productFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            serverSocket = new ServerSocket(6969); //PORT NUMBER: 6969
            serverSocket.setReuseAddress(true);

            while (true) {
                clientSocket = serverSocket.accept(); //Continues to accept connections to the server
                JOptionPane.showMessageDialog(null, "Successfully Connected. " +
                                "Welcome to the Marketplace.",
                        "Connection Established", JOptionPane.INFORMATION_MESSAGE);
                serverHandler = new ServerHandler(clientSocket, USERINFOLOCK, SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK,
                        PRODUCTLOCK); //Creates new serverHandler with clientSocket; //Creates new serverHandler with
                // clientSocket and LOCK
                Thread thread = new Thread(serverHandler); //Creates new thread
                thread.start(); //Starts thread

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
