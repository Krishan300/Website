package backend.luna.lehigh.edu;

import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.Base64;
import java.util.Hashtable;
import java.security.MessageDigest;
import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;

import com.google.api.client.http.FileContent;
import com.google.gson.*;
import com.google.api.services.drive.Drive;

import net.spy.memcached.MemcachedClient;

/**
 * @author Alex Van Heest
 * @version 1.4
 */


/**
 * The App class creates an App object which passes in a MySQL database and stores the
 * data in rows to be pulled from later.
 */
public class App {

    // Final static strings, used throughout program.
    final static String goodData = "{\"res\":\"ok\"}";
    final static String badData = "{\"res\":\"bad data\"}";
    final static String sFileLocation = "/web";
    protected static Connection conn;

    // Only one gson instantiation, for efficiency.
    final Gson gson;

    // Environment variables.
    static Map<String, String> env = System.getenv();
    static String ip = env.get("POSTGRES_IP");
    static String port = env.get("POSTGRES_PORT");
    static String user = env.get("POSTGRES_USER");
    static String pass = env.get("POSTGRES_PASS");
    static String db = env.get("POSTGRES_DB");
    //static String dbstring = env.get("DATABASE_URL");

    static Hashtable<String, Integer> hashtable = new Hashtable<>();

    // MEMCACHIER CREDENTIALS ... Note that servers are the same URL but separate
    // in case that changes at any point.
    // User/pass/server for secret key connections to memcached client
    static String mc_username_sk = "77AE4E";
    static String mc_password_sk = "BF013B51F332A15D8BAD73A2F6D665EC";
    static String mc_server_sk = "mc2.dev.ec2.memcachier.com:11211";

    // User/pass/server for file caching memcached client
    static String mc_username_fc = "7C31C5";
    static String mc_password_fc = "25796431EB1773FF16ABD1247F00BF25";
    static String mc_server_fc = "mc2.dev.ec2.memcachier.com:11211";

    /**
     * This is the long-awaited method that returns a Connection object. Replaces all the versions
     * throughout the code to keep the Connection stuff consistent and easier to update.
     */
    public static Connection createConnection() {

        try {
            // String dbUrl=System.getenv("JDBC_DATABASE_URL");
            // conn=DriverManager.getConnection(dbUrl);
            //URI dbUri = new URI(System.getenv("DATABASE_URL"));
            URI dbUri=new URI("postgres://kqtcfljdpfesuu:c47c0eba68c7ffb8369fc739bcfc16f78b08167f29eb61523d81d2592a4c273f@ec2-54-83-26-65.compute-1.amazonaws.com:5432/db647b4fma1r2m");
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            conn = DriverManager.getConnection(dbUrl, username, password);
            if(conn == null) {
                System.out.println("Error in createDB(): getConnection returned null object in createDB");
                System.exit(-1);
            }
        }   catch(Exception exc) {
            if(exc instanceof URISyntaxException)  {
                System.out.println("Error in createDB(): getConnection in createDB threw a URISyntaxexception.");
                exc.printStackTrace();
                System.exit(-1);
            }
            else if (exc instanceof SQLException) {
                System.out.println("Error in createDB(): getConnection in createDB threw an SQL exception");
                exc.printStackTrace();
                System.exit(-1);
            }
            else {
                System.out.println("Error in createDB(): getConnection in createDB threw an exception");
                exc.printStackTrace();
                System.exit(-1);
            }

        }




        return conn;
        // Connection to be returned.
        //Connection conn = null;

        // Include the needed driver (needs to be tested, may be redundant.
        //try {
        //    Class.forName("org.postgresql.Driver");
        //}
        //catch (ClassNotFoundException e) {
        //    System.err.println("ERROR in createConnection(): Driver not found");
        //    e.printStackTrace();
        //}

        // Connect to the database; fail if we can't
        //try {
            // Open a connection, fail if we cannot get one. Connection to POSTGRES server at following url:
         //   String url = "jdbc:postgresql://" + ip + ":" + port + "/" + db;
         //   conn = DriverManager.getConnection(url, user, pass);

            // Make sure connection object isn't null. If it is, print error and exit.
         //   if (conn == null) {
         //       System.out.println("Error in createDB(): getConnection returned null object in createDB");
         //       System.exit(-1);
         //   }
        //} catch (SQLException e) {
            // Unable to create connection to DB. Check env variables?
        //    System.out.println("Error in createDB(): getConnection in createDB threw an exception");
        //    e.printStackTrace();
        //    System.exit(-1);
        //}

        //return conn;
    }

