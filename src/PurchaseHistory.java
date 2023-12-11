import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * PurchaseHistory
 * <p>
 * Handles all purchase history information for the users
 *
 * @author Ayush Bindal & Lionel Loo Lab #L08
 * @version 12/11/2023
 * <p>
 */

public class PurchaseHistory {


    //Given a customer returns an ArrayList of Strings containing the customer's purchase history
    public static ArrayList<String> viewCustomerPurchaseHistoryServer(String customerEmail,
                                                                      Object PURCHASEHISTORYLOCK) {
        ArrayList<String> returnList = new ArrayList<>();
        try {
            ArrayList<String> purchaseHistoryLines; // List of all purchase lines (lines containing product
            // information)
            synchronized (PURCHASEHISTORYLOCK) {
                //Gets lines from PurchaseHistory.txt
                purchaseHistoryLines = (ArrayList<String>)
                        Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }

            for (String purchaseHistoryLine : purchaseHistoryLines) {
                String[] productLine = purchaseHistoryLine.split(","); //Splits the individual lines
                if (productLine[6].equals(customerEmail)) { //If the customer bought the item adds the line to
                    // ArrayList
                    returnList.add(purchaseHistoryLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList; //Returns ArrayList
    }


    //Given a path and customerEmail creates a new txt file with the customer's purchase history
    public static String exportCustomerPurchaseHistoryServer(String customerEmail, String path,
                                                             Object PURCHASEHISTORYLOCK) {

        try {
            ArrayList<String> purchaseHistoryLines; // List of all purchase lines (lines containing product
            // information)
            synchronized (PURCHASEHISTORYLOCK) {
                //Gets lines from PurchaseHistory.txt
                purchaseHistoryLines = (ArrayList<String>)
                        Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for (int i = 0; i < purchaseHistoryLines.size(); i++) {
                String[] productLine = purchaseHistoryLines.get(i).split(","); //Splits the individual lines
                if (!productLine[6].equals(customerEmail)) { //If the customer didn't buy the item it removes the line
                    purchaseHistoryLines.remove(i);
                    i--; //Accounts for removal of line
                }
            }
            purchaseHistoryLines.add(0, "Product Name,Product Description,Store Name,Seller Email," +
                    "Price,Amount,Customer Email");

            synchronized (PURCHASEHISTORYLOCK) {
                Files.write(Paths.get(path), purchaseHistoryLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }


}

