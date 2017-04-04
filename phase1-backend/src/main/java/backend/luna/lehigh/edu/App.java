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
import java.util.Calendar;
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

    //java.util.Date lastLikeDate;
}

class logDatum {
    String userName;
    String password;
}

class signDatum {
    String userName;
    String eMail;
}




/**
 * The App class creates an App object which passes in a MySQL database and stores the



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
    final Gson gson;



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
    private static String hashPass(String password, String UN) throws NoSuchProviderException, NoSuchAlgorithmException{
        Connection conn;
        String genPass;
        byte[] salt = getSalt();
        genPass = getSecurePassword(password, salt);

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

        int userID = getUserID(UN);
        ResultSet rs;

        try {
            String updateStmt = "UPDATE pwHash SET salt = ?, saltPW = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateStmt);
            stmt.setBytes(1, salt);
            stmt.setString(2, genPass);
            stmt.setInt(3, userID);
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
            e.printStackTrace();
        }
        return genPass;
    }
    //gets salt for passwords previously used, so people can log in
    static byte[] getSavedSalt(String UN) {
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
        }
        int idNum = getUserID(UN);
        byte[] saltResult = null;
        ResultSet rs;

        try {
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblMessage;";
            System.out.println("Is it working?");
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
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + db + "?useSSL=false", user, pass);

            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getAllData");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in getMessage");
            e.printStackTrace();
            return null;
        }
        int idNum = -1;
        ResultSet rs;
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblMessage;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

    //compares a given password with the stored password data
    static Boolean comparePW(int uID, String saltedPW){
        Connection conn = null;
        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getAllData");
                return false;
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result = gson.toJson(results);
        return result;
    }

    String getUpvotes () {
        // get the MYSQL configuration from the environment
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + db + "?useSSL=false", user, pass);

            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getVotes");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in getVotes");
            e.printStackTrace();
            return null;
        }
        String result = "";
        // watch multiples
        //get bio
        try {
            String getStmt = "SELECT bio FROM userData WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setInt(1, uID);

            ResultSet rs = stmt.executeQuery();

            result = rs.getString("bio");
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
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
        }
        result += gson.toJson(likeResults);
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
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result += gson.toJson(authorResults);
        return result;
    }

    //returns comment data from ID number
    static Datum getComment(int commentID) {
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
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
     * This insert takes a user from the frontend and adds it to the user database
     * @param u User on frontend added to databse

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
        // get the MYSQL configuration from the environment
        Connection conn = null;



        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't

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
                System.out.println("Error: getConnection returned null object");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in insertDatum");
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
     * Execute an UPDATE query to modify contents of both upvote tables. Done by passing a boolean isLiked to indicate
     * whether it's a LIKE or DISLIKE.
     * @param   idNum               This is the index of the values to change.
     * @param   numLikes            This is the original number of likes.
     * @param   newLastLikeDate     This is the value to update time of last like/dislike.
     * @param   isLiked             If true, it's a LIKE. If false, it's a DISLIKE.
     */
    static void updateLike(int idNum, int numLikes, Date newLastLikeDate, Boolean isLiked) {
        // get the MYSQL configuration from the environment
        Connection conn = null;
        int newNumLikes = 0;

        // Check if it's like/dislike and change numLikes accordingly.
        if (isLiked)
        {
            newNumLikes = ++numLikes;
        }
        else
        {
            newNumLikes = --numLikes;
        }

        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +          // HERE IS WHERE WE CONNECT
                    port + "/" + db, user, pass);
            if (conn == null) {
                System.out.println("Error: getConnection returned null object");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in updateLike");
            e.printStackTrace();
            return null;
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
    }


    /**
     * Constructs an App object which creates a new Database and Gson Object to be used later by the different routes
     * This object is used to store the Database for each instance.
     */

    /**
     * This method runs a command that drops the database tblData. Made private to ensure "adversaries"
        }
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
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +
                    port + "/" + db, user, pass);
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in createDB");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an exception");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }



        System.out.println("Got to dropDB() and about to create PS");
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
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +
                    port + "/" + db, user, pass);
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in createDB");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an exception");
            e.printStackTrace();
            return;
        }
        // Create a table to store data.  It matches the 'Datum' type from the
        // previous tutorial.

        PreparedStatement stmt;
       PreparedStatement stmt5;
       String createStatement = "CREATE TABLE IF NOT EXISTS tblData (id SERIAL NOT NULL, title VARCHAR(200), comment VARCHAR(200), numLikes INTEGER NOT NULL, uploadDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, lastLikeDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(id));";

        try {
            stmt5 = conn.prepareStatement(createStatement);
            stmt5.executeUpdate();
            stmt5.close();
            System.out.println("stmt.execute worked. table created");
            //conn.close();
        } catch (SQLException e) {
            // Should we handle this in a better way?
            System.out.println("Table not created (it may already exist)");
        }


        String createUser="CREATE TABLE IF NOT EXISTS tblUser (user_id SERIAL PRIMARY KEY, username VARCHAR (255), realname VARCHAR (255), email VARCHAR(255));";
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
                System.out.println("Error: getConnection returned null object in createDB");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an exception");
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
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
                System.out.println("Error: getConnection returned null object");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in insertDatum");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
            e.printStackTrace();
            return null;
        }
        // Only insert if whole datum is not null
        if (d != null && d.userName != null && d.eMail != null) {
            try {
                //  (id, title, comment, numLikes, uploadDate, lastLikeDate)
                String insertStmt = "INSERT INTO pUserData VALUES (default, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,d.userName);
                stmt.setString(2,d.eMail);
                stmt.setBoolean(3,false);
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

       /* // POST a new item into the messsage database-make a new message
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

    }
}



/*

        // Route for RECORDING A LIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL FOR EVERYTHING EXCEPT LIKE AND LLDATE
        post("upvote", (req, res) -> {
            // Call the update method above with the new datum object
            Vote v=app.gson.fromJson(req.body(), Vote.class);
            int message_id = Integer.parseInt(req.params("message_id"));
            int user_id=Integer.parseInt(req.params(user_id));
            app.updateLike(int messageId, int userId, Boolean isLiked, int numLikes);
            return goodData;
        });

        // Route for RECORDING A DISLIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL to like but decrements numlikes instead of incrementing it
        post("/data/like/down/:id", (req, res) -> {
            // Call the update method above with the new datum object
        });


    }
}
