import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Program Name
 * <p>
 * Program Description
 *
 * @author Ayush Bindal, Lab #
 * @version 01/01/2023
 * <p>
 * Sources: [TA NAMES]
 */

public class AccountManager {

    //TODO: Create editAccountDetails method


    //TODO: Create editPassword method


    //TODO: Create updateEmailFilesList method


    //TODO: Create deleteAccount method


    //TODO: Create deleteAccountFilesList method


    //TODO: Create authenticateAccount method


    //TODO: Create registerNewAccount method


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
