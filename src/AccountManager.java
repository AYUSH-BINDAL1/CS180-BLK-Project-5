import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AccountManager
 * <p>
 * Java class that handles all information regarding the user (Usernames, Password, etc...)
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class AccountManager {


    // TODO: Create editAccountDetailsClient Method
    // TODO: Create editAccountDetailsServer Method

    //Given a newPassword and email, updates Username.txt accordingly
    public static void updatePasswordFilesList(String email, String newPassword, Object LOCK) {
        try {
            ArrayList<String> userInformationList; //
            synchronized (LOCK) {
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }

            int passwordIndex = userInformationList.indexOf(email) + 1;
            userInformationList.set(passwordIndex, newPassword);
            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Given a newEmail and email, updates Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
    // accordingly
    public static void updateEmailFilesList(String oldEmail, String newEmail, Object LOCK) {
        try {
            ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
            ArrayList<String> productList; //ArrayList of lines from Product.txt
            ArrayList<String> purchaseHistoryList; //ArrayList of lines from purchaseHistory.txt
            ArrayList<String> shoppingCartList; //ArrayList of lines from ShoppingCart.txt

            synchronized (LOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }
            int emailIndex = userInformationList.indexOf(oldEmail); //Index of oldEmail in Username.txt
            userInformationList.set(emailIndex, newEmail); //Sets newEmail at index emailIndex in Username.txt
            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            synchronized (LOCK) {
                //Reads lines from Product.txt
                productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for(int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(","); //Splits productList at index i into
                // productLine
                if(productLine[3].equals(oldEmail)) { //If oldEmail is found in Product.txt, sets newEmail at
                    // index 3 in productLine
                    productLine[3] = newEmail;
                    productList.set(i, String.join(",", productLine)); //Sets productLine as a
                    // String in productList at index i
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }

            synchronized (LOCK) {
                //Reads lines from PurchaseHistory.txt
                purchaseHistoryList = (ArrayList<String>)
                        Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for(int i = 0; i < purchaseHistoryList.size(); i++) {
                String[] purchaseHistoryLine = purchaseHistoryList.get(i).split(",");
                if(purchaseHistoryLine[3].equals(oldEmail)) { //If the oldEmail is seller in PurchaseHistory.txt
                    purchaseHistoryLine[3] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
                if(purchaseHistoryLine[6].equals(oldEmail)) { //If the oldEmail is customer in PurchaseHistory.txt
                    purchaseHistoryLine[6] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("PurchaseHistory.txt"), productList);
            }

            synchronized (LOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for(int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(",");
                if(shoppingCartLine[3].equals(oldEmail)) {  //If the oldEmail is seller in PurchaseHistory.txt
                    shoppingCartLine[3] = newEmail;
                    shoppingCartList.set(i, String.join(",", shoppingCartLine));
                }
                if(shoppingCartLine[6].equals(oldEmail)) {  //If the oldEmail is customer in PurchaseHistory.txt
                    shoppingCartLine[6] = newEmail;
                    shoppingCartList.set(i, String.join(",", shoppingCartLine));
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), productList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Create deleteAccountClient Method
    //TODO: Create deleteAccountServer Method

    //Given email, deletes information from Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
    // accordingly
    public static void deleteAccountFilesList(String email, Object LOCK) {
        try {
            ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
            ArrayList<String> productList; //ArrayList of lines from Product.txt
            ArrayList<String> shoppingCartList; //ArrayList of lines from ShoppingCart.txt

            synchronized (LOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }
            int emailIndex = userInformationList.indexOf(email); //Index of email in Username.txt
            userInformationList.remove(emailIndex); //Removes email
            userInformationList.remove(emailIndex); //Removes password
            userInformationList.remove(emailIndex); //Removes userType

            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            synchronized (LOCK) {
                //Reads lines from Product.txt
                productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for(int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(",");
                if(productLine[3].equals(email)) { //If email is found in Product.txt, removes product
                    productList.remove(i);
                    i--; //Accounts for removal
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }

            synchronized (LOCK) {
                shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for(int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(","); //Splits shoppingCartList
                if(shoppingCartLine[3].equals(email)) { //If email is found for seller in ShoppingCart.txt, removes
                    // product
                    shoppingCartList.remove(i);
                    i--;
                }
                if(shoppingCartLine[6].equals(email)) { //If email is found for customer in ShoppingCart.txt, removes
                    // product
                    shoppingCartList.remove(i);
                    i--;
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), productList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // TODO: Create authenticateAccountClient Method
    // TODO: Create authenticateAccountServer Method

    // TODO: Create registerNewAccountClient Method
    // TODO: Create registerNewAccountServer Method


    //Verifies email is valid email
    public static boolean validateEmail(String email) {
        String usernameRegex = "^[^,][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; //Regex expression
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(email);

        //TODO: Convert to PrintWriter
        System.out.println("Invalid Email");

        return matcher.matches();
    }


}
