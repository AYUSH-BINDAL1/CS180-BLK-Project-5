# CS180-BLK-Project-5
The capstone project for Purdue's CS180 - BLK course.\
A fully functioning Marketplace with Concurrency, Server-Client Interactions, and a GUI Interface.
## How to use our code:
To use our code, first compile and run MarketPlaceServer.java using the following terminal commands.\
In the terminal run:
```console
javac MarketPlaceServer.java
```
```console
java MarketPlaceServer
```
Once the server has started, compile and run MarketPlaceClient.java using the following terminal commands.\
In the terminal run:
```console
javac MarketPlaceClient.java
```
```console
java MarketPlaceClient
```
If you want to start another instance of the Client on the same machine simply rerun in the terminal:
```console
java MarketPlaceCLient
```
After that simply follow the prompts!
**NOTE: You may need to minimize any current windows to find the Marketplace.

## Submissions on BrightSpace and Vocareum
____ Submitted Vocareum Workspace\
Benjamin Wu Submitted Report on Brightspace\
____ Submitted Presentation on Brightspace

## SRC Files


MarketPlaceServer

Description: ONLY handles connections to ServerSocket and creation of Threads/Runnable Class. Also creates 
individual locks for each of the files. Also, will handle booting of TXT files being used

```java
public class MarketPlaceServer {

    //Creates "global" Object LOCKS for synchronizing specific files
  
    public static void main(String[] args) {
        
        //Creates four txt files if they don't already exist 
        //Starts ServerSocket at PORT: 6969
        //Continually accepts connections from Clients
        //Starts a new instance of ServerHandler and creates a new thread for that instance
    }
}
```


ServerHandler

Description: Implements the Runnable interface and manages client interactions. Initializes and handles the Socket
client within the constructor.
```java
public class ServerHandler implements Runnable {

    //Constructor for new ClientHandler
    public ServerHandler(Socket clientSocket, Object USERINFOLOCK, Object SHOPPINGCARTLOCK,
                         Object PURCHASEHISTORYLOCK, Object PRODUCTLOCK) {
        //Sets up Socket, Reader, Writer, Object Output Stream, and All Locks
    }

    public void run() {
        //Looks for information sent over the Socket in the form of a formatted String seperated by commas
        //Once split the 0th index will always be the command followed by any other information needed by other 
        //methods.
        //Depending on if the String is empty or the ArrayList is empty it will send back the according data type 
        //for the action performed 
    }

    public void exitProgram() {
        //Closes Sockets, Readers, Writers
    }
}
```


AccountManager

Description:
```java
public class AccountManager {
    
    
    public static String updatePasswordFiles(String email, String oldPassword, String newPassword,
                                             Object USERINFOLOCK) {
        //Changes the password associated with a user's email in the Username.txt.
        //Validates that the password is correct before updating it
        //Returns if it suceeded or the oldPassword entered was incorrect
    }
    
    
    public static String updateEmailFiles(String oldEmail, String newEmail, Object USERINFOLOCK,
                                        Object SHOPPINGCARTLOCK, Object PURCHASEHISTORYLOCK, Object PRODUCTLOCK) {
        //Given a newEmail and email, updates Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
        // accordingly. Verifies the newEmail is not already taken by another user
        //Returns a String if it sucessfully updated or the email was taken
    }

    
    public static String deleteAccount(String email, String password, Object USERINFOLOCK,
                                       Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        //Checks if the provided password matches the one stored for the given email in Username.txt
        //Deletes account-related files if the password validation is successful using deleteAccountFiles
    }

    
    public static void deleteAccountFiles(String email, Object USERINFOLOCK,
                                          Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        //Given email, deletes information from Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
        //accordingly
    }
    
    
    public static String loginServer(String email, String password, String userType, Object USERINFOLOCK) {
        //Method that takes email, password, and userType and if they match with an account in the Username.txt sends
        // message back to run in ServerHandler
    }

    
    //Method that takes email, password, userType and if valid and not existing adds to the Username.txt file
    public static String registerServer(String email, String password, String userType, Object USERINFOLOCK) {
        //Checks if the provided username, password, and userType are unique and valid.
        //Registers the new account if the details are valid and not already present in the Username.txt.
        //Uses synchronization to prevent concurrent access during registration.

    }


    
    public static boolean validateEmail(String email) {
        //Verifies email is valid email using a regex expression
    }
    
    

}
```


