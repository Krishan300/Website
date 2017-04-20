package backend.luna.lehigh.edu;

/**
 * UserStateObj - a class that holds a username and secret key that're received from
 * frontend.
 */
class UserStateObj {
    String username;
    int secret_key;

    public UserStateObj (String Gusername, int GsecretKey) {
        username = Gusername;
        secret_key = GsecretKey;
    }
}