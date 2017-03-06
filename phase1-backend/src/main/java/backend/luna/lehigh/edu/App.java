package backend.luna.lehigh.edu;

import static spark.Spark.*;
import com.google.gson.*;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.lang.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alex Van Heest, Kieran Horan
 * @version 1.1
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
    String author;
    int numLikes;
    java.util.Date uploadDate;
    java.util.Date lastLikeDate;
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

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
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
                currentDatum.author = rs.getString("author");
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
                stmt.setString(6,d.userName);
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
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in updateLike");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
            e.printStackTrace();
            return;
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
                System.out.println("Error: getConnection returned null object in createDB");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection in createDB threw an exception");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
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
            System.out.println("Error: getConnection in createDB threw an exception");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception in getAllData");
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
                updateLike(idx, d.numLikes, d.lastLikeDate, true);
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
                updateLike(idx, d.numLikes, d.lastLikeDate, false);
                return goodData;
            }
            else {
                return badData;
            }
        });
    }
}