import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * CustomerShopping
 * <p>
 * This class handles all the file reading/writing for the shopping cart
 *
 * @author  Lab L08
 * @version 01/01/2023
 * <p>
 */

public class CustomerShopping {
    //TODO: Create addToCartClient method

    //Given a product in the form of the formatted String EXACTLY as it is in the Product.txt file adds if it is a
    // valid quantity
    public static String addToCartServer(String email, String chosenProduct, Object LOCK) {
        String[] chosenProductSplit = chosenProduct.split(","); //Splits the product the user chose
        int purchaseQuantity = Integer.parseInt(chosenProductSplit[5]); //Gets the quantity of the user's product
        String result = "";
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        try {
            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt and Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            int productIndex = productLines.indexOf(chosenProduct); //Gets the index where the chosenProduct is
            // found in "Product.txt". This **SHOULD** work if the String is formatted correctly
            String[] productOnFileSplit = productLines.get(productIndex).split(",");
            int amountAvailable = Integer.parseInt(productOnFileSplit[5]); //Gets amount avaliable to be bought

            if(purchaseQuantity > amountAvailable) { //Not enough quantity
                result = "NOT ENOUGH QUANTITY";
            } else {
                //Decreases the amount available, changes the value of the split product and reads it to the
                // productLines and adds to shoppingCart
                amountAvailable = amountAvailable - purchaseQuantity;
                productOnFileSplit[5] = Integer.toString(amountAvailable);
                productLines.set(productIndex, String.join(",", productOnFileSplit));
                result = "ADDED TO CART";
                shoppingCartLines.add(chosenProduct + "," + email);
                synchronized (LOCK) {
                    Files.write(Paths.get("Product.txt"), productLines);
                    Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //TODO: Create removeProductClient method

    //Given a product from a customer's shopping cart attempts to remove it and add it back to Product.txt
    public static String removeProductServer(String email, String chosenProduct, Object LOCK) {
        String[] chosenProductSplit = chosenProduct.split(","); //Splits the product the user chose
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        try {
            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt and Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            //Removes the product from the shopping cart
            productLines.remove(chosenProduct);
            for(int i = 0; i < productLines.size(); i++) {
                String[] productSplit = productLines.get(i).split(",");
                if(productSplit[0].equals(chosenProductSplit[0]) && productSplit[1].equals(chosenProductSplit[1])
                        && productSplit[3].equals(chosenProductSplit[3])) { //If the product line and chosen product =
                    //Reads the values from the chosenProduct back to the line
                    productSplit[5] =
                            String.valueOf(Integer.parseInt(productSplit[5]) +
                                    Integer.parseInt(chosenProductSplit[5]));
                    productLines.set(i, String.join(",", productSplit));
                }
            }
            synchronized (LOCK) {
                //Rewrites files
                Files.write(Paths.get("Product.txt"), productLines);
                Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    //TODO: Create getCustomerShoppingCartClient method
    public static String getCustomerShoppingCartServer(String email, Object LOCK) {
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        StringBuilder result = new StringBuilder(); //Result to return to run method
        try {
            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for(String line : shoppingCartLines) {
                String[] shoppingCartSplit = line.split(",");
                if(shoppingCartSplit[6].equals(email)) { //If the customer email matches it adds it to the result
                    result.append(line).append("\n");
                }
            }
            if(result.isEmpty()) {
                result.append("EMPTY CART"); //Empty cart
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString(); //Returns the result to run method
    }

    //TODO: Create buyProductClient method
    //TODO: Create buyProductServer method

    //TODO: Create checkoutCartClient method
    //Given the customerEmail checks out their cart
    public static String checkoutCartServer(String email, Object LOCK) {
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        ArrayList<String> toAdd = new ArrayList<String>(); //ArrayList to add to PurchaseHistory.txt
        String result = ""; //Result to return to run method
        try {
            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt and PurchaseHistory.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for(int i = 0; i < shoppingCartLines.size(); i++) {
                String[] shoppingCartSplit = shoppingCartLines.get(i).split(",");
                if(shoppingCartSplit[6].equals(email)) { //If the customer email matches it removes it from
                    // the shoppingCart ArrayList and adds it to the toAdd ArrayList to be added
                    toAdd.add(shoppingCartLines.get(i));
                    shoppingCartLines.remove(i);
                    i--; //Accounts for removal
                }
            }
            if(toAdd.isEmpty()) { //Empty cart
                result = "EMPTY CART";
            } else {
                synchronized (LOCK) {
                    //Rewrites files
                    Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines); //Rewrites text file
                    Files.write(Paths.get("PurchaseHistory.txt"), toAdd,
                            StandardOpenOption.APPEND); //Appends the list to the text file
                }
                result = "SUCCESS";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
