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
    //Gets all products from ShoppingCart.txt and returns all products that belong to the seller
    public static ArrayList<String> getSellerShoppingCartServer(String sellerEmail, Object LOCK) {
        ArrayList<String> allProducts;
        ArrayList<String> sellerShoppingCart = new ArrayList<>();
        try {
            synchronized (LOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            //loops through all products and adds all products that belong to the seller to sellerShoppingCart arraylist
            for (int i = 0; i < allProducts.size(); i++) {
                String[] currentProduct = allProducts.get(i).split(","); //splits current product by comma
                if (currentProduct[3].equals(sellerEmail)) {
                    sellerShoppingCart.add(allProducts.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sellerShoppingCart; //returns
    }



    //TODO: Create modifyProductClient method
    //TODO: Create modifyProductServer method
    public static void modifyProductServer(String productName, String productDescription, String storeName,
                                           String sellerEmail, double price, int quantity, String oldProduct,
                                           Object LOCK) {
        ArrayList<String> allShoppingCart;
        ArrayList<String> allProducts;
        String[] oldProductSplit = oldProduct.split(","); //splits old product by comma
        try {
            synchronized (LOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allShoppingCart = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
                //creates arraylist of all products from Product.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++)  {
                String[] productSplit = allProducts.get(i).split(","); //splits current product by comma
                    if(productSplit[0].equals(oldProductSplit[0]) && productSplit[2].equals(oldProductSplit[2])
                            && productSplit[3].equals(oldProductSplit[3])) {
                        allProducts.set(i,
                                productName + "," + productDescription + "," + storeName + "," + sellerEmail + "," + price + "," + quantity);
                    } 
            }
            for (int i = 0; i < allShoppingCart.size(); i++) {
                String[] shoppingCartSplit = allShoppingCart.get(i).split(","); //splits current product by comma(",");
                if(shoppingCartSplit[0].equals(oldProductSplit[0]) && shoppingCartSplit[2].equals(oldProductSplit[2])
                        && shoppingCartSplit[3].equals(oldProductSplit[3])) {
                    allShoppingCart.set(i,
                            productName + "," + productDescription + "," + storeName + "," + sellerEmail + "," + price + "," + quantity + "," + shoppingCartSplit[6]);
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), allProducts);
                Files.write(Paths.get("ShoppingCart.txt"), allShoppingCart);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //TODO: Create deleteProductClient method
    //TODO: Create deleteProductServer method
    public static void deleteProductServer(String oldProduct, Object LOCK) {
        ArrayList<String> allShoppingCart;
        ArrayList<String> allProducts;
        String[] oldProductSplit = oldProduct.split(","); //splits old product by comma
        try {
            synchronized (LOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allShoppingCart = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
                //creates arraylist of all products from Product.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++)  {
                String[] productSplit = allProducts.get(i).split(","); //splits current product by comma
                if(productSplit[0].equals(oldProductSplit[0]) && productSplit[2].equals(oldProductSplit[2])
                        && productSplit[3].equals(oldProductSplit[3])) {
                    allProducts.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < allShoppingCart.size(); i++) {
                String[] shoppingCartSplit = allShoppingCart.get(i).split(","); //splits current product by comma(",");
                if(shoppingCartSplit[0].equals(oldProductSplit[0]) && shoppingCartSplit[2].equals(oldProductSplit[2])
                        && shoppingCartSplit[3].equals(oldProductSplit[3])) {
                    allShoppingCart.remove(i); //removes product from shopping cart (if it exists in shopping cart already)
                    i--; //Accounts for removal (if it exists in shopping cart already)
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), allProducts);
                Files.write(Paths.get("ShoppingCart.txt"), allShoppingCart);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