CustomerShopping

Description: Handles all methods for the customer's shopping and buying a product. Ex. Add To Cart, Buy Product 
Directly, etc...
```java
public class CustomerShopping {

    
    public static String addToCartServer(String email, String chosenProduct, Object SHOPPINGCARTLOCK,
                                         Object PRODUCTLOCK) {
        //Given a product in the form of the formatted String from the Product.txt file adds to ShoppingCart.txt if it 
        //is a valid quantity and the Product exists. Sends a message based on if it was sucessfull added. //Also 
        //subtracts quantity from Product.txt
    }


    public static String removeProductServer(String email, String chosenProduct, Object SHOPPINGCARTLOCK,
                                             Object PRODUCTLOCK) {
        //Given a product in ShoppingCart.txt in the form of a String checks if it is a valid Product then attempts 
        //to remove it by removing from the ShoppingCart and adding the quantity back to Product.txt
    }


    public static ArrayList<String> getCustomerShoppingCartServer(String email, Object SHOPPINGCARTLOCK) {
        //Returns an ArrayList of all the products currently in a customer's shopping cart
    }


    
    public static String buyProductServer(String customerEmail, String chosenProduct, Object PURCHASEHISTORYLOCK,
                                          Object PRODUCTLOCK) {
        //Method that buys product directly from page and returns whether it succeeded or not.
    }

    
    public static String checkoutCartServer(String email, Object SHOPPINGCARTLOCK, Object PURCHASEHISTORYLOCK) {
        //Given the customerEmail checks out their cart by removing it from ShoppingCart.txt and adding it to 
        //PurchaseHistory.txt
    }


}
```


SellerShopping

Description: Handles all methods pertaining to the seller shopping cart information
```java
public class SellerShopping {

    

    //Gets all products from ShoppingCart.txt and returns all products that belong to the seller
    public static ArrayList<String> getSellerShoppingCartServer(String sellerEmail, Object SHOPPINGCARTLOCK) {
        //Returns an ArrayList of product information in a customer shopping cart if the seller owns the product 
    }

    
    public static String modifyProductServer(String productName, String productDescription, String storeName,
                                           String sellerEmail, double price, int quantity, String oldProduct,
                                           Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        //Given an old product in a formatted String and new product information modifies the product information 
        //in Product.txt and ShoppingCart.txt
    }

    
    public static String deleteProductServer(String oldProduct, Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        //Given a product removes it from Product.txt and ShoppingCart.txt
    }

    
    public static String createNewProductServer(String productName, String productDescription, String storeName,
                                                String sellerEmail, double price, int quantity, Object PRODUCTLOCK) {
        //Given the information for a product tries to add it to Product.txt if the product doesnt exsist already
    }
    
    
    public static ArrayList<String> viewSellerProducts(String email, Object PRODUCTLOCK) {
        //Returns an ArrayList of all the products a seller owns
    }
    

}
```


ProductSearch

Description: Handles all the searching by name, description, and store for the customer
```java
public class ProductSearch {

    
    public static ArrayList<String> searchByNameServer(String productName, Object PRODUCTLOCK) {
        //Given searched product name, return ArrayList of products that contain that searched term
    }
    
    
    public static ArrayList<String> searchByStoreServer(String storeName, Object PRODUCTLOCK) {
        //Given searched product store, return ArrayList of products that contain that searched term
    }

    
    public static ArrayList<String> searchByDescriptionServer(String descriptionSearched, Object PRODUCTLOCK) {
        //Given searched product description, return ArrayList of products that contain that searched term
    }


}
```


ProductSort

