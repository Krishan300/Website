package backend.luna.lehigh.edu;

import static spark.Spark.*;
import com.google.gson.*;
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
    int numLikes;
    java.util.Date uploadDate;

    //java.util.Date lastLikeDate;
}


class Message {
    int user_id;
    int message_id;
    String title;
    String body;
    java.util.Date uploadDate;

}

class User{
    int user_id;
    String username;
    String realname;
    String email;
}

class Vote {
    int user_id;
    int message_id;
    boolean isLiked;
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

    // Only one gson instantiation, for efficiency.
    final Gson gson;


    // Environment variables.
    static Map<String, String> env = System.getenv();
    static String ip = env.get("POSTGRES_IP");
    static String url="localhost";
    static String port = env.get("POSTGRES_PORT");
    static String user = env.get("POSTGRES_USER");
    static String pass = env.get("POSTGRES_PASS");
    static String db = env.get("POSTGRES_DB");





    /**
     * No Longer needed because we know longer have single datatable
     * Get Data from our database and returns it in JSON format.
     * @return JSON object from SQL frontend

    String getAllData() {
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
            String getStmt = "SELECT * FROM tblMessage;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                Datum currentDatum = new Datum();
                currentDatum.index = rs.getInt("user_id");
                currentDatum.title = rs.getString("title");
                currentDatum.comment= rs.getString("comment_text");
                //currentDatum.numLikes = rs.getInt("numLikes");
               currentDatum.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
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
    } */



    /**
     * Get the a message from the Database and return it in JSON format
     * @return a JSON function from frontend
     */
    String getMessage(){
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
                System.out.println("Error: getConnection returned null object in getMessage");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception in getMessage");
            e.printStackTrace();
            return null;
        }

