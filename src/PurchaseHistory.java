import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * PurchaseHistory
 * <p>
 * Handles all purchase history information for the users
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 * Sources: [TA NAMES]
 */

public class PurchaseHistory {
    //TODO: Create viewCustomerPurchaseHistoryClient method
    //TODO: Create viewCustomerPurchaseHistoryServer method

    //TODO: Create exportCustomerPurchaseHistoryClient method
    //TODO: Create exportCustomerPurchaseHistoryServer method
    public static void exportCustomerPurchaseHistoryServer(String customerEmail, String path, Object LOCK) {
        try {
            ArrayList<String> purchaseHistoryLines; // List of all purchases (lines containing product
            // information)
            synchronized (LOCK) {
                //Gets lines
                purchaseHistoryLines = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for(int i = 0; i < purchaseHistoryLines.size(); i++) {
                String[] productLine = purchaseHistoryLines.get(i).split(","); //Splits the individual lines
                if(!productLine[6].equals(customerEmail)) { //If the customer didn't buy the item it removes the line
                    purchaseHistoryLines.remove(i);
                    i--; //Accounts for removal of line
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get(path), purchaseHistoryLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
