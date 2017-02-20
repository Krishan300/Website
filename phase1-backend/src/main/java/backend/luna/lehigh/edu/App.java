package backend.luna.lehigh.edu;

import static spark.Spark.*;
import com.google.gson.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.lang.*;

/**
 *
 * Each row in Datum objrct is a specific post with has, in order:
 * index | Title (str) | Comment (str) | likes (int) | UploadDate (DATEtime obj) | likeDate (DATEtime OBJ)
 * P1 CAM
 */
class Datum {
    int index;
    String title;
    String comment;
    int numLikes;
    Date uploadDate;  //DATE OBJECT? DATESTAMP?
    Date lastLikedDate; // Last Liked Date SAME AS ABOVE >????

    // index | Title (str) | Comment (str) | likes (int) | UploadDate (DATE) | lastLikedDate (DATE)
}


/**
 *
 * The App class is written to create an App object which takes in a database
 * in mySQL and through get and post routes will store the data and classify it
 *
 */
public class App {
    // A JSON parser. We will only have one, so that we don't construct these
    // too often, but the cost is that concurrent requests must take turns
    // using the parser.
    final Gson gson;
    static int index = 0;

    static Map<String, String> env = System.getenv();
    static String ip = env.get("MYSQL_IP");
    static String port = env.get("MYSQL_PORT");
    static String user = env.get("MYSQL_USER");
    static String pass = env.get("MYSQL_PASS");
    static String db = env.get("MYSQL_DB");

    /**
     * Get all data from our database and returns it in JSON format.
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
                Datum d = new Datum();
                d.index = rs.getInt("id");
                d.title = rs.getString("title");
                d.comment = rs.getString("comment");
                d.numLikes = rs.getInt("numLikes");
                d.uploadDate = rs.getDate("uploadDate"); // UPdATE
                d.lastLikedDate = rs.getDate("lastLikedDate");   // llDATE
                results.add(d);
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
     */
    String insertDatum(Datum d) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        // Use these to connect to the database and issue commands
        // Connect to the database; fail if we can't
        //System.out.println("Connecting to " + ip + ":" + port + "/" + db);
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
        if (d != null && d.title != null && d.comment != null && d.numLikes >= 0 && d.uploadDate != null && d.lastLikedDate != null) {
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
            return "{\"res\":\"ok\"}";
        } else {
            return "{\"res\":\"bad data\"}";
        }
    }

    /**
     * Execute an UPDATE query to modify table contents. This is used for both likes and dislikes as the
     * actual increase or decrease in likes will be done on the backend, increasing the count by 1 or decreasing
     * by 1 when a distincive post is made to either like or dislike a post. the last liked time will also be updated
     * @param   numLikes     This is the value to update amt of likes.
     * @param   lastLikeDate     This is the value to update time of last like/dislike.
     * @param   which       This is the index of the values to change.
     * @param   inc         This is the value to increase/decrease amount of likes (+1= 1 like, -1= 1 dislike).
     */
    protected static void updateLike(int idNum, int newNumLikes, Date newLastLikedDate, int inc) {
        // get the MYSQL configuration from the environment
        Connection conn = null;

        newNumLikes += inc;

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

        // Check to make sure that neither value is null before running.
        if (newNumLikes >= 0 ) {
            try {
                String updateStmt = "UPDATE tblData SET numLikes = ?, lastLikedDate = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(updateStmt);
                stmt.setInt(1, newNumLikes);
                stmt.setDate(2, newLastLikedDate);
                stmt.setInt(3, idNum);   // S
                stmt.executeUpdate();
                //stmt.close();
                //conn.close(); I dont think we need this
            } catch (SQLException e) {
                System.out.println("Error: unable to update row");
                e.printStackTrace();
            }
        }
    }


    /**
     * Construct app
     */
    public App() {

        if (index++ == 0) {
            createDB();
        }
        gson = new Gson();
    }

    /**
     * This is a quick method used to create the table tblData we need in the database.
     * It executes a CREATE TABLE... command to the database.
     */
    public void createDB() {

        Connection conn = null;

        // Use these to connect to the database and issue commands

        PreparedStatement stmt = null;

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
        System.out.println("Attempting to create tblData");
        String createStatement = "CREATE TABLE tblData (id INT(64) NOT NULL AUTO_INCREMENT, " +
                "title VARCHAR(200), comment VARCHAR(200), numLikes INT(64), uploadDate DATETIME, lastLikeDate DATETIME, PRIMARY KEY(id))";
        try {
            stmt = conn.prepareStatement(createStatement);
            stmt.execute();
            //stmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.out.println("Table not created (it may already exist)");
        }
    }

    /**
     * This is the main method. This created the main App object to be used during
     * execution, sets the static file location, a vital Spark Java process, and
     * contains a series of lambda functions the frontend will use to communicate,
     * including a get home, get data, post data, and post new data (updateLikes).
     * @param args  Standard Java main class argument.
     */
    public static void main( String[] args ) {
        App app = new App();
        // Set up static file service
        staticFileLocation("/web");

        // GET '/' returns the index page
        // (Leaving this alone, at least for now.)
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // GET '/data' returns a JSON string with all of the data in
        // the MySQL database.
        get("/data", (req, res) -> {
            // NB: moving getAllData out of this lambda facilitates testing
            String result = app.getAllData();
            // send a JSON object back, as a string. Tell the client that
            // everything is "OK" (status 200)
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
        post("/data/like/:id/upvote", (req, res) -> {
            // Call the update method above with the new datum object
            String result = "{\"res\":\"ok\"}";
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            int idx = Integer.parseInt(req.params("id"));
            updateLike(idx, d.numLikes, d.lastLikedDate, 1);
            return result;
        });

        // Route for RECORDING A DISLIKE. ":id" is used for getting index,
        // NEW DATUM IS IDENTICAL to like but decrements numlikes instead of incrementing it
        post("/data/like/:id/downvote", (req, res) -> {
            // Call the update method above with the new datum object
            String result = "{\"res\":\"ok\"}";
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            int idx = Integer.parseInt(req.params("id"));
            updateLike(idx, d.numLikes, d.lastLikedDate, -1);
            return result;
        });
    }
}