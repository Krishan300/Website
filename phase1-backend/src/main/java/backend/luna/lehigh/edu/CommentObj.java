package backend.luna.lehigh.edu;

/**
 * CommentObj - a class that stores data pertaining to tblComment table.
 */
class CommentObj {
    int user_id;
    int message_id;
    int comment_id;
    String comment_text;
    java.util.Date uploadDate;
    String username;
    String realname;
    String email;

    public CommentObj(int Guser_id, int Gmessage_id, String Gcomment_text) {
        user_id = Guser_id;
        message_id = Gmessage_id;
        comment_text = Gcomment_text;
    }

    public CommentObj (int Guser_id, int Gmessage_id, int Gcomment_id, String Gcomment_text, java.util.Date GuploadDate,
                       String Gusername, String Grealname, String Gemail) {
        user_id = Guser_id;
        message_id = Gmessage_id;
        comment_id = Gcomment_id;
        comment_text = Gcomment_text;
        uploadDate = GuploadDate;
        username = Gusername;
        realname = Grealname;
        email = Gemail;
    }
}