import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * CSVHandler
 * <p>
 * Java class that handles all CSV handling including export and import
 *
 * @author Ayush Bindal, Lab #L08
 * @version 12/2/2023
 * <p>
 */

public class CSVHandler {


    //Given a path to a seller's CSV file, import the seller's products into the Product.txt file.
    public static String importSellerCSVServer(String path, Object PRODUCTLOCK) {
        Path pathCheck = Paths.get(path);
        if(Files.exists(pathCheck)) {
            try {
                Object CSVLOCK = new Object();
                ArrayList<String> sellerCSVLines; // List of seller CSV lines (lines containing seller information)
                ArrayList<String> productLines; // List of product lines (lines containing product information)
                synchronized (CSVLOCK) {
                    sellerCSVLines = (ArrayList<String>) Files.readAllLines(Paths.get(path));
                }
                synchronized (PRODUCTLOCK) {
                    //Gets information from text file
                    productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
                }
                sellerCSVLines.remove(0); // Remove the first line (index 0) containing formatting

                for (String csvLine : sellerCSVLines) {
                    String[] sellerCSVLine = csvLine.split(",");
                    if (sellerCSVLine.length != 6) { //Skips the line if not formatted properly
                        continue;
                    }
                    if (productLines.contains(csvLine)) { //Skips line if product is already found in Product.txt
                        continue;
                    }
                    productLines.add(csvLine); //Appends the csvLine if it is valid
                }
                synchronized (PRODUCTLOCK) {
                    Files.write(Paths.get("Product.txt"), productLines); //Rewrites the Product.txt
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        } else {
            return "PATH DOES NOT EXIST";
        }
    }
    

    //Given the sellerEmail and path writes Products the seller owns to a new CSV file
    public static String exportSellerCSVServer(String path, String sellerEmail, Object PRODUCTLOCK) {
        Path pathCheck = Paths.get(path);
        if(!Files.exists(pathCheck)) {
            try {
                ArrayList<String> productLines; // List of product lines (lines containing product information)
                synchronized (PRODUCTLOCK) {
                    //Gets lines
                    productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
                }
                for (int i = 0; i < productLines.size(); i++) {
                    String[] productLine = productLines.get(i).split(","); //Splits the individual lines
                    if (!productLine[3].equals(sellerEmail)) { //If the seller doesn't own the product it removes the line
                        productLines.remove(i);
                        i--; //Accounts for removal of line
                    }
                }
                synchronized (PRODUCTLOCK) {
                    Files.write(Paths.get(path), productLines);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        } else {
            return "PATH TAKEN";
        }
    }


}
