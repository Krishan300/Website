package backend.luna.lehigh.edu;

/**
 * LoginObj - a class that stores received login credentials from frontend for validation.
 */
class LoginObj {
    String username;
    String password;

    public LoginObj (String Gusername, String Gpassword) {
        username = Gusername;
        password = Gpassword;
    }
}