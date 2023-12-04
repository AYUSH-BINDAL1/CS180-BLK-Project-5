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
    private static Object USERINFOLOCK; //LOCK for synchronizing Username.txt
    private static Object SHOPPINGCARTLOCK; //LOCK for synchronizing ShoppingCart.txt
    private static Object PURCHASEHISTORYLOCK; //LOCK for synchronizing PurchaseHistory.txt
    private static Object PRODUCTLOCK; //LOCK for synchronizing Product.txt


    //Constructor for new ClientHandler
    public ServerHandler(Socket clientSocket, Object USERINFOLOCK, Object SHOPPINGCARTLOCK,
                         Object PURCHASEHISTORYLOCK, Object PRODUCTLOCK) {
        this.clientSocket = clientSocket; //Sets up socket
        //Sets up LOCK for synchronization
        ServerHandler.USERINFOLOCK = USERINFOLOCK;
        ServerHandler.SHOPPINGCARTLOCK = SHOPPINGCARTLOCK;
        ServerHandler.PURCHASEHISTORYLOCK = PURCHASEHISTORYLOCK;
        ServerHandler.PRODUCTLOCK = PRODUCTLOCK;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates reader
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); //Creates writer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //TODO: Finish run method
    public void run() {
        String clientMessage = "";
        String[] commandSplit;
        String command;
        String result;
        do {
            try {
                clientMessage = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSplit = clientMessage.split(",");
            command = commandSplit[0];
            if (command.equalsIgnoreCase("REGISTER")) {
                result = AccountManager.registerServer(commandSplit[1], commandSplit[2], commandSplit[3],
                        USERINFOLOCK);
            } else if (command.equalsIgnoreCase("LOGIN")) {
                result = AccountManager.loginServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
            } else if (command.equalsIgnoreCase("EDIT USERNAME")) {
                result = AccountManager.updateEmailFiles(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK, PRODUCTLOCK);
            } else if (command.equalsIgnoreCase("EDIT PASSWORD")) {
                result = AccountManager.updatePasswordFiles(commandSplit[1], commandSplit[2], commandSplit[3],
                        USERINFOLOCK);
            } else if (command.equalsIgnoreCase("DELETE ACCOUNT")) {
                result = AccountManager.deleteAccount(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PRODUCTLOCK);
            } else if (command.equalsIgnoreCase("BUY PRODUCT")) {

            }  else if (command.equalsIgnoreCase("CHECKOUT CART")) {

            } else if (command.equalsIgnoreCase("ADD PRODUCT TO CART")) {

            } else if (command.equalsIgnoreCase("REMOVE PRODUCT FROM CART")) {

            } else if (command.equalsIgnoreCase("SEARCH BY NAME")) {

            } else if (command.equalsIgnoreCase("SEARCH BY STORE")) {

            } else if (command.equalsIgnoreCase("SEARCH BY DESCRIPTION")) {

            } else if (command.equalsIgnoreCase("SORT INCREASING PRICE")) {

            } else if (command.equalsIgnoreCase("SORT DECREASING PRICE")) {

            } else if (command.equalsIgnoreCase("SORT INCREASING QUANTITY")) {

            } else if (command.equalsIgnoreCase("SORT DECREASING QUANTITY")) {

            } else if (command.equalsIgnoreCase("VIEW PURCHASE HISTORY")) {

            } else if (command.equalsIgnoreCase("EXPORT PURCHASE HISTORY")) {

            }  else if (command.equalsIgnoreCase("VIEW CUSTOMER STATISTICS")) {

            }  else if (command.equalsIgnoreCase("VIEW SELLER PRODUCTS")) {

            }  else if (command.equalsIgnoreCase("MODIFY PRODUCT")) {

            }  else if (command.equalsIgnoreCase("DELETE PRODUCT")) {

            }  else if (command.equalsIgnoreCase("CREATE NEW PRODUCT")) {

            }  else if (command.equalsIgnoreCase("VIEW SALES BY STORE")) {

            }  else if (command.equalsIgnoreCase("IMPORT CSV")) {

            }  else if (command.equalsIgnoreCase("EXPORT CSV")) {

            }  else if (command.equalsIgnoreCase("VIEW SELLER STATISTICS")) {

            }  else if (command.equalsIgnoreCase("VIEW SELLER SHOPPING CART")) {

            } else if (command.equalsIgnoreCase("EXIT PROGRAM")) {
                exitProgram();
            }
        } while (!command.equalsIgnoreCase("EXIT PROGRAM"));

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
