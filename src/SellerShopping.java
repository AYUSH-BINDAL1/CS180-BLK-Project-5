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

    //Gets all products from ShoppingCart.txt and returns all products that belong to the seller
    public static ArrayList<String> getSellerShoppingCartServer(String sellerEmail, Object SHOPPINGCARTLOCK) {
        ArrayList<String> allProducts;
        ArrayList<String> sellerShoppingCart = new ArrayList<>();
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            //loops through all products and adds all products belonging to seller sellerShoppingCart arraylist
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
    public static void modifyProductServer(String productName, String productDescription, String storeName,
                                           String sellerEmail, double price, int quantity, String oldProduct,
                                           Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        ArrayList<String> allShoppingCart;
        ArrayList<String> allProducts;
        String[] oldProductSplit = oldProduct.split(","); //splits old product by comma
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allShoppingCart = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            synchronized (PRODUCTLOCK) {
                //creates arraylist of all products from Product.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++) {
                String[] productSplit = allProducts.get(i).split(","); //splits current product by comma
                if (productSplit[0].equals(oldProductSplit[0]) && productSplit[2].equals(oldProductSplit[2])
                        && productSplit[3].equals(oldProductSplit[3])) {
                    allProducts.set(i,
                            productName + "," + productDescription + "," + storeName + "," + sellerEmail + ","
                                    + price + "," + quantity);
                }
            }
            for (int i = 0; i < allShoppingCart.size(); i++) {
                String[] shoppingCartSplit = allShoppingCart.get(i).split(","); //splits current product by comma(",");
                if (shoppingCartSplit[0].equals(oldProductSplit[0]) && shoppingCartSplit[2].equals(oldProductSplit[2])
                        && shoppingCartSplit[3].equals(oldProductSplit[3])) {
                    allShoppingCart.set(i,
                            productName + "," + productDescription + "," + storeName + "," + sellerEmail + ","
                                    + price + "," + quantity + "," + shoppingCartSplit[6]);
                }
            }
            synchronized (SHOPPINGCARTLOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), allShoppingCart);
            }
            synchronized (PRODUCTLOCK) {
                Files.write(Paths.get("Product.txt"), allProducts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //TODO: Create deleteProductClient method
    public static void deleteProductServer(String oldProduct, Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        ArrayList<String> allShoppingCart;
        ArrayList<String> allProducts;
        String[] oldProductSplit = oldProduct.split(","); //splits old product by comma
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //creates arraylist of all products from ShoppingCart.txt
                allShoppingCart = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            synchronized (PRODUCTLOCK) {
                //creates arraylist of all products from Product.txt
                allProducts = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < allProducts.size(); i++) {
                String[] productSplit = allProducts.get(i).split(","); //splits current product by comma
                if (productSplit[0].equals(oldProductSplit[0]) && productSplit[2].equals(oldProductSplit[2])
                        && productSplit[3].equals(oldProductSplit[3])) {
                    allProducts.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < allShoppingCart.size(); i++) {
                String[] shoppingCartSplit = allShoppingCart.get(i).split(","); //Splits by product ","
                if (shoppingCartSplit[0].equals(oldProductSplit[0]) && shoppingCartSplit[2].equals(oldProductSplit[2])
                        && shoppingCartSplit[3].equals(oldProductSplit[3])) {
                    allShoppingCart.remove(i); //removes product from shopping cart (exists in shopping cart )
                    i--; //Accounts for removal (if it exists in shopping cart already)
                }
            }
            synchronized (SHOPPINGCARTLOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), allShoppingCart);
            }
            synchronized (PRODUCTLOCK) {
                Files.write(Paths.get("Product.txt"), allProducts);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Create createNewProductClient method
    public static String createNewProductServer(String productName, String productDescription, String storeName,
                                                String sellerEmail, double price, int quantity, Object PRODUCTLOCK) {
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        boolean productExists; //boolean to check if product already exists
        try {
            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < productLines.size(); i++) {
                String[] productSplit = productLines.get(i).split(","); //Splits the productLines
                if (productSplit[0].equals(productName) && productSplit[2].equals(storeName) &&
                        productSplit[3].equals(sellerEmail)) { //If the product already exists
                    return "PRODUCT ALREADY EXISTS"; //returns error message if product already exists
                }
            }
            productLines.add(productName + "," + productDescription + "," + storeName + "," + sellerEmail + ","
                    + price + "," + quantity); //Creates new formatted product
            synchronized (PRODUCTLOCK) {
                Files.write(Paths.get("Product.txt"), productLines);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "PRODUCT CREATED";
    }


    //TODO: Create viewAllProductsClient method
    public ArrayList<String> viewAllProductsServer(String email, Object PRODUCTLOCK) {
        ArrayList<String> productLines = new ArrayList<>(); //ArrayList of lines from Product.txt
        try {
            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < productLines.size(); i++) {
                String[] productSplit = productLines.get(i).split(","); //Splits the productLines
                if (!productSplit[3].equals(email)) {
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
    public ArrayList<String> viewShoppingCartsServer(String email, Object PRODUCTLOCK) {
        ArrayList<String> shoppingCartLines = new ArrayList<>(); //ArrayList of lines from ShoppingCart.txt
        try {
            synchronized (PRODUCTLOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < shoppingCartLines.size(); i++) {
                String[] productSplit = shoppingCartLines.get(i).split(","); //Splits the ShoppingCart line
                if (!productSplit[3].equals(email)) {
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
