package backend.luna.lehigh.edu;

/**
 * UserObj - a class that stores data pertaining to tblUser table.
 */
class UserObj {
    int user_id;
    String username;
    String realname;
    String email;

    // below is probably more common constructor since frontend won't know new user's index
    public UserObj(String Gusername, String Grealname, String Gemail) {
        username = Gusername;
        realname = Grealname;
        email = Gemail;
    }

    public UserObj(int Guser_id, String Gusername, String Grealname, String Gemail) {
        user_id = Guser_id;
        username = Gusername;
        realname = Grealname;
        email = Gemail;
    }
}