package backend.luna.lehigh.edu;

/**
 * MessageObj - a class that stores data pertaining to tblMessage table.
 */
class MessageObj {
    int user_id;
    int message_id;
    String title;
    String body;
    java.util.Date uploadDate;
    String username;
    String realname;
    String email;
    byte[] filecontent;


    public MessageObj (int Guser_id, int Gmessage_id, String Gtitle, String Gbody, java.util.Date GuploadDate,
                       String Gusername, String Grealname, String Gemail) {
        user_id = Guser_id;
        message_id = Gmessage_id;
        title = Gtitle;
        body = Gbody;
        uploadDate = GuploadDate;
        username = Gusername;
        realname = Grealname;
        email = Gemail;
    }

    public MessageObj (int Guser_id, int Gmessage_id, String Gtitle, String Gbody, java.util.Date GuploadDate,
                       String Gusername, String Grealname, String Gemail, byte[] Gfilecontent) {
        user_id = Guser_id;
        message_id = Gmessage_id;
        title = Gtitle;
        body = Gbody;
        uploadDate = GuploadDate;
        username = Gusername;
        realname = Grealname;
        email = Gemail;
        if (Gfilecontent != null && Gfilecontent.length != 0) {
            filecontent = Gfilecontent.clone();
        }
        else {
            filecontent = null;
        }
    }

    public MessageObj (int Guser_id, String Gtitle, String Gbody) {
        user_id = Guser_id;
        title = Gtitle;
        body = Gbody;
    }
}