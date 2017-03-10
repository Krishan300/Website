package backend.luna.lehigh.edu;

import static spark.Spark.*;
import com.google.gson.*;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.util.Date;

/**
 * @author Alex Van Heest, Kieran Horan, Robert Salay
 * @version 1.2
 */


/**
 * Datum object is the base object for storing our data on the backend. We store an index, title, comment,
 * number of likes, upload date, and like date.
 */
class Datum {
    int index;
    String title;
    String comment;
    String userName;
    String userToken;
    int author;
    int numLikes;
    java.util.Date uploadDate;
    java.util.Date lastLikeDate;
}

class logDatum {
    String userName;
    String password;
}

class pwDatum {
    String userName;
    String oldPW;
    String newPW;
}

class signDatum {
    String userName;
    String passWord;
    String eMail;
}




/**
 * The App class creates an App object which passes in a MySQL database and stores the
 * data in rows to be pulled from later.
 */
public class App {
    // Final static strings, used throughout program.
    final static String goodData = "{\"res\":\"ok\"}";
    final static String badData = "{\"res\":\"bad data\"}";
    final static String sFileLocation = "/web";        // FIX FOR WHATEVER THE HIERARCHY IT IS IN FINAL VERSION
    Connection conn = null;


    // Only one gson instantiation, for efficiency.
    final static Gson gson = new Gson();

    // Environment variables.
    //static Map<String, String> env = System.getenv();
    //static String ip = env.get("MYSQL_IP");
    //static String port = env.get("MYSQL_PORT");
    //static String user = env.get("MYSQL_USER");
    //static String pass = env.get("MYSQL_PASS");
    //static String db = env.get("MYSQL_DB");

