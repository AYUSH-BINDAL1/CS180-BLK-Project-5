import javax.swing.*;
import java.io.*;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
    BufferedWriter writer; //Writer that writes String results to client socket
    ObjectOutputStream outputStream;  //Output Stream to write an ArrayList to the client socket
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
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream()); //Creates output stream
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSplit = clientMessage.split(",");
            command = commandSplit[0];
            if (command.equalsIgnoreCase("REGISTER")) {
                result = AccountManager.registerServer(commandSplit[1], commandSplit[2], commandSplit[3],
                        USERINFOLOCK);  //Parameters: email, password, userType
            } else if (command.equalsIgnoreCase("LOGIN")) {
                result = AccountManager.loginServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK); //Parameters: email, password, userType
            } else if (command.equalsIgnoreCase("EDIT USERNAME")) {
                result = AccountManager.updateEmailFiles(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK, PRODUCTLOCK); //Parameters: oldEmail, newEmail
            } else if (command.equalsIgnoreCase("EDIT PASSWORD")) {
                result = AccountManager.updatePasswordFiles(commandSplit[1], commandSplit[2], commandSplit[3],
                        USERINFOLOCK); //Parameters: oldEmail, oldPassword, newPassword
            } else if (command.equalsIgnoreCase("DELETE ACCOUNT")) {
                result = AccountManager.deleteAccount(commandSplit[1], commandSplit[2], USERINFOLOCK,
                        SHOPPINGCARTLOCK, PRODUCTLOCK); //Parameters: email, password
            } else if (command.equalsIgnoreCase("BUY PRODUCT")) {
                result =  CustomerShopping.buyProductServer(commandSplit[1], commandSplit[2], PURCHASEHISTORYLOCK,
                        PRODUCTLOCK); //Parameters: email, chosenProduct (Formatted String)
            } else if (command.equalsIgnoreCase("CHECKOUT CART")) {
                result = CustomerShopping.checkoutCartServer(commandSplit[1], SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK); //Parameters: email
            } else if (command.equalsIgnoreCase("ADD PRODUCT TO CART")) {
                result = CustomerShopping.addToCartServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK,
                        PRODUCTLOCK); //Parameter: email, chosenProduct (Formatted String)
            } else if (command.equalsIgnoreCase("VIEW CART")) {
            } else if (command.equalsIgnoreCase("REMOVE PRODUCT FROM CART")) {
                result = CustomerShopping.removeProductServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK,
                        PRODUCTLOCK); //Parameter: email, chonenProduct (Formatted String)
            } else if (command.equalsIgnoreCase("SEARCH BY NAME")) {
                resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK); //Parameter: Search Query
            } else if (command.equalsIgnoreCase("SEARCH BY STORE")) {
                resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK); //Parameter: Search Query
            } else if (command.equalsIgnoreCase("SEARCH BY DESCRIPTION")) {
                resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK); //Parameter: Search Query
            } else if (command.equalsIgnoreCase("SORT INCREASING PRICE")) {
                resultList = ProductSort.sortByIncreasingPriceServer(PRODUCTLOCK); //Parameter: NONE
            } else if (command.equalsIgnoreCase("SORT DECREASING PRICE")) {
                resultList = ProductSort.sortByDecreasingPriceServer(PRODUCTLOCK); //Parameter: NONE
            } else if (command.equalsIgnoreCase("SORT INCREASING QUANTITY")) {
                resultList = ProductSort.sortByIncreasingQuantityServer(PRODUCTLOCK); //Parameter: NONE
            } else if (command.equalsIgnoreCase("SORT DECREASING QUANTITY")) {
                resultList = ProductSort.sortByDecreasingQuantityServer(PRODUCTLOCK); //Parameter: NONE
            } else if (command.equalsIgnoreCase("VIEW PURCHASE HISTORY")) {
                resultList = PurchaseHistory.viewCustomerPurchaseHistoryServer(commandSplit[1], PURCHASEHISTORYLOCK); //Parameter: email
            } else if (command.equalsIgnoreCase("EXPORT PURCHASE HISTORY")) {
                result = PurchaseHistory.exportCustomerPurchaseHistoryServer(commandSplit[1], commandSplit[2],
                        PURCHASEHISTORYLOCK); //Parameter: email, FileName
            } else if (command.equalsIgnoreCase("VIEW CUSTOMER STATISTICS")) {
                resultList = Statistics.customerDashboardServer(commandSplit[1],PURCHASEHISTORYLOCK, PRODUCTLOCK);
            } else if (command.equalsIgnoreCase("VIEW CUSTOMER STATISTICS")) {
                resultList = Statistics.customerDashboardSpecificServer(commandSplit[1],
                        commandSplit[2], PURCHASEHISTORYLOCK);
            } else if (command.equalsIgnoreCase("VIEW SELLER PRODUCTS")) {
                resultList = SellerShopping.viewSellerProducts(commandSplit[1], PRODUCTLOCK); //Parameter: Email
            } else if (command.equalsIgnoreCase("MODIFY PRODUCT")) {
                result = SellerShopping.modifyProductServer(commandSplit[1], commandSplit[2],
                        commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                        Integer.parseInt(commandSplit[6]), commandSplit[7], SHOPPINGCARTLOCK, PRODUCTLOCK);
                //Parameter: Product Name, Description, Store, Seller Email, Price, Quantity, oldProduct (Formatted
                // String)
            } else if (command.equalsIgnoreCase("DELETE PRODUCT")) {
                result = SellerShopping.deleteProductServer(commandSplit[1], SHOPPINGCARTLOCK, PRODUCTLOCK);
                //oldProduct (Formatted String)
            } else if (command.equalsIgnoreCase("CREATE NEW PRODUCT")) {
                result = SellerShopping.createNewProductServer(commandSplit[1], commandSplit[2],
                        commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                        Integer.parseInt(commandSplit[6]), PRODUCTLOCK); //Parameter: Product Name, Description, Store, Seller Email, Price, Quantity
            } else if (command.equalsIgnoreCase("VIEW SALES BY STORE")) {
                result = Statistics.viewSalesByStoreServer(commandSplit[1], PURCHASEHISTORYLOCK); //Parameter: Seller
                // Name
            } else if (command.equalsIgnoreCase("IMPORT SELLER CSV")) {
                result = CSVHandler.importSellerCSVServer(commandSplit[1], PRODUCTLOCK); //Parameter: Path
            } else if (command.equalsIgnoreCase("EXPORT SELLER CSV")) {
                result = CSVHandler.exportSellerCSVServer(commandSplit[1], commandSplit[2], PRODUCTLOCK);
                //Parameter: path, sellerEmail
            } else if (command.equalsIgnoreCase("VIEW SELLER STATISTICS")) {
                result = Statistics.generateSellerDashboardServer(commandSplit[1], Integer.parseInt(commandSplit[2])
                        , PURCHASEHISTORYLOCK); //Parameter: sellerEmail, rank (integer either 1 or 0)
            } else if (command.equalsIgnoreCase("VIEW SELLER SHOPPING CART")) {
                resultList = SellerShopping.getSellerShoppingCartServer(commandSplit[1], PRODUCTLOCK); //Parameter:
                // sellerEmail
            } else if (command.equalsIgnoreCase("EXIT PROGRAM")) {
                exitProgram();
            }

            if(result.isEmpty()) {
                try {
                    outputStream.writeObject(resultList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(resultList.isEmpty()) {
                try {
                    writer.write(result);
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
