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
 * @version 11/30/2023
 * <p>
 */

public class CSVHandler {

    private static final Object LOCK = new Object();
    //TODO: Create importSellerCSVClient method

    public static void importSellerCSVServer(String path) {
        try {
            ArrayList<String> sellerCSVLines = (ArrayList<String>) Files.readAllLines(Paths.get(path));
            ArrayList<String> productLines = (ArrayList<String>)Files.readAllLines(Paths.get("Product.txt"));

            sellerCSVLines.remove(0); // Remove the first line (index 0)

            for (int i = 0; i < sellerCSVLines.size(); i++) {
                String[] sellerCSVLine = sellerCSVLines.get(i).split(",");
                if(sellerCSVLine.length != 6) {
                    continue;
                }
                if(productLines.contains(sellerCSVLines.get(i))) {
                    continue;
                }
                productLines.add(sellerCSVLines.get(i));
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), productLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Create importSellerCSVClient method


    public static void exportSellerCSVServer(String path, String sellerEmail) {
        try {
            ArrayList<String> productLines = (ArrayList<String>)Files.readAllLines(Paths.get("Product.txt"));
            for(int i = 0; i < productLines.size(); i++) {
                String[] productLine = productLines.get(i).split(",");
                if(!productLine[3].equals(sellerEmail)) {
                    productLines.remove(i);
                    i--;
                }
            }
            Files.write(Paths.get(path), productLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