Description: Sorts products by quantity and price for a customer
```java
public class ProductSort {


    public static ArrayList<String> sortByIncreasingPriceServer(Object PRODUCTLOCK) {
        //Sorts all products by min to max price using a selection sort and returns arraylist
    }


    public static ArrayList<String> sortByDecreasingPriceServer(Object PRODUCTLOCK) {
        //Sorts all products by max to min price using a selection sort and returns arraylist
    }
    

    //sorts all products by min to max quantity
    public static ArrayList<String> sortByIncreasingQuantityServer(Object PRODUCTLOCK) {
        //sorts all products by min to max quantity using a selection sort and returns arraylist
    }

    
    public static ArrayList<String> sortByDecreasingQuantityServer(Object PRODUCTLOCK) {
        //sorts all products by max to min quantity using a selection sort and returns arraylist
    }

}
```


PurchaseHistory

Description: Handles all purchase history information for all users
```java
public class PurchaseHistory {

    
    
    public static ArrayList<String> viewCustomerPurchaseHistoryServer(String customerEmail, Object PURCHASEHISTORYLOCK) {
        //Given a customer returns an ArrayList of Strings containing the customer's purchase history
    }
    
    
    public static String exportCustomerPurchaseHistoryServer(String customerEmail, String path,
                                                             Object PURCHASEHISTORYLOCK) {
        //Given a path and customerEmail creates a new txt file with the customer's purchase history 
    }


}
```


CSVHandler

Description: Handles all the CSV handling for the seller who can import or export a CSV file

```java
import javax.swing.*;

public class CSVHandler {
    public static String importSellerCSVServer(String path, Object PRODUCTLOCK) {
        //Reads each line of the CSV file from the given path into an ArrayList
        //Removes the first line as that is just formatting
        //Loops through the CSV ArrayList and checks if the Product is already in the Product.txt and if it isn't 
        //formatted correctly
        //Returns String if it suceeded or failed
    }

    public static String exportSellerCSVServer(String path, String sellerEmail, Object PRODUCTLOCK) {
        //Loops through "Product.txt" and see if a seller owns a product. If they do adds it to the CSV file in the 
        //formatted String
    }
}
```


Statistics

Description: Handles all information regarding statistics feature for both customer and seller
```java
public class Statistics {
    
    
    public static ArrayList<String> customerDashboardServer(String sort, Object PURCHASEHISTORYLOCK,
                                                            Object PRODUCTLOCK) {
    }
    

    public static ArrayList<String> customerDashboardSpecificServer(String customerEmail,
                                                                    String sort, Object PURCHASEHISTORYLOCK) {
    }
    
    
    public static ArrayList<String> sortCustomerLowtoHighServer(ArrayList<String> toSort) {
        //sort from low to high using selection sort
    }

    
    public static ArrayList<String> sortCustomerHightoLowServer(ArrayList<String> toSort) {
        //sort from high to low using selection sort
    }


    public static String viewSalesByStoreServer(String sellerEmail, Object PURCHASEHISTORYLOCK) {
        //Given a seller's email loops through PurchaseHistory.txt and creates a formatted String based on a sale 
        //at a store.
    }


    public static String generateSellerDashboardServer(String sellerEmail, int rank, Object PURCHASEHISTORYLOCK) {
        //Creates seller dashboard given the purchase history and sorts
    }


    private static void updatePurchaseCount(ArrayList<String> list, String key, String quantity) {
        // Find existing entry
    }


    private static void sortList(ArrayList<String> list, int rank) {
        //Sorts Seller Dashboard based on rank provided by user
    }


}
```

## File Formatting
Four Files In Use: Username.txt, Product.txt, PurchaseHistory.txt, ShoppingCart.txt

File Format:\
Usernames/Password\
Username\nPassword\nSELLER/CUSTOMER


All Product, Purchase History,Shopping Cart
Product Name, Product Description, Store Name, Seller Name, Price, Quantity → if its shopping cart or purchase history add Customer Name at the end

When Split\
0 = Product Name\
1 = Product Description\
2 = Store Name\
3 = Seller Email\
4 = Price\
5 = Quantity\
6 = Customer Name (For Shopping Cart/Purchase History)
