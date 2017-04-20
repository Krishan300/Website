package backend.luna.lehigh.edu;

/**
 * VoteObj - a class that stores data pertaining to tblUpVote / tblDownVote table.
 */
class VoteObj {
    int user_id;
    int message_id;

    public VoteObj (int Guser_id, int Gmessage_id) {
        user_id = Guser_id;
        message_id = Gmessage_id;
    }
}