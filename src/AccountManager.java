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

    private static final Object LOCK = new Object();

    // TODO: Create editAccountDetailsClient Method
    // TODO: Create editAccountDetailsServer Method

    //FIXME: Fix synchronize
    public static void updatePasswordFilesList(String email, String newPassword) {
        try {
            ArrayList<String> userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            int passwordIndex = userInformationList.indexOf(email) + 1;
            userInformationList.set(passwordIndex, newPassword);
            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //FIXME: Fix synchronize
    public static void updateEmailFilesList(String oldEmail, String newEmail) {
        try {
            ArrayList<String> userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            int emailIndex = userInformationList.indexOf(oldEmail);
            userInformationList.set(emailIndex, newEmail);
            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            ArrayList<String> productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            for(int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(",");
                if(productLine[3].equals(oldEmail)) {
                    productLine[3] = newEmail;
                    productList.set(i, String.join(",", productLine));
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }

            ArrayList<String> purchaseHistoryList = (ArrayList<String>) Files.readAllLines(Paths.get("PurchaseHistory.txt"));
            for(int i = 0; i < purchaseHistoryList.size(); i++) {
                String[] purchaseHistoryLine = purchaseHistoryList.get(i).split(",");
                if(purchaseHistoryLine[3].equals(oldEmail)) {
                    purchaseHistoryLine[3] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
                if(purchaseHistoryLine[6].equals(oldEmail)) {
                    purchaseHistoryLine[6] = newEmail;
                    purchaseHistoryList.set(i, String.join(",", purchaseHistoryLine));
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("PurchaseHistory.txt"), productList);
            }


            ArrayList<String> shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            for(int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(",");
                if(shoppingCartLine[3].equals(oldEmail)) {
                    shoppingCartLine[3] = newEmail;
                    shoppingCartList.set(i, String.join(",", shoppingCartLine));
                }
                if(shoppingCartLine[6].equals(oldEmail)) {
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

    public static void deleteAccountFilesList(String email) {
        try {
            ArrayList<String> userInformationList = (ArrayList<String>) Files.readAllLines(Paths.get("Username.txt"));
            int emailIndex = userInformationList.indexOf(email);
            userInformationList.remove(emailIndex);
            userInformationList.remove(emailIndex);
            userInformationList.remove(emailIndex);

            synchronized (LOCK) {
                Files.write(Paths.get("Username.txt"), userInformationList);
            }

            ArrayList<String> productList = (ArrayList<String>) Files.readAllLines(Paths.get("Product.txt"));
            for(int i = 0; i < productList.size(); i++) {
                String[] productLine = productList.get(i).split(",");
                if(productLine[3].equals(email)) {
                    productList.remove(i);
                    i--;
                }
            }
            synchronized (LOCK) {
                Files.write(Paths.get("Product.txt"), productList);
            }


            ArrayList<String> shoppingCartList = (ArrayList<String>) Files.readAllLines(Paths.get("ShoppingCart.txt"));
            for(int i = 0; i < shoppingCartList.size(); i++) {
                String[] shoppingCartLine = shoppingCartList.get(i).split(",");
                if(shoppingCartLine[3].equals(email)) {
                    shoppingCartList.remove(i);
                    i--;
                }
                if(shoppingCartLine[6].equals(email)) {
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
        String usernameRegex = "^[^,][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(email);

        //TODO: Convert to PrintWriter
        System.out.println("Invalid Email");

        return matcher.matches();
    }


}
