package backend.luna.lehigh.edu;

/**
 * MessageContentObj - a class that stores data pertaining to tblMessage table.
 * This version of the object includes userdata to be included with Message content, used
 * by front page. Created after realizing a need for new fields during querying processes.
 */
class MessageContentObj {
    int user_id;
    int message_id;
    String title;
    String body;
    java.util.Date uploadDate;
    String username;
    String realname;
    String email;
    int upvotes;
    int downvotes;
    int tot_votes;

    public MessageContentObj (int Guser_id, int Gmessage_id, String Gtitle, String Gbody, java.util.Date GuploadDate,
                              String Gusername, String Grealname, String Gemail, int Gupvotes, int Gdownvotes,
                              int Gtot_votes) {
        user_id = Guser_id;
        message_id = Gmessage_id;
        title = Gtitle;
        body = Gbody;
        uploadDate = GuploadDate;
        username = Gusername;
        realname = Grealname;
        email = Gemail;
        upvotes = Gupvotes;
        downvotes = Gdownvotes;
        tot_votes = Gtot_votes;
    }
}