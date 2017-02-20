package backend.luna.lehigh.edu;

import static spark.Spark.*;
import com.google.gson.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.lang.*;

/**
 * @author Charles Mandala
 * @version 1.0
 */


/**
 * We define a Datum which holds each row of data
 * Each row in Datum objrct is a specific post with has, in order:
 * index | Title (str) | Comment (str) | likes (int) | UploadDate (DATEtime obj) | likeDate (DATEtime OBJ)
 * P1 CAM
 */
class Datum {
    int index;
    String title;
    String comment;
    int numLikes;
    Date uploadDate;
    Date lastLikedDate;
}


/**
 * The App class creates an App object which passes in a MySQL database and stores the
 * data in rows to be pulled from later
 */
public class App {
    // A JSON parser. We will only have one

    final static String goodData = "{\"res\":\"ok\"}";
    final static String badData = "{\"res\":\"bad data\"}";
    final static String sFileLocation = "/web";        // FIX FOR WHATEVER THE HIERARCHY IT IS IN FINAL VERSION

    final Gson gson;

    static Map<String, String> env = System.getenv();
    static String ip = env.get("MYSQL_IP");
    static String port = env.get("MYSQL_PORT");
    static String user = env.get("MYSQL_USER");
    static String pass = env.get("MYSQL_PASS");
    static String db = env.get("MYSQL_DB");

    /**
     * Get all data from our database and returns it in JSON format.
     * @return JSON object from SQL frontend
     */
    String getAllData() {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db + "?useSSL=false", user, pass);
            if (conn == null) {
                System.out.println("Error: getConnection returned null object in getAllData");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in getAllData");
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
                currentDatum.uploadDate = rs.getDate("uploadDate"); // UPdATE
                currentDatum.lastLikedDate = rs.getDate("lastLikedDate");   // llDATE
                results.add(currentDatum);
            }
            //stmt.close();
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
    String insertDatum(Datum d) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        System.out.println("Connecting to " + ip + ":" + port + "/" + db);
        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" +
                    port + "/" + db + "?useSSL=false", user, pass);
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
        if (d != null && d.title != null && d.comment != null && d.numLikes == 0 && d.uploadDate != null && d.lastLikedDate != null) {
            try {
                String insertStmt = "INSERT INTO tblData VALUES (default, ?, ?, ?, ?, ? )";
                // index | Title (str) | Comment (str) | likes (int) | UploadDate (DATE obj) | lastLikedDate DATE OBJ)
                PreparedStatement stmt = conn.prepareStatement(insertStmt);


                stmt.setString(1,d.title);
                stmt.setString(2,d.comment);
                stmt.setInt(3,d.numLikes);
                stmt.setDate(4,d.uploadDate);
                stmt.setDate(5,d.lastLikedDate);
                // 0 index |  1 Title (str) | 2 Comment (str) | 3 likes (int) | 4 UploadDate (DATE) | 5 lastLikedDate (DATE)
                stmt.executeUpdate();
                //stmt.close();

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
     * Execute an UPDATE query to modify table contents. This is used for both likes and dislikes as the
     * actual increase or decrease in likes will be done on the backend, increasing the count by 1 or decreasing
     * by 1 when a distincive post is made to either like or dislike a post. the last liked time will also be updated
     * @param   idNum       This is the index of the values to change.
     * @param   newNumLikes     This is the value to update amt of likes. passed +1 or -1 if like or dislike
     * @param   newLastLikeDate     This is the value to update time of last like/dislike.
     */
    void updateLike(int idNum, int newNumLikes, Date newLastLikedDate) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" +          // HERE IS WHERE WE CONNECT
                    port + "/" + db, user, pass);
            if (conn == null) {
                System.out.println("Error: getConnection returned null object");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in updateLike");
            e.printStackTrace();
            return;
        }


        // MIGHT NEED TO ADD A CONDITIONAL HERE TO CHECK IF WE ARE PASSED A VALID ID
        // SHould we also check date?

            try {
                String updateStmt = "UPDATE tblData SET numLikes = ?, lastLikedDate = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(updateStmt);
                stmt.setInt(1, newNumLikes);
                stmt.setDate(2, newLastLikedDate);
                stmt.setInt(3, idNum);
                stmt.executeUpdate();
                //stmt.close();
                //conn.close(); I dont think we need this
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

    /**
     * This method actually creates the Database inside the App object that is used to store values passed from
     * MySql commands
     */
    public void createDB() {

        Connection conn = null;

        // Use these to connect to the database and issue commands


        // Connect to the database; fail if we can't
        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" +
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

        PreparedStatement stmt = null;
        // System.out.println("Attempting to create tblData");

        String createStatement = "CREATE TABLE tblData (id INT(64) NOT NULL AUTO_INCREMENT, " +
                "title VARCHAR(200), comment VARCHAR(200), numLikes INT(64), uploadDate DATETIME, lastLikeDate DATETIME, PRIMARY KEY(id))";
        try {
            stmt = conn.prepareStatement(createStatement);
            stmt.execute();
            //stmt.close();
            //conn.close();
        } catch (SQLException e) {
            // Should we handle this in a better way?
            System.out.println("Table not created (it may already exist)");
        }
    }

    /**
     * Main method which holds all get and post routes for updating and sending the database to the server.
     * Each route is formed as a lambda function which returns a GSON object to be passed.
     * @param args  Standard Java main class argument.
     */
    public static void main( String[] args ) {
        App app = new App();
        // Set up static file service WHAT IS THE HIEARCHY HERE
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
            String result = app.getAllData();
            // send a JSON object back
            res.status(200);
            res.type("application/json");
            return result;
        });

        // POST a new item into the database
        post("/data", (req, res) -> {
            // Try to create a Datum from the request object
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            String result = app.insertDatum(d);
            res.status(200);
            res.type("application/json");
            return result;
        });


        // Route for RECORDING A LIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL FOR EVERYTHING EXCEPT LIKE AND LLDATE
        post("/data/like/up/:id", (req, res) -> {
            // Call the update method above with the new datum object
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            int idx = Integer.parseInt(req.params("id"));
            app.updateLike(idx, d.numLikes+1, d.lastLikedDate);
            return goodData;
        });

        // Route for RECORDING A DISLIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL to like but decrements numlikes instead of incrementing it
        post("/data/like/down/:id", (req, res) -> {
            // Call the update method above with the new datum object
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            int idx = Integer.parseInt(req.params("id"));
            app.updateLike(idx, d.numLikes -1, d.lastLikedDate);
            return goodData;
        });
    }
}