import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AccountManager
 * <p>
 * Java class that handles all infomration regarding the user (Usernames, Password, etc...)
 *
 * @author Ayush Bindal, Lab #L08
 * @version 11/30/2023
 * <p>
 */

public class AccountManager {


    //TODO: Create editAccountDetailsClient method
    //TODO: Create editAccountDetailsServer method


    //TODO: Create editPasswordClient method
    //TODO: Create editPasswordServer method


    //TODO: Create updateEmailFilesListClient method
    //TODO: Create updateEmailFilesListServer method


    //TODO: Create deleteAccountClient method
    //TODO: Create deleteAccountServer method

    //TODO: Create deleteAccountFilesListClient method
    //TODO: Create deleteAccountFilesListServer method


    //TODO: Create deleteAccountFilesListClient method
    //TODO: Create authenticateAccountServer method

    //TODO: Create registerNewAccountServer method
    //TODO: Create registerNewAccountServer method


    //TODO: Create validateEmail method
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