    /**
     * Check to see if all five expected tables exist. Used primarily for testing
     * purposes.
     *
     * @return True if all tables found, false if any / all are missing.
     */
    public boolean hasFoundTables() {
        Connection conn = createConnection();
        boolean retval = true;  // only changed if one is found to not exist
        String[] tblNames = {"tbluser", "tblmessage", "tblcomments", "tbldownvotes", "tblupvotes"};

        try {
            // Loop through all tblNames provided above. Ensure that each returns true.
            PreparedStatement stmt;
            ResultSet rs;
            String queryTemplate = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = ?);";
            for (String tblname : tblNames) {
                stmt = conn.prepareStatement(queryTemplate);
                stmt.setString(1,tblname);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    retval = rs.getBoolean("exists");
                }
                if (!retval)    break;
            }
        }
        catch (SQLException e) {
            System.out.println("Error in tablesAreFound(): connections failed");
            e.printStackTrace();
            retval = false;
        }

        return retval;
    }

    /**
     * Get all data from our database and returns it in JSON format.
     * @return JSON object from SQL frontend
     *
     * DEPRECATED: Replaced for "gen 3" with new versions that depend on
     *             which data is actually needed.
     */
    String getAllData() {
        Connection conn = createConnection();

        ArrayList<Datum> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tbldata";
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                Datum currentDatum = new Datum();
                currentDatum.index = rs.getInt("id");
                currentDatum.title = rs.getString("title");
                currentDatum.comment = rs.getString("comment");
                currentDatum.numLikes = rs.getInt("numLikes");
                currentDatum.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
                results.add(currentDatum);
            }
            stmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        String result = gson.toJson(results);
        return result;
    }

    /**
     * Method that collects all Messages from tblMessage and returns a JSON string with
     * all its contents. Also collects all
     *
     * OBJECT PARAMS:
     * int user_id;
     * int message_id;
     * String title;
     * String body;
     * java.util.Date uploadDate;
     * String username;
     * String realname;
     * String email;
     *
     * @return  All Messages formatted as JSON string.
     */
    String getAllMessages() {
        Connection conn = createConnection();
        ArrayList<MessageObj> allMessages = new ArrayList<>();

        try {
            // Retrieve all contents from Message table:
            PreparedStatement stmt = conn.prepareStatement("SELECT tblmessage.user_id," +
                    "tblmessage.message_id, tblmessage.title, tblmessage.body, tblmessage.create_date, " +
                    "tbluser.username, tbluser.realname, tbluser.email " +
                    "FROM tblmessage INNER JOIN tbluser " +
                    "ON tblmessage.user_id = tbluser.user_id ORDER BY message_id DESC;");
            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet

            while (rs.next()) {
                // convert the RS to MessageObj objects.
                allMessages.add(new MessageObj(rs.getInt("user_id"),
                        rs.getInt("message_id"), rs.getString("title"),
                        rs.getString("body"),
                        sqlDateToJavaDate(rs.getTimestamp("create_date")),
                        rs.getString("username"), rs.getString("realname"),
                        rs.getString("email")));
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println("ERROR IN getAllMessages(): Unable to retrieve messages");
            e.printStackTrace();
        }

        // Return properly formatted JSON string:
        return gson.toJson(allMessages);
    }

    /**
     * Method that collects all Comments from tblComment pertaining to a given message_id
     * and returns a JSON string with all of the contents.
     *
     * OBJECT PARAMS:
     * int user_id;
     * int message_id;
     * int comment_id;
     * String comment_text;
     * java.util.Date uploadDate;
     * String username;
     * String realname;
     * String email;
     *
     * @return  All Comments formatted as JSON string.
     */
    String getAllComments(int givenMessage_id) {
        Connection conn = createConnection();
        ArrayList<CommentObj> allComments = new ArrayList<>();

        try {
            // Retrieve all contents from Comment table for specified message_id:
            PreparedStatement stmt = conn.prepareStatement("SELECT tblcomments.comment_id, " +
                    "tblcomments.user_id, tblcomments.comment_text, tblcomments.create_date, " +
                    "tbluser.username, tbluser.realname, tbluser.email " +
                    "FROM tblcomments INNER JOIN tbluser ON tblcomments.user_id = tbluser.user_id " +
                    "WHERE message_id=?;");
            stmt.setInt(1, givenMessage_id);
            ResultSet rs = stmt.executeQuery();

            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to MessageObj objects.
                allComments.add(new CommentObj(rs.getInt("user_id"),
                        givenMessage_id, rs.getInt("comment_id"),
                        rs.getString("comment_text"),
                        sqlDateToJavaDate(rs.getTimestamp("create_date")),
                        rs.getString("username"), rs.getString("realname"),
                        rs.getString("email")));
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println("ERROR IN getAllComments(): Unable to retrieve comments for Message with id " + givenMessage_id + "!");
            e.printStackTrace();
        }

        // Return properly formatted JSON string:
        return gson.toJson(allComments);
    }

    /**
     * Method that returns Message data with upvotes and downvotes for specified message_id.
     *
     * OBJECT STRUCTURE:
     * int user_id;
     * int message_id;
     * String title;
     * String body;
     * java.util.Date uploadDate;
     * String username;
     * String realname;
     * String email;
     * int upvotes;
     * int downvotes;
     * int tot_votes;
     *
     * @return  Message data, num upvotes, and num downvotes formatted as JSON.
     */
    String getMessageContentAndVotes(int givenMessage_id) {
        Connection conn = createConnection();
        ArrayList<MessageContentObj> msgData = new ArrayList<>();

        try {
            // First retrieve details of message at givenMessage_id:
            PreparedStatement stmt = conn.prepareStatement("SELECT tbluser.username, " +
                    "tbluser.realname, tbluser.email, tblmessage.message_id, tblmessage.title, " +
                    "tblmessage.body, tblmessage.create_date, tbluser.user_id " +
                    "FROM tblmessage INNER JOIN tbluser ON tblmessage.user_id = tbluser.user_id " +
                    "WHERE tblmessage.message_id = ?;");
            stmt.setInt(1, givenMessage_id);
            ResultSet rs = stmt.executeQuery();

            // should only be one row but for now it's forgivable
            int curUser_id = 0, curMessage_id = 0;
            String curTitle = null, curBody = null, curUsername = null, curRealname = null, curEmail = null;
            java.util.Date curCreate_date = new Date();
            while (rs.next()) {
                curUser_id = rs.getInt("user_id");
                curMessage_id = rs.getInt("message_id");
                curTitle = rs.getString("title");
                curBody = rs.getString("body");
                curCreate_date = sqlDateToJavaDate(rs.getTimestamp("create_date"));
                curUsername = rs.getString("username");
                curRealname = rs.getString("realname");
                curEmail = rs.getString("email");
            }

            // Next get the total upvotes and downvotes:
            int curUpvotes = 0, curDownvotes = 0;
            PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) AS totupvotes FROM tblupvotes WHERE message_id = ?;");
            stmt2.setInt(1, givenMessage_id);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                curUpvotes = rs2.getInt("totupvotes");
            }

            PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) AS totdownvotes FROM tbldownvotes WHERE message_id = ?;");
            stmt3.setInt(1, givenMessage_id);
            ResultSet rs3 = stmt3.executeQuery();
            while (rs3.next()) {
                curDownvotes = rs3.getInt("totdownvotes");
            }

            // Then create the new object and add it to the arraylist
            msgData.add(new MessageContentObj(curUser_id, curMessage_id, curTitle, curBody, curCreate_date,
                    curUsername, curRealname, curEmail, curUpvotes, curDownvotes, curUpvotes - curDownvotes));

            stmt.close();
            stmt2.close();
            stmt3.close();
        }
        catch (SQLException e) {
            System.out.println("ERROR IN getAllContentAndVotes(): Unable to retrieve Message with id " + givenMessage_id + "!");
            e.printStackTrace();
        }

        // Return properly formatted JSON string:
        return gson.toJson(msgData);
    }

    /**
     * Method that takes in a user_id and returns all user data pertaining to that user. Doesn't
     * include posts & comments, this will be done in a separate method. Intended to be called by a
     * method that combine the two and output into a JSON format for frontend to use.
     *
     * UserObj:
     * int user_id;
     * String username;
     * String realname;
     * String email;
     *
     * @return Properly formatted JSON string with user data.
     */
    String getUserData(int givenUser_id) {
        Connection conn = createConnection();
        ArrayList<UserObj> userData = new ArrayList<>();

        try {
            // Retrieve all userdata for as at givenUser_id:
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tbluser WHERE user_id = ?;");
            stmt.setInt(1,givenUser_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userData.add(new UserObj(givenUser_id, rs.getString("username"),
                        rs.getString("realname"), rs.getString("email")));
            }
        }
        catch (SQLException e) {
            System.out.println("ERROR IN getUserData(): Unable to retrieve comments for Message with id " + givenUser_id + "!");
            e.printStackTrace();
        }

        // Return properly formatted JSON string:
        return gson.toJson(userData);
    }


    /**
     * This insert takes new data from the frontend sever and adds it into the datum database
     * @param   d   A Datum object retrieved by and built through Spark framework
     * @return command telling server if addition was a success or not
     *
     * DEPRECATED: Replaced by inserts for the new multi-table POSTGRES schema.
     */
    String insertDatum(Datum d) {
        Connection conn = createConnection();

        // Only insert if whole datum is not null
        if (d != null && d.title != null && d.comment != null && d.numLikes == 0 && d.uploadDate != null && d.lastLikeDate != null) {
            try {
                //  (id, title, comment, numLikes, uploadDate, lastLikeDate)
                String insertStmt = "INSERT INTO tbldata VALUES (default, ?, ?, ?, ?, ?);";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,d.title);
                stmt.setString(2,d.comment);
                stmt.setInt(3,d.numLikes);
                stmt.setTimestamp(4,javaDateToSqlDate(d.uploadDate));
                stmt.setTimestamp(5,javaDateToSqlDate(d.lastLikeDate));
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error: insertion failed");
                e.printStackTrace();
            }
            return goodData;
        } else {
            return badData;
        }
    }

    /**
     * Method that takes in a new user object and adds it into the database. (NOTE: Building this into
     * the main App for now because I'm creating this just so I can build the frontend. Configuration
     * shouldn't have to be changed much after this, but for now, I'd like it to all be convenient to find.)
     * @param uo    A passed-in UserObj from the frontend.
     * @return      Either good or bad response to frontend.
     */
    String insertUser(UserObj uo) {
        Connection conn = createConnection();

        // Only insert if whole UserObj is not null
        if (uo != null && uo.username != null && uo.realname != null && uo.email != null) {
            try {
                //  (user_id, username, realname, email)
                String insertStmt = "INSERT INTO tbluser VALUES (default, ?, ?, ?);";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,uo.username);
                stmt.setString(2,uo.realname);
                stmt.setString(3,uo.email);
                stmt.executeUpdate();
                stmt.close();
            }
            catch (SQLException e) {
                System.out.println("Error in insertUser(): user insertion failed");
                e.printStackTrace();
                return badData;
            }
            return goodData;
        }
        else {
            return badData;
        }
    }

    /**
     * Method that takes in a new message object and adds it into the database. (NOTE: Building this into
     * the main App for now because I'm creating this just so I can build the frontend. Configuration
     * shouldn't have to be changed much after this, but for now, I'd like it to all be convenient to find.)
     * @param mo    A passed-in MessageObj from the frontend.
     * @return      Either good or bad response to frontend.
     */
    String insertMessage(MessageObj mo) {
        Connection conn = createConnection();

        // Check if there's a file given, and if there is, retrieve it and save it:
        if (mo.filecontent != null) {
            // Decode the Base64 and get file contents as a string, write
            byte[] decodedFile = Base64.getDecoder().decode(mo.filecontent);
            String decodedFileContents = new String(decodedFile);
            saveFileToGDrive(decodedFileContents);
        }

        // Only insert if relevant parts of MessageObj are not null
        if (mo != null && mo.user_id > -1 && mo.title != null && mo.body != null) {
            try {
                //  ((message_id, user_id, title, body, create_date)
                String insertStmt = "INSERT INTO tblmessage VALUES (default, ?, ?, ?, default);";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setInt(1,mo.user_id);
                stmt.setString(2,mo.title);
                stmt.setString(3,mo.body);
                stmt.executeUpdate();
                stmt.close();
            }
            catch (SQLException e) {
                System.out.println("Error in insertMessage(): message insertion failed");
                e.printStackTrace();
                return badData;
            }
            return goodData;
        }
        else {
            return badData;
        }
    }


    /**
     * Method that takes in a new comment object and adds it into the database. (NOTE: Building this into
     * the main App for now because I'm creating this just so I can build the frontend. Configuration
     * shouldn't have to be changed much after this, but for now, I'd like it to all be convenient to find.)
     * @param co    A passed-in CommentObj from the frontend.
     * @return      Either good or bad response to frontend.
     */
    String insertComment(CommentObj co) {
        Connection conn = createConnection();

        // Only insert if relevant parts of MessageObj are not null
        if (co != null && co.user_id > -1 && co.message_id > -1 && co.comment_text != null) {
            try {
                //  (comment_id, user_id, message_id, comment_text, create_date)
                String insertStmt = "INSERT INTO tblcomments VALUES (default, ?, ?, ?, default);";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setInt(1,co.user_id);
                stmt.setInt(2,co.message_id);
                stmt.setString(3,co.comment_text);
                stmt.executeUpdate();
                stmt.close();
            }
            catch (SQLException e) {
                System.out.println("Error in insertComment(): comment insertion failed");
                e.printStackTrace();
                return badData;
            }
            return goodData;
        }
        else {
            return badData;
        }
    }

    /**
     * Method that takes in a new VoteObj object and adds it into the database. (NOTE: Building this into
     * the main App for now because I'm creating this just so I can build the frontend. Configuration
     * shouldn't have to be changed much after this, but for now, I'd like it to all be convenient to find.)
     * @param vo    A passed-in VoteObj from the frontend.
     * @param ud    Boolean where true == upvote, false == downvote.
     * @return      Either good or bad response to frontend.
     */
    String insertUpDownVote(VoteObj vo, boolean ud) {
        Connection conn = createConnection();
        String insertStmt;

        // Determine if up/down vote (upvote will be true, downvote will be false):
        if (ud)     insertStmt = "INSERT INTO tblupvotes VALUES (?,?);";
        else        insertStmt = "INSERT INTO tbldownvotes VALUES (?,?);";

        if (vo != null && vo.message_id > -1 && vo.user_id > -1) {
            try {
                //  (user_id, message_id)
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setInt(1,vo.user_id);
                stmt.setInt(2,vo.message_id);
                stmt.executeUpdate();
                stmt.close();
            }
            catch (SQLException e) {
                System.out.println("Error in insertUpDownVote(): vote insertion failed");
                e.printStackTrace();
                return badData;
            }
            return goodData;
        }
        else {
            return badData;
        }
    }

    /**
     * Execute an UPDATE query to modify table contents. Done by passing a boolean isLiked to indicate
     * whether it's a LIKE or DISLIKE.
     * @param   idNum               This is the index of the values to change.
     * @param   numLikes            This is the original number of likes.
     * @param   newLastLikeDate     This is the value to update time of last like/dislike.
     * @param   isLiked             If true, it's a LIKE. If false, it's a DISLIKE.
     * DEPRECATED: Refer to insertUpDownVote() instead for proper POSTGRESQL syntax.
     * Also, let's be honest: this method is TRASH.
     */
    void updateLike(int idNum, int numLikes, Date newLastLikeDate, Boolean isLiked) {
        Connection conn = createConnection();

        int newNumLikes = 0;

        // Check if it's like/dislike and change numLikes accordingly.
        if (isLiked)    newNumLikes = ++numLikes;
        else            newNumLikes = --numLikes;

        try {
            String updateStmt = "UPDATE tbldata SET numLikes = ?, lastLikeDate = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateStmt);
            stmt.setInt(1, newNumLikes);
            stmt.setTimestamp(2, javaDateToSqlDate(newLastLikeDate));
            stmt.setInt(3, idNum);
            stmt.executeUpdate();
            stmt.close();
            //conn.close(); I don't think we need this
        } catch (SQLException e) {
            System.out.println("Error: unable to update row");
            e.printStackTrace();
        }
    }


    /**
     * Constructs an App object which creates a new Database and Gson Object to be used later by the different routes
     * This object is used to store the Database for each instance.
     */
    public App() {
        createDB();
        gson = new Gson();
    }

    public App(boolean needDrop) {
        if (needDrop) {
            dropDB();
        }
        createDB();
        gson = new Gson();
    }

    /**
     * This method runs a command that drops all of the used tables in mydb.
     */
    private static void dropDB() {
        Connection conn = createConnection();

        try {
            // DROP ALL TABLES! IF THEY EXIST.
            PreparedStatement stmt, stmt2, stmt3, stmt4, stmt5;
            stmt = conn.prepareStatement("DROP TABLE IF EXISTS tbluser CASCADE");
            stmt.execute();
            stmt.close();
            stmt2 = conn.prepareStatement("DROP TABLE IF EXISTS tblmessage CASCADE");
            stmt2.execute();
            stmt2.close();
            stmt3 = conn.prepareStatement("DROP TABLE IF EXISTS tblcomments CASCADE");
            stmt3.execute();
            stmt3.close();
            stmt4 = conn.prepareStatement("DROP TABLE IF EXISTS tbldownvotes CASCADE");
            stmt4.execute();
            stmt4.close();
            stmt5 = conn.prepareStatement("DROP TABLE IF EXISTS tblupvotes CASCADE");
            stmt5.execute();
            stmt5.close();
        } catch (SQLException e) {
            System.out.println("Error: droptable error");
            e.printStackTrace();
            return;
        }
    }

    /**
     * This method actually creates the database in Docker that is used to store values passed from
     * MySQL commands.
     */
    public void createDB() {
        Connection conn = createConnection();

        PreparedStatement createTblUser, createTblMessage, createTblComments, createTblDownVotes, createTblUpVotes;

        // MAJOR PROBLEM HERE WAS SYNTAX. Needed to primarily update variable types, as expected.
        try {
            // Create tblUser
            createTblUser = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tblUser (" +
                    "user_id SERIAL PRIMARY KEY," +
                    "username VARCHAR(255)," +
                    "realname VARCHAR(255)," +
                    "email VARCHAR(255)" +
                    ");");
            createTblUser.executeUpdate();
            createTblUser.close();
            //System.out.println("DEBUG:: Created tblUser");

            // Create tblMessage
            createTblMessage = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tblMessage (" +
                    "message_id SERIAL PRIMARY KEY," +
                    "user_id INTEGER, title VARCHAR(50)," +
                    "body VARCHAR(140)," +
                    "create_date TIMESTAMP NOT NULL DEFAULT (now() at time zone 'utc')," +
                    "FOREIGN KEY (user_id) REFERENCES tblUser (user_id)" +
                    ");");
            createTblMessage.executeUpdate();
            createTblMessage.close();
            //System.out.println("DEBUG:: Created tblMessage");

            // Create tblComments
            createTblComments = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tblComments (" +
                    "comment_id SERIAL PRIMARY KEY," +
                    "user_id INTEGER," +
                    "message_id INTEGER," +
                    "comment_text VARCHAR(255)," +
                    "create_date TIMESTAMP NOT NULL DEFAULT (now() at time zone 'utc')," +
                    "FOREIGN KEY (user_id) REFERENCES tblUser (user_id)," +
                    "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)" +
                    ");");
            createTblComments.executeUpdate();
            createTblComments.close();
            //System.out.println("DEBUG:: Created tblComments");

            // Create tblDownVotes
            createTblDownVotes = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tblDownVotes (" +
                    "user_id INTEGER," +
                    "message_id INTEGER," +
                    "FOREIGN KEY (user_id) REFERENCES tblUser (user_id)," +
                    "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)," +
                    "PRIMARY KEY (user_id, message_id)" +
                    ");");
            createTblDownVotes.executeUpdate();
            createTblDownVotes.close();
            //System.out.println("DEBUG:: Created tblDownVotes");

            // Create tblUpVotes
            createTblUpVotes = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tblUpVotes (" +
                    "user_id INTEGER," +
                    "message_id INTEGER," +
                    "FOREIGN KEY (user_id) REFERENCES tblUser (user_id)," +
                    "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)," +
                    "PRIMARY KEY (user_id, message_id)" +
                    ");");
            createTblUpVotes.executeUpdate();
            createTblUpVotes.close();
            //System.out.println("DEBUG:: Created tblUpVotes");

            //conn.close();
        } catch (SQLException e) {
            // Should we handle this in a better way?
            System.out.println("ERROR in createDB(): table(s) failed to be created");
            e.printStackTrace();
            System.exit(-1);
        }

        //System.out.println("DEBUG:: All tables created; now exiting.");
        //System.exit(0);
    }

    /**
     * Method that converts a Java Date to a Timestamp object.
     * @param myDate    Date formatted as java.util.Date, to be converted to java.sql.Date
     * @return          Returns Date as Timestamp object.
     */
    public static java.sql.Timestamp javaDateToSqlDate(java.util.Date myDate) {
        return new java.sql.Timestamp(myDate.getTime());
    }

    /**
     * Method that converts a Timestamp to a Java Date object.
     * @param myDate    Date formatted as java.sql.Date, to be converted to java.util.Date
     * @return          Returns Date as Timestamp object.
     */
    public static java.util.Date sqlDateToJavaDate(java.sql.Timestamp myDate) {
        // This implementation is sort of annoying. I'm using a Calendar between the Timestamp
        // and Date objects. So I create a Calendar, set it using the timestamp, and finally
        // convert the Calendar to a Date object, which will be returned.
        Calendar curCal = Calendar.getInstance();
        curCal.setTimeInMillis( myDate.getTime() );
        return curCal.getTime();
    }

    /**
     * Method that attempts to log a user in regardless of prior existence in DB and on site.
     * @param username  Given username.
     * @param password  Given password.
     * @return          Secret_key as JSON if successful, badData if unsuccessful.
     */
    public String login(String username, String password) {
        // FIRST, DO OAUTH VALIDATION
        String apiKey = oValidate(username, password);
        if (apiKey != null) {
            // IF OAUTH VALIDATES CORRECTLY, CALL ADDUSER() TO HANDLE LOGIN
            int sK = addUser(username, apiKey);
            return "{\"username\":\"" + username + "\",\"secret_key\":" + sK + "}";
        }
        else {
            // IF OAUTH VALIDATION FAILS, RETURN BADDATA
            return badData;
        }
    }

    /**
     * Method that performs OAuth validation. If it receives any API code from OAuth, return
     * true. Else, return false.
     * @param username  Given username.
     * @param password  Given password.
     * @return          API key if successful, null if unsuccessful.
     * TODO: Use actual OAuth validation in this method, aka Phase 3. No time in Phase 4 to do this, sorry.
     */
    public static String oValidate(String username, String password) {
        if (username != null && password != null)   return "oauth_api_key";
        else                                        return null;
    }

    /**
     * Method that logs in user after OAuth validation succeeds. Uses username,
     * api key, and current date to build a secret key to add to hashmap and insertUser()
     * if need be. To be called only after validation by login() method.
     * @param username  Given username.
     * @param apiKey    Given API key.
     * @return          Secret key.
     */
    public int addUser(String username, String apiKey) {
        // Create hashcode int (to serve as secret key)
        Date curDate = new Date();
        String toHash = username + apiKey + curDate.toString();
        int hashCode = toHash.hashCode();
        MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_sk, mc_password_sk, mc_server_sk);
        try {
            mc.set(username, 3600, hashCode);
        }
        catch (NullPointerException ex) {
            System.out.println("ERROR in addUser(): unable to set() in memcachier!");
        }
        //hashtable.put(username, hashCode);  // replaced by Memcachier in phase 4
        int inTable = 0;

        // Check to see if user exists in the table; add if doesn't exist.
        Connection conn = createConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS intable FROM tbluser WHERE username = ?;");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                inTable = rs.getInt("intable");
            }
            // Means that user isn't in table; needs to be added. Otherwise, in table.
            if (inTable == 0) {
                // When using OAuth, we should be able to retrieve realname for the user.
                this.insertUser(new UserObj(username, username, username + "@lehigh.edu"));
            }
        }
        catch (SQLException e) {
            System.out.println("ERROR IN addUser(): failed to query for if user in table");
            e.printStackTrace();
        }

        return hashCode;
    }

    /**
     * Method that validates a username + secret key before allowing a user to proceed with any
     * non-login action. Should be called before any route call in main() below.
     * @param username  Given username.
     * @param secretKey Given secret key.
     * @return          True if validation is successful, false if validation fails.
     */
    public static boolean validateAction(String username, int secretKey) {
        // Successful validation case
        int sK;
        try {
            MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_sk, mc_password_sk, mc_server_sk);
            sK = (Integer) mc.get(username);
        }
        catch (NullPointerException ex) {
            sK = secretKey + 1; // cannot equal secretKey and validate!
        }
        if (sK == secretKey)      return true;
        else                      return false;
    }

    public static boolean validateAction(UserStateObj uso) {
        // Successful validation case
        int sK;
        try {
            MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_sk, mc_password_sk, mc_server_sk);
            sK = (Integer) mc.get(uso.username);
        }
        catch (NullPointerException ex) {
            sK = uso.secret_key + 1;    // cannot equal secretKey and validate!
        }
        if (sK == uso.secret_key) return true;
        else                      return false;
    }

    /**
     * Method that logs a user out. Maybe should be called regardless of success of secret key? If it's
     * invalid, then shouldn't a user not be logged in? Or would this just be an opportunity for trolls?
     * For the moment however, only logout if secret key is true, may change later.
     * @param username  Given username.
     * @param secretKey Given secret key.
     * @return goodData if successful, badData if unsuccessful.
     */
    public String logout(String username, int secretKey) {
        MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_sk, mc_password_sk, mc_server_sk);
        if (validateAction(username, secretKey)) {
            mc.delete(username);
            return goodData;
        }
        else return badData;
    }

    /**
     * Method that takes in a File object from frontend and uploads it to the Google Drive.
     * @param  filecontents Given raw string-formatted file contents.
     * @return fileid       URL of the created file from file contents.
     */
    public String saveFileToGDrive(String filecontents) {
        // Build a new authorized API client service.
        //System.out.println("TEST: saveFileToGDrive() started.");
        //System.out.println("TEST: quickstart object created.");

        // Write "filecontents" to a file
        String filepath = "src/main/resources/files/myfilename.txt";
        //System.out.println("File contents: " + filecontents);
        String fileid;

        try {
            // First, write contents to the file
            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            writer.println(filecontents);
            writer.close();

            // Then write this to a file
            Drive service = Quickstart.getDriveService();
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName("myfilename.txt");
            java.io.File filePath = new java.io.File(filepath);
            //filePath.createNewFile();
            FileContent mediaContent = new FileContent("text/plain", filePath);
            com.google.api.services.drive.model.File file = service.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            //System.out.println("File ID: " + file.getId());
            fileid = file.getId();

            // Then add the file to the cache.
            addFileToCache(fileid, filecontents);
        }
        catch (IOException ex) {
            System.out.println("IOException in saveFileToGDrive() caught");
            ex.printStackTrace();
            fileid = "";
            System.exit(-1);
        }

        System.out.println("File contents: " + filecontents);

        System.out.println("TEST: saveFileToGDrive() about to finish.");
        return fileid;
    }

    /**
     * Method where, given a file id, retrieves a File object for that file.
     * @param fileid    File ID retrieved from frontend.
     * @return          Filecontents from cache/GDrive load or null if file not found.
     */
    public String getFileFromGDriveById(String fileid) {
        // Let's plan to send the filecontents encoded in base64 back to the frontend/Android.
        // There are two ways this can be done: if the file's in the cache, load it from there.
        // Otherwise, we'll need to load the file contents from the GDrive.

        // Let's figure out first if the file at the given fileid is in the cache in hopes of
        // saving valuable milliseconds for our die hard fans!!!!!
        MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_fc, mc_password_fc, mc_server_fc);
        Object o;   // stores retrieved content from cache
        String filecontents;    // stores retrieved filecontents from cache/Drive (if not found null)
        try {
            o = mc.get(fileid);
        }
        catch (NullPointerException ex) {
            o = null;
        }

        // Then retrieve filecontents, either using GDrive or Memcachier file cache.
        if (o != null) {    // means that the given key matched something in the cache! yay for efficiency!!
            filecontents = o.toString();
        }
        else { // look for file on GDrive since it's definitely not in cache
            try {
                Drive service = Quickstart.getDriveService();
                com.google.api.services.drive.model.File file = service.files().get(fileid).execute();
                filecontents = file.getWebContentLink();
                //String filemimetype = file.getMimeType();
                //String filetitle = file.getTitle();
                //String filedescr = file.getDescription();
            }
            catch (IOException ex) {
                System.out.println("ERROR in getFileFromGDrive(): Not in cache or in GDrive, load failed! Null returned.");
                filecontents = null;
            }
        }

        //return "https://drive.google.com/file/d/" + fileid;
        return filecontents;
    }

    /**
     * Method called by saveFileToGDrive() that adds a file at filepath to a memcachier.
     * @param fileid        Fileid given by GDrive, used as key in memcachier.
     * @param filecontents  Contents we want to cache.
     */
    public static void addFileToCache(String fileid, String filecontents) {
        // Get memcachier connection to our file-caching server.
        MemcachedClient mc = MemcachedObj.getMemcachedConnection(mc_username_fc, mc_password_fc, mc_server_fc);
        try {
            mc.add(fileid, 3600, filecontents);
        }
        catch (NullPointerException ex) {
            System.out.println("ERROR in addFileToCache(): Unable to add " + fileid + " to cache!");
        }
    }

    /**
     * Main method which holds all get and post routes for updating and sending the database to the server.
     * Each route is formed as a lambda function which returns a GSON object to be passed.
     * @param args  Standard Java main class argument.
     */
    public static void main(String[] args) {
        // "Do this early in your main" -- Prof
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR IN main(): Driver not found");
            e.printStackTrace();
            System.exit(-1);
        }

        App app = new App(true);
        staticFileLocation(sFileLocation);

        // GET '/' returns the index page
        // (Leaving this alone, at least for now.)
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // GET '/data' returns a JSON string with all of the data in the POSTGRES db.
        get("/data", (req, res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                String result = app.getAllMessages();
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // DEPRECATED VERSION OF POST MESSAGE:
//        // POST a new item into the db
//        post("/data", (req, res) -> {
//            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
//                return badData;
//            }
//            else {
//                MessageObj mo = app.gson.fromJson(req.body(), MessageObj.class);
//                String result = app.insertMessage(mo);
//                res.status(200);
//                res.type("application/json");
//                return result;
//            }
//        });

        // POST a new +vote into db
        post("/data/vote/up", (req, res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                VoteObj vo = app.gson.fromJson(req.body(), VoteObj.class);
                String result = app.insertUpDownVote(vo,true);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // POST a new -vote into db
        post("/data/vote/down", (req, res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                VoteObj vo = app.gson.fromJson(req.body(), VoteObj.class);
                String result = app.insertUpDownVote(vo,false);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // PHASE 2 ROUTES (written by Alex Van Heest):

        // GET message and votes for one page
        // TODO: Add the Comments section as separate route.
        get("/data/message/:id", (req,res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                int idx = Integer.parseInt(req.params("id"));
                String result = app.getMessageContentAndVotes(idx);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // POST a comment for a given message
        post("/data/message/comment/:id", (req,res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                CommentObj co = app.gson.fromJson(req.body(), CommentObj.class);
                String result = app.insertComment(co);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // GET userdata for a given profile page
        get("/user/:id", (req,res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                int idx = Integer.parseInt(req.params("id"));
                String result = app.getUserData(idx);
                // TBD: Get all comments and messages as well!
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        post("/login", (req,res) -> {
            LoginObj lo = app.gson.fromJson(req.body(), LoginObj.class);
            String result = app.login(lo.username, lo.password);
            //System.out.println(result);
            res.status(200);
            res.type("application/json");
            return result;
        });

        get("/logout", (req,res) -> {
            String un = req.cookie("user");
            int us = Integer.parseInt(req.cookie("session"));
            if (!validateAction(un, us)) {
                return badData;
            }
            else {
                String result = app.logout(un, us);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });

        // PHASE 4 ROUTES (written by Alex Van Heest):
        // POST a new item into the db and receive optional file content (handled in insertMessage()...)
        post("/data", (req, res) -> {
            if (!validateAction(req.cookie("user"), Integer.parseInt(req.cookie("session")))) {
                return badData;
            }
            else {
                MessageObj mo = app.gson.fromJson(req.body(), MessageObj.class);
                String result = app.insertMessage(mo);
                res.status(200);
                res.type("application/json");
                return result;
            }
        });
    }


}