        String result = "";
        // watch multiples
        ArrayList<Message> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblMessage;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                Message currentmessage = new Message();
                currentmessage.user_id = rs.getInt("user_id");
                currentmessage.message_id = rs.getInt("message_id");
                currentmessage.title = rs.getString("title");
                currentmessage.body=rs.getString("body");
                currentmessage.uploadDate=sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.numLikes = rs.getInt("numLikes");
                //currentmessage.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
                results.add(currentmessage);
            }
            stmt.close();
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
        ArrayList<Vote> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblUpvote;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                Vote currentVote = new Vote();
                currentVote.user_id = rs.getInt("user_id");
                currentVote.message_id = rs.getInt("message_id");
                //currentDatum.numLikes = rs.getInt("numLikes");
                //currentmessage.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
                results.add(currentVote);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result = gson.toJson(results);
        return result;





    }

    String getDownvotes () {
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
        ArrayList<Vote> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblDownvote;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                Vote currentVote = new Vote();
                currentVote.user_id = rs.getInt("user_id");
                currentVote.message_id = rs.getInt("message_id");
                //currentDatum.numLikes = rs.getInt("numLikes");
                //currentmessage.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
                results.add(currentVote);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result = gson.toJson(results);
        return result;





    }
    /**
     * This insert takes a user from the frontend and adds it to the user database
     * @param u User on frontend added to databse

     */
     String insertUser(User u) {
         Connection conn=null;

         try {
             Class.forName("org.postgresql.Driver");
         }
         catch (ClassNotFoundException e) {
             System.err.println("Where is your PostgreSQL JDBC Driver? "
                     + "Include in your library path!");
             e.printStackTrace();
         }
         try {
             // Open a connection, fail if we cannot get one
             conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +
                     port + "/" + db + "?useSSL=false", user, pass);
             if (conn == null) {
                 System.out.println("Error: getConnection returned null object");
                 return null;
             }
         } catch (SQLException e) {
             System.out.println("Error: getConnection threw an exception in insertUser");
             e.printStackTrace();
             return null;
         }

         if(u!=null && u.user_id > -1 && u.username!=null && u.realname!=null && u.email!=null){
             try {
             String insertStmt= "INSERT INTO tbluser (user_id, username, realname, email)  VALUES (default, ?, ?, ?, ?, ?);";
             PreparedStatement stmt=conn.prepareStatement(insertStmt);
             stmt.setString(1, "\"user_id\"");
             stmt.setString(2, "\"username\"");
             stmt.setString(3, "\"realname\"");
             stmt.setString(4, "\"email\"");
             stmt.executeUpdate();
             stmt.close();
         }
        catch (SQLException e) {
             System.out.println("Error: insertion failed");
             e.printStackTrace();
         }
         return goodData;
     } else {
        return badData;
    }





}


    String getUser () {
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
        ArrayList<User> results = new ArrayList<>();
        try {
            // get all data into a ResultSet
            String getStmt = "SELECT * FROM tblUser;";
            System.out.println("Is it working?");
            PreparedStatement stmt = conn.prepareStatement(getStmt);

            ResultSet rs = stmt.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                // convert the RS to Data objects.
                User currentUser = new User();
                currentUser.user_id = rs.getInt("user_id");
                currentUser.username = rs.getString("username");
                currentUser.realname=rs.getString("realname");
                currentUser.email=rs.getString("email");
                //currentDatum.numLikes = rs.getInt("numLikes");
                //currentmessage.uploadDate = sqlDateToJavaDate(rs.getTimestamp("uploadDate"));
                //currentDatum.lastLikeDate = sqlDateToJavaDate(rs.getTimestamp("lastLikeDate"));
                results.add(currentUser);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: query failed");
            e.printStackTrace();
        }
        // Convert the array of results to a JSON string and return it
        result = gson.toJson(results);
        return result;
    }
      /*
      *Check to see if user_id is already in user table
      * Validation method

      */
       public boolean userExists(Message m){
           Connection conn = null;
           boolean userisValid=true;
           int isFound=0;
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
                   return false;
               }
           } catch (SQLException e) {
               System.out.println("Error: getConnection threw an exception in getVotes");
               e.printStackTrace();
               return false;

           }

       String checkUserTable="SELECT EXISTS(SELECT 1 FROM tblUser where user_id="+m.user_id;
           try {
        PreparedStatement stmt=conn.prepareStatement(checkUserTable);
        isFound=stmt.executeUpdate();
        stmt.close();

       }
        catch (SQLException e) {
        System.out.println("Error: value search failed");
        e.printStackTrace();
    }
         if(isFound==0){
               userisValid=false;
         }

         if(isFound==1) {
             userisValid=true;
         }

       return userisValid;
       }

        /**
         * This insert takes a message from the frontend and adds it to the message database
         * @param m  A message retrieved by and built by spark framework
         * @return command telling server if addition was succesful or not
         */

     String insertMessage(Message m) {
         //validate if message id is the same as id of already existing user

             Connection conn = null;

             try {
                 Class.forName("org.postgresql.Driver");
             } catch (ClassNotFoundException e) {
                 System.err.println("Where is your PostgreSQL JDBC Driver? "
                         + "Include in your library path!");
                 e.printStackTrace();
             }

             try {
                 // Open a connection, fail if we cannot get one
                 conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +
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
             if (m != null && m.message_id != 0 && m.user_id != 0 && m.title != null && m.body != null && userExists(m)) {
                 try {
                     //  (id, title, comment, numLikes, uploadDate, lastLikeDate)
                     String insertStmt = "INSERT INTO tblmessage VALUES (default, ?, ?, ?, ?, ?);";
                     PreparedStatement stmt = conn.prepareStatement(insertStmt);
                     stmt.setInt(1, m.message_id);
                     stmt.setInt(2, m.user_id);
                     stmt.setString(3, m.title);
                     stmt.setString(4, m.body);

                     stmt.setTimestamp(5, javaDateToSqlDate(m.uploadDate));


                     //stmt.setInt(3,d.numLikes);

                     //stmt.setTimestamp(5,javaDateToSqlDate(d.lastLikeDate));
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
     * This insert takes new data from the frontend sever and adds it into the datum database
     * @param   d   A Datum object retrieved by and built through Spark framework
     * @return command telling server if addition was a success or not

    String insertDatum(Datum d) {
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
        //System.out.println("Connecting to " + ip + ":" + port + "/" + db);
        try {
            // Open a connection, fail if we cannot get one
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" +
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
        if (d != null && d.title != null && d.comment != null && d.uploadDate != null) {
            try {
                //  (id, title, comment, numLikes, uploadDate, lastLikeDate)
                String insertStmt = "INSERT INTO tblmessage VALUES (default, ?, ?, ?, ?, ?);";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1,d.title);
                stmt.setString(2,d.comment);
                //stmt.setInt(3,d.numLikes);
                stmt.setTimestamp(4,javaDateToSqlDate(d.uploadDate));
                //stmt.setTimestamp(5,javaDateToSqlDate(d.lastLikeDate));
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




    }*/

    /**
     * Execute an UPDATE query to modify contents of both upvote tables. Done by passing a boolean isLiked to indicate
     * whether it's a LIKE or DISLIKE.
     * @param   D              The vote that is either a like or dislike.

     *
     */
    String updateLike(Vote D) {
        // get the MYSQL configuration from the environment
        Connection conn = null;
        String ret="badData";
        int newNumLikes = 0;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }

        // Check if it's like/dislike and change numLikes accordingly.
       /* if (isLiked) newNumLikes = ++numLikes;
        else newNumLikes = --numLikes;*/

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

        if (D != null && D.message_id > -1 && D.user_id > -1) {
            if (D.isLiked) {
                try {
                    String insertStmt = "INSERT INTO tblUpvote VALUES (default, ?, ?);";
                    PreparedStatement stmt = conn.prepareStatement(insertStmt);
                    stmt.setInt(1, D.message_id);
                    stmt.setInt(2, D.user_id);
                    stmt.executeUpdate();
                    stmt.close();

                } catch (SQLException e) {
                    System.out.println("Error: insertion failed");
                    e.printStackTrace();
                }
                ret=goodData;
            } else if (!D.isLiked) {
                try {
                    String insertStmt = "INSERT INTO tblDownvote VALUES (default, ?, ?);";
                    PreparedStatement stmt = conn.prepareStatement(insertStmt);
                    stmt.setInt(1, D.message_id);
                    stmt.setInt(2, D.user_id);
                    stmt.executeUpdate();
                    stmt.close();

                } catch (SQLException e) {
                    System.out.println("Error: insertion failed");
                    e.printStackTrace();
                }
                ret=goodData;

            } else {
                ret=badData;
            }


            //

        }
        return ret;
    }




    /**
     * Constructs an App object which creates a new Database and Gson Object to be used later by the different routes
     * This object is used to store the Database for each instance.
     */
    public App() {
        createDB();
        gson = new Gson();
    }

    public App(boolean shouldBeDropped) {
        if (shouldBeDropped) {
            dropDB();
        }
        createDB();
        gson = new Gson();
    }

    /**
     * This method runs a command that drops the database tblData. Made private to ensure "adversaries"
     * can't access it since it is highly dangerous in the wrong hands. Called within createDB() to ensure
     * that our Docker instance is cleared. Especially useful for tests.
     */
    private static void dropDB() {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }

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
            System.out.println("Error: getConnection in dropDB threw an exception");
            e.printStackTrace();
            return;
        }



        System.out.println("Got to dropDB() and about to create PS");
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("DROP TABLE IF EXISTS tblcomments cascade;");
            stmt.execute();
            stmt.close();

            PreparedStatement stmt2;
            stmt2 = conn.prepareStatement("DROP TABLE IF EXISTS tbluser cascade;");
            stmt2.execute();
            stmt2.close();

            PreparedStatement stmt3;
            stmt3 = conn.prepareStatement("DROP TABLE IF EXISTS tblmessage cascade;");
            stmt3.execute();
            stmt3.close();

            PreparedStatement stmt4;
            stmt4 = conn.prepareStatement("DROP TABLE IF EXISTS tblupvote cascade;");
            stmt4.execute();
            stmt4.close();

            PreparedStatement stmt5;
            stmt5 = conn.prepareStatement("DROP TABLE IF EXISTS tbldownvote cascade;");
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
        // Quickly make sure to drop table if exists. Used primarily for ease of testing.
        // NOTE: This only works at the beginning of this because we create a new App object each time
        // we need this connection. MAIN only uses one instance of App, so it won't accidentally delete.
        dropDB();

        Connection conn = null;

        // Connect to the database; fail if we can't
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }

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
            stmt = conn.prepareStatement(createUser);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("stmt.execute worked. table created");
            //conn.close();
        } catch (SQLException e) {
            // Should we handle this in a better way?
            System.out.println("Table not created (it may already exist)");
        }



        PreparedStatement stmt2;
        String createMessage="CREATE TABLE IF NOT EXISTS tblMessage (message_id SERIAL PRIMARY KEY, user_id INTEGER, title VARCHAR(50), body VARCHAR(140), uploadDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  FOREIGN KEY(user_id) REFERENCES tblUser (user_id));";
        try {
            stmt2 = conn.prepareStatement(createMessage);
            stmt2.executeUpdate();
            stmt2.close();
            System.out.println("stmt.execute worked. table created");
        } catch (SQLException e) {
            System.out.println ("Message table not created");
            e.printStackTrace();

        }


        PreparedStatement stmt3;
        String createComment="CREATE TABLE IF NOT EXISTS tblComments (" +
                "comment_id SERIAL PRIMARY KEY," +
                "user_id INTEGER," +
                "message_id INTEGER," +
                "comment_text VARCHAR(255)," +
                "uploadDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES tblUser (user_id)," +
                "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)" +
                ");";
        try {
            stmt3 = conn.prepareStatement(createComment);
            stmt3.executeUpdate();
            stmt3.close();
            System.out.println("stmt.execute worked. table created");
        } catch (SQLException e) {
            System.out.println ("Comment table not created");
            e.printStackTrace();



        }

        PreparedStatement stmt4;
        String createdownvote="CREATE TABLE IF NOT EXISTS tblDownVote ( user_id INTEGER, message_id INTEGER, FOREIGN KEY (user_id) REFERENCES tblUser (user_id),  FOREIGN KEY(message_id) REFERENCES tblMessage (message_id), PRIMARY KEY (user_id, message_id));";
        try {
            stmt4 = conn.prepareStatement(createdownvote);
            stmt4.executeUpdate();
            stmt4.close();
            System.out.println("stmt.execute worked. table created");
        } catch (SQLException e) {
            System.out.println ("Downvote not created");
            e.printStackTrace();




        }

        PreparedStatement stmt6;
        String createupvote="CREATE TABLE IF NOT EXISTS tblUpVote (user_id INTEGER, message_ID INTEGER, FOREIGN KEY (user_id) REFERENCES tblUser(user_id), FOREIGN KEY(message_id) REFERENCES tblMessage (message_id), PRIMARY KEY (user_id, message_id));";

        try {
            stmt6 = conn.prepareStatement(createupvote);
            stmt6.executeUpdate();
            stmt6.close();
            System.out.println("Upvote.execute worked. table created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println ("Upvote not created");



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

    /**
     * Main method which holds all get and post routes for updating and sending the database to the server.
     * Each route is formed as a lambda function which returns a GSON object to be passed.
     * @param args  Standard Java main class argument.
     */
    public static void main( String[] args ) {

        App app = new App();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }
        // Set up static file service WHAT IS THE HIERARCHY HERE
        staticFileLocation(sFileLocation);

        // GET '/' returns the index page
        // (Leaving this alone, at least for now.)
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // Post a new user into the user database
        post("/user", (req, res) -> {
            User u=app.gson.fromJson(req.body(), User.class);
            String result=app.insertUser(u);
            res.status(200);
            res.type("application/json");
            return result;

        });


        // Get existing user from database
        get("/getuser", (req, res) -> {
            User u=app.gson.fromJson(req.body(), User.class);
            String result=app.getUser();
            res.status(200);
            res.type("application/json");
            return result;


                });

        // GET '/data' returns a JSON string with all of the data in
        // the MySQL database.
        get("/message", (req, res) -> {
            String result = app.getMessage();
            // send a JSON object back
            res.status(200);
            res.type("application/json");
            return result;
        });

       /* // POST a new item into the messsage database-make a new message
        post("/data", (req, res) -> {
            // Try to create a Datum from the request object
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            String result = app.insertDatum(d);
            res.status(200);
            res.type("application/json");
            return result;
        });*/

        post("/data", (req, res) -> {
            Message m = app.gson.fromJson(req.body(), Message.class);
            String result = app.insertMessage(m);
            res.status(200);
            res.type("application/json");
            return result;


        });



        post("/vote ", (req, res) -> {
            Vote v = app.gson.fromJson(req.body(), Vote.class);
            String result = app.updateLike(v);
            res.status(200);
            res.type("application/json");
            return result;

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
            Datum d = app.gson.fromJson(req.body(), Datum.class);
            int idx = Integer.parseInt(req.params("id"));
            app.updateLike(idx, d.numLikes, d.lastLikeDate, false);
            return goodData;
        });


    }
} */