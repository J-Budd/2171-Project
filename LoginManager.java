package Application_Logic;

// Used GitHub Copilot in VScode to separate the old 2140 code to follow the Package Diagram
import java.util.ArrayList;

import java.io.IOException;
import java.text.ParseException;
import UI.LoginScreen;
import UI.MainEntry;
import Security.UserVerification;
import Data_Persistence.UserRecord;

public class LoginManager {
    public static void handleLogin(boolean signIn, String username, String password1, String password2, String role, MainEntry mainScreen, LoginScreen loginScreen) {
        ArrayList<UserRecord> users = UserRecord.userlist;
        UserRecord userLogin = null;
        boolean usernameAvail = true;
        boolean principalAvail = true;

        if (!signIn) {
            for (UserRecord i : users) {
                if (i.getRole().toLowerCase().equals("principal")){
                    principalAvail = false;
                }
                if (i.getUsername().equals(username)) {
                    usernameAvail = false;
                    loginScreen.showPopUp("Username Not Available");
                    return;
                }
            }
        }

        if (usernameAvail && username.length() >= 3) {
            if (!signIn) {
                if (password1.length() < 8 || !password1.equals(password2)) {
                    loginScreen.showPopUp("Password Invalid. Must be 8 characters long");
                    return;
                } else {
                    String roleLower = role.toLowerCase();
                    if ((!roleLower.equals("principal")) && (!roleLower.equals("teacher")) && (!roleLower.equals("cook"))){
                        loginScreen.showPopUp("Role Invalid. Inputted role must be Principal, Teacher or Cook");
                        return;
                    } else if ((!principalAvail) && (roleLower.equals("principal"))) {
                        loginScreen.showPopUp("A Principal is already registered. Please select a different role");
                        return;
                    } else {
                        userLogin = new UserRecord(username, password1, roleLower);
                        UserRecord.saveUsers();
                    }
                }
            } else {
                userLogin = UserVerification.verifyUser(username, password1);
                if (userLogin == null) {
                    loginScreen.showPopUp("Invalid login. Check Username and Password and try again.");
                    return;
                } else {
                    loginScreen.showPopUp("Login successful");
                }
            }
        } else {
            loginScreen.showPopUp("Username must be three characters or longer");
            return;
        }

        if (userLogin != null) {
            String roleLower = userLogin.getRole().toLowerCase();
            if (roleLower.equals("principal")) {
                loginScreen.showPopUp("Log in successful");
                new UI.PrincipalListing().setVisible(true);
            } else if (roleLower.equals("teacher")) {
                // new TeacherListing(userLogin).setVisible(true);
            } else if (roleLower.equals("cook")) {
                // InventoryMain.main(new String[]{});
            } else {
                loginScreen.showPopUp("Invalid Role");
                return;
            }
            loginScreen.setVisible(false);
        }
    }
}
