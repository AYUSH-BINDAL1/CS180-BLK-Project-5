import javax.swing.*;
import java.io.*;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * ServerHandler
 * <p>
 * Java class that implements Runnable used to create new Threads for new connections to the Server
 *
 * @author Ayush Bindal, Lab #L08
 * @version 12/02/2023
 * <p>
 */

public class ServerHandler implements Runnable {

    private final Socket clientSocket; //Individual Client Socket
    BufferedReader reader; //Reader that reads from the client socket to the server
    ObjectOutputStream writer;  //Output Stream to write an ArrayList or String to the client socket
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
            writer = new ObjectOutputStream(clientSocket.getOutputStream()); //Creates output stream
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
        ArrayList<String> resultList = new ArrayList<>();
        do {
            result = "";
            resultList.clear();
            try {
                clientMessage = reader.readLine();
                System.out.println(clientMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSplit = clientMessage.split(",");
            command = commandSplit[0];
            switch (command.toUpperCase()) {
                case "GET ALL PRODUCTS" -> resultList = CustomerShopping.getAllProducts(PRODUCTLOCK);
                case "REGISTER" -> result = AccountManager.registerServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                case "LOGIN" -> result = AccountManager.loginServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                case "EDIT USERNAME" -> result = AccountManager.updateEmailFiles(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK, PRODUCTLOCK);
                case "EDIT PASSWORD" -> result = AccountManager.updatePasswordFiles(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                case "DELETE ACCOUNT" -> result = AccountManager.deleteAccount(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PRODUCTLOCK);
                case "BUY PRODUCT" -> result = CustomerShopping.buyProductServer(commandSplit[1], commandSplit[2], PURCHASEHISTORYLOCK,
                        PRODUCTLOCK);
                case "CHECKOUT CART" -> result = CustomerShopping.checkoutCartServer(commandSplit[1], SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK,
                        PRODUCTLOCK);
                case "ADD PRODUCT TO CART" -> result = CustomerShopping.addToCartServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK,
                        PRODUCTLOCK);
                case "REMOVE PRODUCT FROM CART" -> result = CustomerShopping.removeProductServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK,
                        PRODUCTLOCK);
                case "SEARCH BY NAME", "SEARCH BY STORE", "SEARCH BY DESCRIPTION" -> resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK);
                case "SORT INCREASING PRICE" -> resultList = ProductSort.sortByIncreasingPriceServer(PRODUCTLOCK);
                case "SORT DECREASING PRICE" -> resultList = ProductSort.sortByDecreasingPriceServer(PRODUCTLOCK);
                case "SORT INCREASING QUANTITY" -> resultList = ProductSort.sortByIncreasingQuantityServer(PRODUCTLOCK);
                case "SORT DECREASING QUANTITY" -> resultList = ProductSort.sortByDecreasingQuantityServer(PRODUCTLOCK);
                case "VIEW PURCHASE HISTORY" -> resultList = PurchaseHistory.viewCustomerPurchaseHistoryServer(commandSplit[1], PURCHASEHISTORYLOCK);
                case "EXPORT PURCHASE HISTORY" -> result = PurchaseHistory.exportCustomerPurchaseHistoryServer(commandSplit[1], commandSplit[2],
                        PURCHASEHISTORYLOCK);
                case "VIEW CUSTOMER STATISTICS" -> resultList = Statistics.customerDashboardServer(commandSplit[1], PURCHASEHISTORYLOCK, PRODUCTLOCK);
                case "VIEW CUSTOMER STATISTICS SPECIFIC" -> resultList = Statistics.customerDashboardSpecificServer(commandSplit[1],
                        commandSplit[2], PURCHASEHISTORYLOCK);
                case "VIEW SELLER PRODUCTS" -> resultList = SellerShopping.viewSellerProducts(commandSplit[1], PRODUCTLOCK);
                case "MODIFY PRODUCT" -> result = SellerShopping.modifyProductServer(commandSplit[1], commandSplit[2],
                        commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                        Integer.parseInt(commandSplit[6]), commandSplit[7], SHOPPINGCARTLOCK, PRODUCTLOCK);
                case "DELETE PRODUCT" -> result = SellerShopping.deleteProductServer(commandSplit[1], SHOPPINGCARTLOCK, PRODUCTLOCK);
                case "CREATE NEW PRODUCT" -> result = SellerShopping.createNewProductServer(commandSplit[1], commandSplit[2],
                        commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                        Integer.parseInt(commandSplit[6]), PRODUCTLOCK);
                case "VIEW SALES BY STORE" -> result = Statistics.viewSalesByStoreServer(commandSplit[1], PURCHASEHISTORYLOCK);
                case "IMPORT SELLER CSV" -> result = CSVHandler.importSellerCSVServer(commandSplit[1], PRODUCTLOCK);
                case "EXPORT SELLER CSV" -> result = CSVHandler.exportSellerCSVServer(commandSplit[1], commandSplit[2], PRODUCTLOCK);
                case "VIEW SELLER STATISTICS" -> result = Statistics.generateSellerDashboardServer(commandSplit[1], Integer.parseInt(commandSplit[2]),
                        PURCHASEHISTORYLOCK);
                case "VIEW SELLER SHOPPING CART" -> resultList = SellerShopping.getSellerShoppingCartServer(commandSplit[1], PRODUCTLOCK);
                case "EXIT PROGRAM" -> exitProgram();
                default -> System.out.println("ERROR, MESSAGE FROM CLIENT IS INVALID");
            }

            System.out.println(result);
            if (result.isEmpty()) {
                try {
                    writer.writeObject(resultList);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(resultList.isEmpty()) {
                try {
                    writer.writeObject(result);
                    writer  .flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
