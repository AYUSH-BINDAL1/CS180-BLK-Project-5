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


    public void run() {
        String clientMessage = "";
        String[] commandSplit = null;
        String command = "";
        String result = "";
        ArrayList<String> resultList = new ArrayList<>();
        do {
            try {
                if (reader.ready()) { // Check if data is available to read
                    clientMessage = reader.readLine();
                    commandSplit = clientMessage.split(",");
                    command = commandSplit[0];
                    switch (command.toUpperCase()) {
                        // Formatted String: GET ALL PRODUCTS
                        case "GET ALL PRODUCTS":
                            resultList = ServerHandler.getAllProducts();
                            break;

                        // Formatted String: REGISTER,email,password,userType
                        case "REGISTER":
                            result = AccountManager.registerServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                            break;

                        // Formatted String: LOGIN,email,password,userType
                        case "LOGIN":
                            result = AccountManager.loginServer(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                            break;

                        // Formatted String: EDIT USERNAME,oldEmail,newEmail
                        case "EDIT USERNAME":
                            result = AccountManager.updateEmailFiles(commandSplit[1], commandSplit[2], USERINFOLOCK,
                                    SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: EDIT PASSWORD,email,oldPassword,newPassword
                        case "EDIT PASSWORD":
                            result = AccountManager.updatePasswordFiles(commandSplit[1], commandSplit[2], commandSplit[3], USERINFOLOCK);
                            break;

                        // Formatted String: DELETE ACCOUNT,email,password
                        case "DELETE ACCOUNT":
                            result = AccountManager.deleteAccount(commandSplit[1], commandSplit[2], USERINFOLOCK,
                                    SHOPPINGCARTLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: BUY PRODUCT,email,chosenProduct
                        case "BUY PRODUCT":
                            result = CustomerShopping.buyProductServer(commandSplit[1], commandSplit[2], PURCHASEHISTORYLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: CHECKOUT CART,email
                        case "CHECKOUT CART":
                            result = CustomerShopping.checkoutCartServer(commandSplit[1], SHOPPINGCARTLOCK, PURCHASEHISTORYLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: ADD PRODUCT TO CART,email,chosenProduct
                        case "ADD PRODUCT TO CART":
                            result = CustomerShopping.addToCartServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: REMOVE PRODUCT FROM CART,email,chosenProduct
                        case "REMOVE PRODUCT FROM CART":
                            result = CustomerShopping.removeProductServer(commandSplit[1], commandSplit[2], SHOPPINGCARTLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: SEARCH BY NAME,name
                        case "SEARCH BY NAME":
                            resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK);
                            // Formatted String: SEARCH BY STORE,store
                        case "SEARCH BY STORE":
                            resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK);
                            // Formatted String: SEARCH BY DESCRIPTION,description
                        case "SEARCH BY DESCRIPTION":
                            resultList = ProductSearch.searchByNameServer(commandSplit[1], PRODUCTLOCK);
                            break;

                        // Formatted String: SORT INCREASING PRICE
                        case "SORT INCREASING PRICE":
                            resultList = ProductSort.sortByIncreasingPriceServer(PRODUCTLOCK);
                            break;

                        // Formatted String: SORT DECREASING PRICE
                        case "SORT DECREASING PRICE":
                            resultList = ProductSort.sortByDecreasingPriceServer(PRODUCTLOCK);
                            break;

                        // Formatted String: SORT INCREASING QUANTITY
                        case "SORT INCREASING QUANTITY":
                            resultList = ProductSort.sortByIncreasingQuantityServer(PRODUCTLOCK);
                            break;

                        // Formatted String: SORT DECREASING QUANTITY
                        case "SORT DECREASING QUANTITY":
                            resultList = ProductSort.sortByDecreasingQuantityServer(PRODUCTLOCK);
                            break;

                        // Formatted String: VIEW PURCHASE HISTORY,email
                        case "VIEW PURCHASE HISTORY":
                            resultList = PurchaseHistory.viewCustomerPurchaseHistoryServer(commandSplit[1], PURCHASEHISTORYLOCK);
                            break;

                        // Formatted String: EXPORT PURCHASE HISTORY,email,file path
                        case "EXPORT PURCHASE HISTORY":
                            result = PurchaseHistory.exportCustomerPurchaseHistoryServer(commandSplit[1], commandSplit[2],
                                    PURCHASEHISTORYLOCK);
                            break;

                        // Formatted String: VIEW CUSTOMER STATISTICS,email
                        case "VIEW CUSTOMER STATISTICS":
                            resultList = Statistics.customerDashboardServer(commandSplit[1], PURCHASEHISTORYLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: VIEW CUSTOMER STATISTICS SPECIFIC,email,rank (Either 1 or 0)
                        case "VIEW CUSTOMER STATISTICS SPECIFIC":
                            resultList = Statistics.customerDashboardSpecificServer(commandSplit[1], commandSplit[2],
                                    PURCHASEHISTORYLOCK);
                            break;

                        // Formatted String: VIEW SELLER PRODUCTS,email
                        case "VIEW SELLER PRODUCTS":
                            resultList = SellerShopping.viewSellerProducts(commandSplit[1], PRODUCTLOCK);
                            break;

                        // Formatted String: MODIFY PRODUCT,email,Product Name,Product Description,Store Name, Seller Email,
                        // Price,Quantity,Chosen Product
                        case "MODIFY PRODUCT":
                            result = SellerShopping.modifyProductServer(commandSplit[1], commandSplit[2],
                                    commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                                    Integer.parseInt(commandSplit[6]), commandSplit[7], SHOPPINGCARTLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: DELETE PRODUCT,Chosen Product
                        case "DELETE PRODUCT":
                            result = SellerShopping.deleteProductServer(clientMessage, SHOPPINGCARTLOCK, PRODUCTLOCK);
                            break;

                        // Formatted String: CREATE NEW PRODUCT,Product Name,Product Description,Store Name, Seller Email,Price,Quantity
                        case "CREATE NEW PRODUCT":
                            result = SellerShopping.createNewProductServer(commandSplit[1], commandSplit[2],
                                    commandSplit[3], commandSplit[4], Double.parseDouble(commandSplit[5]),
                                    Integer.parseInt(commandSplit[6]), PRODUCTLOCK);
                            break;

                        // Formatted String: VIEW SALES BY STORE,sellerEmail
                        case "VIEW SALES BY STORE":
                            result = Statistics.viewSalesByStoreServer(commandSplit[1], PURCHASEHISTORYLOCK);
                            break;

                        // Formatted String: IMPORT SELLER CSV,File path
                        case "IMPORT SELLER CSV":
                            result = CSVHandler.importSellerCSVServer(commandSplit[1], PRODUCTLOCK);
                            break;

                        // Formatted String: EXPORT SELLER CSV,File Path,Seller Email
                        case "EXPORT SELLER CSV":
                            result = CSVHandler.exportSellerCSVServer(commandSplit[1], commandSplit[2], PRODUCTLOCK);
                            break;

                        // Formatted String: VIEW SELLER STATISTICS,sellerEmail,rank (Either 1 or 0)
                        case "VIEW SELLER STATISTICS":
                            result = Statistics.generateSellerDashboardServer(commandSplit[1], Integer.parseInt(commandSplit[2]),
                                    PURCHASEHISTORYLOCK);
                            break;

                        // Formatted String: VIEW SELLER SHOPPING CART,sellerEmail
                        case "VIEW SELLER SHOPPING CART":
                            resultList = SellerShopping.getSellerShoppingCartServer(commandSplit[1], PRODUCTLOCK);
                            break;

                        // Exit the program
                        case "EXIT PROGRAM":
                            exitProgram();
                            break;

                        default:
                            // Handle unknown command
                            break;
                    }

                    if (!result.isEmpty()) {
                        writer.writeObject(result);
                    } else {
                        writer.writeObject(resultList);
                    }
                    writer.flush();
                    result = "";
                    resultList.clear();
                } else {
                    Thread.sleep(100); //Wait for a short duration if no data is available
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } while (!command.equalsIgnoreCase("EXIT PROGRAM"));
    }


    //Returns ArrayList of All Products in Product.txt
    public static ArrayList<String> getAllProducts() {
        ArrayList<String> productLines = new ArrayList<String>();
        try {
            //Reads lines from Product.txt
            synchronized (PRODUCTLOCK) {
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productLines;
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
