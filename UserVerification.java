package Security;

// Used GitHub Copilot in VScode to separate the old 2140 code to follow the Package Diagram
import java.util.ArrayList;
import Data_Persistence.UserRecord;

public class UserVerification {
    static {
        UserRecord.loadExistingUsers();
    }

    public static UserRecord verifyUser(String username, String password) {
        ArrayList<UserRecord> users = UserRecord.userlist;
        for (UserRecord user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
