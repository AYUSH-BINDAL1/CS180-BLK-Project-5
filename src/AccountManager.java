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
 * @version 12/2/2023
 * <p>
 */

public class AccountManager {


    // TODO: Create editAccountDetailsClient Method


    //Given a newPassword and email, updates Username.txt accordingly
    public static String updatePasswordFiles(String email, String oldPassword, String newPassword,
                                             Object USERINFOLOCK) {
        String result = "";
        try {
            ArrayList<String> userInformationList;
            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }

            int passwordIndex = userInformationList.indexOf(email) + 1; //Index of password
            String oldPasswordOnFile = userInformationList.get(passwordIndex); //Password on file
            if (oldPasswordOnFile.equals(oldPassword)) { //Checks to see if the password they enter is equal to the
                // password on file
                userInformationList.set(passwordIndex, newPassword); //Changes password and rewrites file
                synchronized (USERINFOLOCK) {
                    Files.write(Paths.get("Username.txt"), userInformationList);
                }
                result = "PASSWORD UPDATED";
            } else {
                result = "INCORRECT PASSWORD";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //Given a newEmail and email, updates Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
    // accordingly
    public static String updateEmailFiles(String oldEmail, String newEmail, Object USERINFOLOCK,
                                        Object SHOPPINGCARTLOCK, Object PURCHASEHISTORYLOCK, Object PRODUCTLOCK) {
        try {
            ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
            ArrayList<String> productList; //ArrayList of lines from Product.txt
            ArrayList<String> purchaseHistoryList; //ArrayList of lines from purchaseHistory.txt
            ArrayList<String> shoppingCartList; //ArrayList of lines from ShoppingCart.txt

            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }
            int emailIndex = userInformationList.indexOf(oldEmail); //Index of oldEmail in Username.txt
            int newEmailIndexCheck = userInformationList.indexOf(newEmail); //Checks to see if the newEmail is
            // already taken
            if (newEmailIndexCheck != -1) { //If newEmail is already taken
                return "EMAIL ALREADY TAKEN";
            }
            userInformationList.set(emailIndex, newEmail); //Sets newEmail at index emailIndex in Username.txt
            synchronized (USERINFOLOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(","); //Splits productList at index i into
                // productLine
                if (productLine[3].equals(oldEmail)) { //If oldEmail is found in Product.txt, sets newEmail at
                    // index 3 in productLine
                    productLine[3] = newEmail;
                    productList.set(i, String.join(",", productLine)); //Sets productLine as a
                    // String in productList at index i
                }
            }
            synchronized (PRODUCTLOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }

            synchronized (PURCHASEHISTORYLOCK) {
                //Reads lines from PurchaseHistory.txt
                purchaseHistoryList = (ArrayList<String>)
                        Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            }
            for (int i = 0; i < purchaseHistoryList.size(); i++) {
                String[] purchaseHistoryLine = purchaseHistoryList.get(i).split(",");
                if (purchaseHistoryLine[3].equals(oldEmail)) { //If the oldEmail is seller in PurchaseHistory.txt
                    purchaseHistoryLine[3] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
                if (purchaseHistoryLine[6].equals(oldEmail)) { //If the oldEmail is customer in PurchaseHistory.txt
                    purchaseHistoryLine[6] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
            }
            synchronized (PURCHASEHISTORYLOCK) {
                Files.write(Paths.get("PurchaseHistory.txt"), productList);
            }

            synchronized (SHOPPINGCARTLOCK) {
                //Reads lines from ShoppingCart.txt
                shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for (int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(",");
                if (shoppingCartLine[3].equals(oldEmail)) {  //If the oldEmail is seller in PurchaseHistory.txt
                    shoppingCartLine[3] = newEmail;
                    shoppingCartList.set(i, String.join(",", shoppingCartLine));
                }
                if (shoppingCartLine[6].equals(oldEmail)) {  //If the oldEmail is customer in PurchaseHistory.txt
                    shoppingCartLine[6] = newEmail;
                    shoppingCartList.set(i, String.join(",", shoppingCartLine));
                }
            }
            synchronized (SHOPPINGCARTLOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), productList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }


    //TODO: Create deleteAccountClient Method


    public static String deleteAccount(String email, String password, Object USERINFOLOCK,
                                       Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        String result = ""; //Result to send back to run
        try {
            ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }
            int emailIndex = userInformationList.indexOf(email); //Index of email in Username.txt
            if (userInformationList.get(emailIndex + 1).equals(password)) { //If password matches deletes account
                deleteAccountFiles(email, USERINFOLOCK, SHOPPINGCARTLOCK, PRODUCTLOCK);
                result = "SUCCESS";
            } else {
                result = "INVALID PASSWORD";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //Given email, deletes information from Username.txt, Product.txt, ShoppingCart.txt and PurchaseHistory.txt
    // accordingly
    public static void deleteAccountFiles(String email, Object USERINFOLOCK,
                                          Object SHOPPINGCARTLOCK, Object PRODUCTLOCK) {
        try {
            ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
            ArrayList<String> productList; //ArrayList of lines from Product.txt
            ArrayList<String> shoppingCartList; //ArrayList of lines from ShoppingCart.txt

            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }
            int emailIndex = userInformationList.indexOf(email); //Index of email in Username.txt
            userInformationList.remove(emailIndex); //Removes email
            userInformationList.remove(emailIndex); //Removes password
            userInformationList.remove(emailIndex); //Removes userType

            synchronized (USERINFOLOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            synchronized (PRODUCTLOCK) {
                //Reads lines from Product.txt
                productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            }
            for (int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(",");
                if (productLine[3].equals(email)) { //If email is found in Product.txt, removes product
                    productList.remove(i);
                    i--; //Accounts for removal
                }
            }
            synchronized (PRODUCTLOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }

            synchronized (SHOPPINGCARTLOCK) {
                shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            }
            for (int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(","); //Splits shoppingCartList
                if (shoppingCartLine[3].equals(email)) { //If email is found for seller in ShoppingCart.txt, removes
                    // product
                    shoppingCartList.remove(i);
                    i--;
                }
                if (shoppingCartLine[6].equals(email)) { //If email is found for customer in ShoppingCart.txt, removes
                    // product
                    shoppingCartList.remove(i);
                    i--;
                }
            }
            synchronized (SHOPPINGCARTLOCK) {
                Files.write(Paths.get("ShoppingCart.txt"), productList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // TODO: Create loginClient Method


    //Method that takes email, password, and userType and if they match with an account in the Username.txt sends
    // message back to run in ServerHandler
    public static String loginServer(String email, String password, String userType, Object USERINFOLOCK) {

        ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
        String result = "";

        try {
            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }

            int emailIndex = userInformationList.indexOf(email); //Index of email in Username.txt
            if (emailIndex != -1) { //If the email is valid

                if (userInformationList.get(emailIndex + 1).equals(password)) { //If the password is valid
                    if (userInformationList.get(emailIndex + 2).equals(userType)) { //If the userType is valid
                        result = "SUCCESS"; //Login Successful
                    } else { //Invalid User Type
                        result = "INVALID USER TYPE";
                    }
                } else { //Invalid Password
                    result = "INVALID PASSWORD";
                }
            } else {  //If the Username.txt doesn't contain the email
                result = "INVALID EMAIL";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result; //Returns result to run to let client know if the login succeeded or failed
    }


    // TODO: Create registerClient Method


    //Method that takes email, password, userType and if valid and not existing adds to the Username.txt file
    public static String registerServer(String email, String password, String userType, Object USERINFOLOCK) {
        String result = "";
        ArrayList<String> userInformationList; //ArrayList of lines from Username.txt
        boolean validEmail = validateEmail(email); //Verifies email is valid email;
        if (!validEmail) {
            result = "INVALID EMAIL"; //If email is not valid, returns invalid email to client
        }
        try {
            synchronized (USERINFOLOCK) {
                //Reads lines from Username.txt
                userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            }

            int emailIndex = userInformationList.indexOf(email); //Index of email in Username.txt
            if (emailIndex != -1) { //If the email already exists in Username.txt
                result = "EMAIL ALREADY EXISTS";
            } else { //If the email does not exist already
                userInformationList.add(email); //Adds email to Username.txt
                userInformationList.add(password); //Adds password to Username.txt
                userInformationList.add(userType); //Adds userType to Username.txt
                synchronized (USERINFOLOCK) {
                    Files.write(Paths.get("Username.txt"), userInformationList);
                }
                result = "SUCCESS";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result; //Returns result to run to let client know if the registration succeeded or failed
    }


    //Verifies email is valid email
    public static boolean validateEmail(String email) {
        String usernameRegex = "^[^,][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; //Regex expression
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



}
