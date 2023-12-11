import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * CustomerShopping
 * <p>
 * This class handles all the methods for the customer shopping cart and buying.
 *
 * @author Ayush Bindal & Lionel Loo Lab #L08
 * @version 12/11/2023
 * <p>
 */

public class CustomerShopping {


    //Given a product in the form of the formatted String EXACTLY as it is in the Product.txt file adds if it is a
    // valid quantity
    public static String addToCartServer(String email, String productName, String productDescription,
                                         String storeName, String sellerEmail, String price, String quantitiy,
                                         Object SHOPPINGCARTLOCK,
                                         Object PRODUCTLOCK) {
        String chosenProduct =
                productName + "," + productDescription + "," + storeName + "," + sellerEmail + "," + price + "," +
                        quantitiy;

        String[] chosenProductSplit = chosenProduct.split(","); //Splits the product the user chose
        int purchaseQuantity = Integer.parseInt(chosenProductSplit[5]); //Gets the quantity of the user's product
        String result = "";
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        boolean inCart = false; //Checks if the product is already in the cart
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            int productIndex = productLines.indexOf(chosenProduct); //Gets the index where the chosenProduct is
            // found in "Product.txt". This **SHOULD** work if the String is formatted correctly
            result = "INVALID PRODUCT";
            for (int j = 0; j < productLines.size(); j++) {
                String[] productLinesSplit = productLines.get(j).split(",");
                if (productLinesSplit[0].equals(chosenProductSplit[0]) &&
                        productLinesSplit[2].equals(chosenProductSplit[2]) &&
                        productLinesSplit[3].equals(chosenProductSplit[3])
                ) {
                    String[] productOnFileSplit = productLines.get(j).split(",");
                    int amountAvailable = Integer.parseInt(productOnFileSplit[5]); //Gets amount available to be
                    // bought

                    if (purchaseQuantity > amountAvailable) { //Not enough quantity
                        result = "NOT ENOUGH QUANTITY";
                    } else {
                        //Decreases the amount available, changes the value of the split product and reads it to the
                        // productLines and adds to shoppingCart
                        for (int i = 0; i < shoppingCartLines.size(); i++) {
                            String[] shoppingCartSplit = shoppingCartLines.get(i).split(",");
                            if (shoppingCartSplit[0].equals(chosenProductSplit[0]) &&
                                    shoppingCartSplit[2].equals(chosenProductSplit[2]) &&
                                    shoppingCartSplit[3].equals(chosenProductSplit[3]) &&
                                    shoppingCartSplit[6].equals(email)) {
                                shoppingCartSplit[5] =
                                        String.valueOf(Integer.parseInt(shoppingCartSplit[5]) + purchaseQuantity);
                                shoppingCartLines.set(i, String.join(",", shoppingCartSplit));
                                inCart = true;
                            }
                        }
                        result = "ADDED TO CART";
                        if (!inCart) {
                            shoppingCartLines.add(chosenProduct + "," + email);
                        }
                        synchronized (SHOPPINGCARTLOCK) {
                            Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //Given a product from a customer's shopping cart attempts to remove it and add it back to Product.txt
    //FIXME: Probably need to check if the chosenProduct is in the shopping cart first
    public static String removeProductServer(String productName, String productDesciption,
                                             String storename, String sellerEmail, String price, String quantity,
                                             String email, Object SHOPPINGCARTLOCK) {
        String chosenProduct =
                productName + "," + productDesciption + "," + storename + "," + sellerEmail + "," + price + ","
                        + quantity + "," + email;
        String[] chosenProductSplit = chosenProduct.split(","); //Splits the product the user chose
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt

        try {
            synchronized (SHOPPINGCARTLOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }

            //Removes the product from the shopping cart
            shoppingCartLines.remove(chosenProduct);

            synchronized (SHOPPINGCARTLOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }


    public static ArrayList<String> getCustomerShoppingCartServer(String email, Object SHOPPINGCARTLOCK) {
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        ArrayList<String> returnList = new ArrayList<String>(); //Result to return to run method
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for (String line : shoppingCartLines) {
                String[] shoppingCartSplit = line.split(",");
                if (shoppingCartSplit[6].equals(email)) { //If the customer email matches it adds it to the result
                    returnList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList; //Returns the result to run method
    }


    //Method that buys product directly from page and returns whether it succeeded or not.
    public static String buyProductServer(String customerEmail, String productName, String productDescription,
                                          String storeName, String sellerEmail, String price, String quantitiy,
                                          Object PURCHASEHISTORYLOCK,
                                          Object PRODUCTLOCK) {
        String chosenProduct =
                productName + "," + productDescription + "," + storeName + "," + sellerEmail + "," + price + "," +
                        quantitiy;
        String[] chosenProductSplit = chosenProduct.split(","); //Splits the product the user chose
        int purchaseAmount = Integer.parseInt(chosenProductSplit[5]); //Gets the quantity of the user's product
        ArrayList<String> productLines; //ArrayList of lines from Product.txt
        String result = ""; //Result to return to run method
        String modifiedProduct = ""; //Product to add to PurchaseHistory.txt
        String toPurchaseHistory = "";

        try {

            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productLines = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }

            for (int i = 0; i < productLines.size(); i++) { //Loops through the Product.txt file
                String[] currentProductSplit = productLines.get(i).split(","); //Splits the current product line
                if (chosenProductSplit[0].equals(currentProductSplit[0]) &&
                        chosenProductSplit[2].equals(currentProductSplit[2]) &&
                        chosenProductSplit[3].equals(currentProductSplit[3])) { //If the product is found in Product
                    // .txt
                    int amountAvailable = Integer.parseInt(currentProductSplit[5]);
                    System.out.println(amountAvailable);
                    if (amountAvailable >= purchaseAmount) { //Checks if there is enough quantity and modifies
                        // accordingly
                        amountAvailable = amountAvailable - purchaseAmount;
                        currentProductSplit[5] = Integer.toString(amountAvailable);
                        modifiedProduct = String.join(",", currentProductSplit); //Rejoins string
                        toPurchaseHistory = String.join(",", chosenProductSplit);
                        productLines.set(i, modifiedProduct);
                        if (amountAvailable == 0) { //If the product is out of stock it removes it
                            productLines.remove(i);
                            System.out.println("removed");
                        }
                        toPurchaseHistory += "," + customerEmail; //Adds the customer email to the
                        result = "SUCCESS";
                    } else { //Not enough quantity
                        result = "NOT ENOUGH QUANTITY";
                    }
                    break;
                }
            }
            if (result.isEmpty()) { //Didn't find the product
                result = "PRODUCT NOT FOUND";
            } else if (result.equals("SUCCESS")) { //If and only if the product has been changed modifies it
                synchronized (PRODUCTLOCK) {
                    Files.write(Paths.get("Product.txt"), productLines);
                }
                synchronized (PURCHASEHISTORYLOCK) {
                    //Rewrites files
                    Files.write(Paths.get("PurchaseHistory.txt"), (toPurchaseHistory + "\n").getBytes(),
                            StandardOpenOption.APPEND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //Given the customerEmail checks out their cart
    public static ArrayList<String> checkoutCartServer(String email, Object SHOPPINGCARTLOCK,
                                                       Object PURCHASEHISTORYLOCK,
                                                       Object PRODUCTLOCK) {
        ArrayList<String> shoppingCartLines; //ArrayList of lines from ShoppingCart.txt
        ArrayList<String> purchaseResults = new ArrayList<>(); //Basically stores all the arraylist of results -->
        // if one of the products does not have enough quantity it will store that otherwise it will be success
        ArrayList<String> toAdd = new ArrayList<String>(); //ArrayList to add to PurchaseHistory.txt
        String result = ""; //Result to return to run method
        try {
            synchronized (SHOPPINGCARTLOCK) {
                //Reads lines from ShoppingCart.txt and PurchaseHistory.txt
                shoppingCartLines = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for (int i = 0; i < shoppingCartLines.size(); i++) {
                String[] shoppingCartSplit = shoppingCartLines.get(i).split(",");
                String currentProduct = shoppingCartSplit[0] + "," + shoppingCartSplit[1] + "," +
                        shoppingCartSplit[2] + "," + shoppingCartSplit[3] + "," + shoppingCartSplit[4] +
                        "," + shoppingCartSplit[5];
                if (shoppingCartSplit[6].equals(email)) { //If the customer email matches it removes it from
                    // the shoppingCart ArrayList and adds it to the toAdd ArrayList to be added
                    purchaseResults.add(shoppingCartSplit[0] + "," + buyProductServer(shoppingCartSplit[6],
                            shoppingCartSplit[0], shoppingCartSplit[1],
                            shoppingCartSplit[2], shoppingCartSplit[3],
                            shoppingCartSplit[4], shoppingCartSplit[5],
                            PURCHASEHISTORYLOCK,
                            PRODUCTLOCK));

                    toAdd.add(shoppingCartLines.get(i));
                    shoppingCartLines.remove(i);
                    i--; //Accounts for removal
                }
            }
            if (toAdd.isEmpty()) { //Empty cart
                result = "EMPTY CART";
            } else {
                synchronized (SHOPPINGCARTLOCK) {
                    Files.write(Paths.get("ShoppingCart.txt"), shoppingCartLines); //Rewrites text file
                }
                result = "SUCCESS";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return purchaseResults;
    }

    //Returns ArrayList of All Products in Product.txt
    public static ArrayList<String> getAllProducts(Object PRODUCTLOCK) {
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

}