    //new getConnection method for postgre
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }

    //creates salt bytes for hashSalting process
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //create array for salt
        byte[] salt = new byte[16];
        //get random salt
        sr.nextBytes(salt);
        return salt;
    }

    //hashSalts a given password, used for both storing and comparing
    private static String getSecurePassword(String password2Hash, byte[] salt){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            md.update(salt);
            //Get hash's bytes
            byte[] bytes = md.digest(password2Hash.getBytes());
            // convert decimal format to hex
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Could not hash password");
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //creates salted and hashed value using previously saved salt
    private static String hashPreviousPass(String password, byte[] salt){
        String genPass;

        genPass = getSecurePassword(password, salt);
        return genPass;
    }

    //creates salted and hashed value, and saves salt in pw table
    private static String hashPass(String password, String userName) throws NoSuchProviderException, NoSuchAlgorithmException{
        Connection conn;
        String genPass;
        byte[] salt = getSalt();
        genPass = getSecurePassword(password, salt);

        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in hashPass");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in hashPass");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in hashPass");
            e.printStackTrace();
            return null;
        }

        ResultSet rs;

        try {
            String updateStmt = "UPDATE pUserData SET salt = ?, saltPW = ? WHERE userName = ?";
            PreparedStatement stmt = conn.prepareStatement(updateStmt);
            stmt.setBytes(1, salt);
            stmt.setString(2, genPass);
            stmt.setString(3, userName);
            stmt.execute();
        } catch (SQLException e){
            System.out.println("Error while storing salt");
            e.printStackTrace();
        }
        return genPass;
    }
    //gets salt for passwords previously used, so people can log in
    static byte[] getSavedSalt(String UN) {
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getSavedSalt");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in getSavedSalt");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getSavedSalt");
            e.printStackTrace();
            return null;
        }
        int idNum = getUserID(UN);
        byte[] saltResult = null;
        ResultSet rs;

        try {
            String getStmt = "SELECT salt FROM pwHash WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, idNum);
            rs = stmt.executeQuery();
            saltResult = rs.getBytes("salt");
        } catch (SQLException e) {
            System.out.println("Error while collecting previous salt");
            e.printStackTrace();
        }
        return saltResult;
    }

    //produces the id number of a user from their username
    private static int getUserID(String UN){
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getUserID");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in getUserID");
            e.printStackTrace();
            return -1;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getUserID");
            e.printStackTrace();
            return -1;
        }
        int idNum = -1;
        ResultSet rs;
        try {
            String getStmt = "SELECT userID FROM userData WHERE userName = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setString(1, UN);
            rs = stmt.executeQuery();
            idNum = rs.getInt("userID");
        } catch (SQLException e){
            System.out.println("Error while collecting user ID from Name");
            e.printStackTrace();
        }
        return idNum;
    }

    //compares a given password with the stored password data
    static Boolean comparePW(String UN, String saltedPW){
        Connection conn = null;
        int uID = getUserID(UN);
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in comparePW");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in comparePW");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in comparePW");
            e.printStackTrace();
        }

        String savedSalt = "";
        ResultSet rs;
        try {
            String getStmt = "SELECT saltedPW FROM pwHash WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, uID);
            rs = stmt.executeQuery();
            savedSalt = rs.getString("saltedPW");
        } catch (SQLException e) {
            System.out.println("Error while comparing stored and supplied pws");
            e.printStackTrace();
        }
        return savedSalt.equals(saltedPW);
    }

    //Creates a user token for login, should be unique for each user, and each time the user logs in
    static String makeToken(String UN) {
        Random ran = new Random();
        int x = ran.nextInt(99999);
        String uToken = UN + x;
        return uToken;
    }

    //using the username provided, returns a json containing this users bio, liked comments, and made comments
    static String getUserData(String userName) {
        int uID = getUserID(userName);
        Connection conn = null;



        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getUserData");
                return badData;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in getUserData");
            e.printStackTrace();
            return badData;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getUserData");
            e.printStackTrace();
            return badData;
        }
        String[] result = new String[3];
        // watch multiples
        //get bio
        try {
            String getStmt = "SELECT bio FROM userData WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, uID);

            ResultSet rs = stmt.executeQuery();

            result[0] = rs.getString("bio");
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
            return badData;
        }
        //get all comments this user liked
        ArrayList<Datum> likeResults = new ArrayList<>();
        try {
            String getStmt = "SELECT commentID FROM voteData WHERE userID = ? AND voteUp = True";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, uID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Datum currentDatum = getComment(rs.getInt("id"));
                likeResults.add(currentDatum);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
            return badData;
        }
        result[1] = gson.toJson(likeResults);
        //get all comments this user made
        ArrayList<Datum> authorResults = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblData WHERE author = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1,uID);

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
                currentDatum.author = rs.getInt("author");
                authorResults.add(currentDatum);
            }
            stmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
            return badData;
        }
        // Convert the array of results to a JSON string and return it
        result[2] = gson.toJson(authorResults);
        String stringRes = gson.toJson(result);
        return stringRes;
    }

    //returns comment data from ID number
    static Datum getComment(int commentID) {
        Connection conn = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getComment");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in getComment");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getComment");
            e.printStackTrace();
            return null;
        }
        Datum d = new Datum();

        try {
            String getStmt = "SELECT * FROM tblData WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();

            d.index = rs.getInt("id");
            d.title = rs.getString("title");
            d.comment = rs.getString("comment");
            d.numLikes = rs.getInt("numLikes");
            d.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
            d.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
            d.author = rs.getInt("author");
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        return d;
    }
    /**
     * Get all data from our database and returns it in JSON format.
     * @return JSON object from SQL frontend
     */
    static String getAllData() {
        // get the MYSQL configuration from the environment
        Connection conn = null;



        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getAllData");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in getAllData");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
            e.printStackTrace();
            return null;
        }

        String result = "";
        // watch multiples
        ArrayList<Datum> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblData";
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
                currentDatum.author = rs.getInt("author");
                results.add(currentDatum);
            }
            stmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result = gson.toJson(results);
        return result;
    }

    /**
     * This insert takes new data from the frontend sever and adds it into the datum database
     * @param   d   A Datum object retrieved by and built through Spark framework
     * @return command telling server if addition was a success or not
     */
    static String insertDatum(Datum d) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        //System.out.println("Connecting to " + ip + ":" + port + "/" + db);
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in insertDatum");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in insertDatum");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
            e.printStackTrace();
            return null;
        }
        // Only insert if whole datum is not null
        if (d != null && d.title != null && d.comment != null && d.numLikes == 0 && d.uploadDate != null && d.lastLikeDate != null) {
            try {
                //  (id, title, comment, numLikes, uploadDate, lastLikeDate)
                String insertStmt = "INSERT INTO tblData VALUES (default, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,d.title);
                stmt.setString(2,d.comment);
                stmt.setInt(3,d.numLikes);
                stmt.setTimestamp(4,javaDateToSqlDate(d.uploadDate));
                stmt.setTimestamp(5,javaDateToSqlDate(d.lastLikeDate));
                int idx = getUserID(d.userName);
                stmt.setInt(6,idx);
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
     * Execute an UPDATE query to modify table contents. Done by passing a boolean isLiked to indicate
     * whether it's a LIKE or DISLIKE.
     * @param   idNum               This is the index of the values to change.
     * @param   numLikes            This is the original number of likes.
     * @param   newLastLikeDate     This is the value to update time of last like/dislike.
     * @param   isLiked             If true, it's a LIKE. If false, it's a DISLIKE.
     */
    static void updateLike(int idNum, int numLikes, Date newLastLikeDate, String userName, Boolean isLiked) {
        // get the MYSQL configuration from the environment
        Connection conn = null;
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in updateLike");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in updateLike");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
            e.printStackTrace();
            return;
        }
        int newNumLikes = 0;
        int pastVoteID = -1;
        boolean pastVote = false;
        try {
            String getStmt = "SELECT id,voteUp FROM voteData WHERE commentID = ? AND userID = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, idNum);
            stmt.setInt(2, getUserID(userName));
            ResultSet rs = stmt.executeQuery();
            pastVoteID = rs.getInt("id");
            pastVote = rs.getBoolean("voteUp");
        } catch (SQLException e) {
            System.out.println("Error: unable to find voteData table");
            e.printStackTrace();
        }

        if (pastVoteID >= 0) {
            if (pastVote) {
                newNumLikes = --numLikes;
            } else {
                newNumLikes = ++numLikes;
            }

            try {
                String deleteStmt = "DELETE FROM voteData WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteStmt);
                stmt.setInt(1, pastVoteID);
                stmt.execute();
            } catch (SQLException e) {
                System.out.println("Error: unable to delete voteData row");
                e.printStackTrace();
            }
        } else {
            // Check if it's like/dislike and change numLikes accordingly.
            if (isLiked) {
                newNumLikes = ++numLikes;
            } else {
                newNumLikes = --numLikes;
            }
        }

            // add vote to voteTbl
            try {
                String updateStmt = "UPDATE tblData SET numLikes = ?, lastLikeDate = ? WHERE id = ?";
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

            // check if this user has already voted
    }


    /**
     * Constructs an App object which creates a new Database and Gson Object to be used later by the different routes
     * This object is used to store the Database for each instance.
     */

    /**
     * This method runs a command that drops the database tblData. Made private to ensure "adversaries"
     * can't access it since it is highly dangerous in the wrong hands. Called within createDB() to ensure
     * that our Docker instance is cleared. Especially useful for tests.
     */
    private static void dropDB() {
        Connection conn = null;

        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in dropDB");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an SQL exception in dropDB");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in dropDB");
            e.printStackTrace();
            return;
        }

        try {
            PreparedStatement stmt = null;
            String createStatement = "DROP TABLE IF EXISTS tblData";
            stmt = conn.prepareStatement(createStatement);
            stmt.execute();
            stmt.close();
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
        // Quickly make sure to drop table if exists. Used primarily for ease of testing.
        // NOTE: This only works at the beginning of this because we create a new App object each time
        // we need this connection. MAIN only uses one instance of App, so it won't accidentally delete.
        dropDB();

        Connection conn = null;

        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in createDB");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an SQL exception in CreateDB");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in CreateDB");
            e.printStackTrace();
            return;
        }
        // Create a table to store data.  It matches the 'Datum' type from the
        // previous tutorial.

        PreparedStatement stmt = null;
        String createStatement = "CREATE TABLE tblData (id INT(64) NOT NULL AUTO_INCREMENT, title VARCHAR(200), comment VARCHAR(200), numLikes INT(64), uploadDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, lastLikeDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(id))";
        try {
            stmt = conn.prepareStatement(createStatement);
            stmt.execute();
            stmt.close();
            //conn.close();
        } catch (SQLException e) {
            // Should we handle this in a better way?
            System.out.println("Table not created (it may already exist)");
        }
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

    // returns true if the user token matches the most recent token paired with that user name
    public static boolean validateUserToken(String uT, String uN){
        Connection conn = null;

        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in validateUserToken");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an SQL exception in validateUserToken");
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in validateUserToken");
            e.printStackTrace();
            return false;
        }

        try {
            //get username from user data table
            String getStmt = "SELECT userName FROM userData WHERE userToken = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setString(1, uT);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                //get username, if any, that matches the user token in the table
                String uTCheck = rs.getString("userName");
                if (uTCheck.equals(uN)){
                    return true;
                }
            }
            stmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        return false;
    }

    // adds user's sign up info to possible user table. This table includes their desired UN, their email, and
    // a boolean that indicates if their sign up data has been read by the admin app, set to false.
    // returns good data if everything is not null, or bad data if something is null.
    static String signUpUser(signDatum d) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        //System.out.println("Connecting to " + ip + ":" + port + "/" + db);
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in signUpUser");
                return badData;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an SQL exception in signUpUser");
            e.printStackTrace();
            return badData;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in signUpUser");
            e.printStackTrace();
            return badData;
        }
        // Only insert if whole datum is not null
        if (d != null && d.userName != null && d.eMail != null && d.passWord != null) {
            try {
                d.passWord = hashPass(d.passWord, d.userName);
                String insertStmt = "INSERT INTO pUserData VALUES (default, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,d.userName);
                stmt.setString(2,d.eMail);
                stmt.setString(3, d.passWord);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error: insertion failed");
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error: no such algorithm");
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                System.out.println("Error: no such provider");
                e.printStackTrace();
            }
            return goodData;
        } else {
            return badData;
        }
    }

    public static String changePW(pwDatum d) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        //System.out.println("Connecting to " + ip + ":" + port + "/" + db);
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object while changing passwords");
                return badData;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in changePW");
            e.printStackTrace();
            return badData;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in changePw");
            e.printStackTrace();
            return badData;
        }
        d.newPW = hashPreviousPass(d.newPW, getSavedSalt(d.userName));
        int uID = getUserID(d.userName);

        try {
            String updateStmt = "UPDATE pwHash Set saltedPW = ? WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(updateStmt);
            stmt.setString(1,d.newPW);
            stmt.setInt(2,uID);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error: Query Failed");
            e.printStackTrace();
            return badData;
        }
        return goodData;
    }
    /**
     * Main method which holds all get and post routes for updating and sending the database to the server.
     * Each route is formed as a lambda function which returns a GSON object to be passed.
     * @param args  Standard Java main class argument.
     */
    public static void main( String[] args ) {
        // Set up static file service WHAT IS THE HIERARCHY HERE
        staticFileLocation(sFileLocation);

        // GET '/' returns the index page
        // (Leaving this alone, at least for now.)
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // GET '/data' returns a JSON string with all of the data in
        // the MySQL database.
        get("/data", (req, res) -> {
            String result = getAllData();
            // send a JSON object back
            res.status(200);
            res.type("application/json");
            return result;
        });

        // POST a new item into the database
        post("/data", (req, res) -> {
            // Try to create a Datum from the request object
            Datum d = gson.fromJson(req.body(), Datum.class);
            if (validateUserToken(d.userToken, d.userName)){
                String result = insertDatum(d);
                res.status(200);
                res.type("application/json");
                return result;
            }else{
                res.status(417);
                res.type("application/json");
                return null;
            }
        });


        // Route for RECORDING A LIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL FOR EVERYTHING EXCEPT LIKE AND LLDATE
        post("/data/like/up/:id", (req, res) -> {
            // Call the update method above with the new datum object
            Datum d = gson.fromJson(req.body(), Datum.class);
            if (validateUserToken(d.userToken, d.userName)){
                int idx = Integer.parseInt(req.params("id"));
                updateLike(idx, d.numLikes, d.lastLikeDate, d.userName, true);
                return goodData;
            }
            else {
                return badData;
            }
        });

        // Route for RECORDING A DISLIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL to like but decrements numlikes instead of incrementing it
        post("/data/like/down/:id", (req, res) -> {
            // Call the update method above with the new datum object
            Datum d = gson.fromJson(req.body(), Datum.class);
            if(validateUserToken(d.userToken, d.userName)) {
                int idx = Integer.parseInt(req.params("id"));
                updateLike(idx, d.numLikes, d.lastLikeDate, d.userName, false);
                return goodData;
            }
            else {
                return badData;
            }
        });

        //Route for Login
        //get route for login, sent Username & password, return validity boolean and user token
        get("/data/login/", (req, res) -> {
            String result = "";
            logDatum d = gson.fromJson(req.body(), logDatum.class);
            d.password = hashPreviousPass(d.password, getSavedSalt(d.userName));
            if (comparePW(d.userName, d.password)) {
                result = makeToken(d.userName);
                res.type("application/json");
                return result;
            }

            return badData;

        });
        //post route for signup, adds UN and email to possible users table
        post("/data/signup/", (req, res) -> {
           signDatum d = gson.fromJson(req.body(), signDatum.class);
           String result = signUpUser(d);
            res.status(200);
            res.type("application/json");
            return result;
        });

        //get route for user page, returns UN, a bio, all made comments, and all liked comments
        get("/data/userpage/:UN/", (req, res) -> {
            Datum d = gson.fromJson(req.body(), Datum.class);
            if(validateUserToken(d.userToken, d.userName)) {
                String userName = req.params("UN");
                String result = getUserData(userName);
                res.status(200);
                res.type("application/json");
                return result;
            }

            return badData;
        });

        post("/data/pwchange/", (req, res) -> {
            pwDatum d = gson.fromJson(req.body(), pwDatum.class);
            String result = badData;
            d.oldPW = hashPreviousPass(d.oldPW, getSavedSalt(d.userName));
            if (comparePW(d.userName, d.oldPW)) {
                result = changePW(d);
            }
            return result;
        });
    }
}