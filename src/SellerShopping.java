import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * SellerShopping
 * <p>
 * Handles all methods pertaining to the seller shopping cart information
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class SellerShopping {

    //TODO: Create getSellerShoppingCartClient method
    //TODO: Create getSellerShoppingCartServer method

    //TODO: Create modifyProductClient method
    //TODO: Create modifyProductServer method

    //TODO: Create deleteProductClient method
    //TODO: Create deleteProductServer method

    //TODO: Create createNewProductClient method
    //TODO: Create createNewProductServer method

    //TODO: Create viewAllProductsClient method
    public ArrayList<String> viewAllProductsServer(String email, Object LOCK) {
        ArrayList<String> productLines = new ArrayList<>(); //ArrayList of lines from Product.txt
        try {
            synchronized (LOCK) {
                //Reads lines from Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for(int i = 0; i < productLines.size(); i++) {
                String[] productSplit = productLines.get(i).split(","); //Splits the productLines
                if(!productSplit[3].equals(email)) {
                    productLines.remove(i);
                    i--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productLines;
    }

    //TODO: Create viewShoppingCartsClient method
    public ArrayList<String> viewShoppingCartsServer(String email, Object LOCK) {
        ArrayList<String> shoppingCartLines = new ArrayList<>(); //ArrayList of lines from ShoppingCart.txt
        try {
            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for(int i = 0; i < shoppingCartLines.size(); i++) {
                String[] productSplit = shoppingCartLines.get(i).split(","); //Splits the ShoppingCart line
                if(!productSplit[3].equals(email)) {
                    shoppingCartLines.remove(i);
                    i--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shoppingCartLines;
    }



}
