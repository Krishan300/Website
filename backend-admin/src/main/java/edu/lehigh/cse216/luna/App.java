package edu.lehigh.cse216.luna;

import java.net.URISyntaxException;
import java.sql.*;
import com.sendgrid.*;
import java.io.IOException;

/**
 * Hello world!
 *
 */


public class App 
{
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }

    /**
     *
     * @param rs specific user from possible users list
     * @param conn connection to db
     */
    static void checkUser(ResultSet rs, Connection conn) {
        String UN = null;
        String eMail = null;
        Boolean qualified = true;
        try {
            UN = rs.getString("userName");
            eMail = rs.getString("eMail");
        } catch (SQLException e) {
            System.out.println("Error getting data from pUserData");
            e.printStackTrace();
            return;
        }
        try {
            String getStmt = "SELECT * FROM userData WHERE userName = ?";
            PreparedStatement stmt = conn.prepareStatement(getStmt);
            stmt.setString(1, UN);
            ResultSet rSet = stmt.executeQuery();
            if (rSet.next()){
                qualified = false;
                Email from = new Email("admin@buzz.com");
                String subject = "There was an issue setting up your account!";
                Email to = new Email(eMail);
                Content content = new Content("text/plain", "Unfortunately, the username you have chosen has already been taken.\n" +
                            "Please choose another username and try again!\n\n" +
                            "Sincerely,\nThe Buzz Staff");
                Mail mail = new Mail(from, subject, to, content);
                SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
                Request request = new Request();
                try {
                    request.method = Method.POST;
                    request.endpoint = "mail/send";
                    request.body = mail.build();
                    Response response = sg.api(request);
                    System.out.println(response.statusCode);
                } catch (IOException e) {
                    System.out.println("Error: could not send email to recipient");
                    e.printStackTrace();
                    return;
                }
                return;
            } else {
                getStmt = "SELECT * FROM userData WHERE eMail = ?";
                stmt = conn.prepareStatement(getStmt);
                stmt.setString(1, eMail);
                rSet = stmt.executeQuery();
                if (rSet.next()) {
                    qualified = false;
                    Email from = new Email("admin@buzz.com");
                    String subject = "There was an issue setting up your account!";
                    Email to = new Email(eMail);
                    Content content = new Content("text/plain", "It appears you already have an account. Your email address is already being used " +
                            "by the user called " + rs.getString("userName") + ". If this is not your account, please get in contact with use ASAP " +
                            "so we can sort out the problem. \n\n Sincerely, \n The Buzz Staff");
                    Mail mail = new Mail(from, subject, to, content);
                    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
                    Request request = new Request();
                    try {
                        request.method = Method.POST;
                        request.endpoint = "mail/send";
                        request.body = mail.build();
                        Response response = sg.api(request);
                        System.out.println(response.statusCode);
                    } catch (IOException e) {
                        System.out.println("Error: could not send email to user");
                        e.printStackTrace();
                        return;
                    }
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL exception while checking users");
            e.printStackTrace();
            return;
        }
        try {
            if (qualified) {
                String insertStmt = "INSERT INTO userData VALUES (default, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertStmt);
                stmt.setString(1, rs.getString("userName"));
                stmt.setString(2, null);
                stmt.setString(3, null);
                stmt.setString(4, rs.getString("eMail"));
                stmt.execute();
                insertStmt = "INSERT INTO pwHash VALUSE (default, ?, ?)";
                stmt = conn.prepareStatement(insertStmt);
                stmt.setBytes(1, rs.getBytes("salt"));
                stmt.setString(2, rs.getString("password"));
                stmt.execute();
            }
            String deleteStmt = "DELETE * FROM pUserData WHERE userName = ? AND eMail = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStmt);
            stmt.setString(1, rs.getString("userName"));
            stmt.setString(2, rs.getString("eMail"));
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error creating new user.");
            e.printStackTrace();
            return;
        }
        return;
    }

    public static void main( String[] args )
    {
        Connection conn = null;

        try {
            // Open a connection, fail if we cannot get one
            conn = getConnection();
            if (conn == null) {
                System.out.println("Error: getConnection returned null object");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error: getConnection threw an exception");
            e.printStackTrace();
            return;
        } catch (URISyntaxException e) {
            System.out.println("Error: getConnection threw a URI Syntax exception");
            e.printStackTrace();
            return;
        }
        if (args.length == 0) {
            System.out.println("Run this program with the command \"help\" for details.");
            return;
        }
        if (args[0].equals("help")){
            System.out.println("createTable tblName name1 type1 name# type# - create a new database table w/ # titled columns \n" +
                    "WARNING: The first column name will automatically be an integer named id, and this column will be the primary key.\n" +
                    "deleteTable tblName - drop database table \n" +
                    "readUsers - read through possible users, display them, and send emails based on availability.\n" +
                    "deleteUser UN - deletes user data and all comments by user UN");
            return;
        }
        if (args[0].equals("createTable")) {

            if(args.length % 2 != 0) {
                System.out.println("Improper usage of commands. Check the help command for more details.");
                return;
            } else {
                String createStatement = "CREATE TABLE IF NOT EXISTS " + args[1] + " (";
                createStatement = createStatement + "id INT(64) NOT NULL AUTO_INCREMENT, ";
                for(int i = 2; i < args.length; i += 2){
                    createStatement = createStatement + args[i] + " ";
                    if (args[i+1] == "int") {
                        createStatement = createStatement + "INT(64), ";
                    }
                    if ((args[i+1] == "char") || args[i+1]== "string"){
                        createStatement = createStatement + "VARCHAR(200), ";
                    }
                    if (args[i+1] == "date") {
                        createStatement = createStatement + "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
                    }
                    if (args[i+1] == "boolean") {
                        createStatement = createStatement + "BOOLEAN, ";
                    }
                    if (args[i+1] == "bytes") {
                        createStatement = createStatement + "VARBINARY(64), ";
                    }
                }
                createStatement = createStatement + "PRIMARY KEY(id))";
                try {
                    PreparedStatement stmt = conn.prepareStatement(createStatement);
                } catch (SQLException e){
                    System.out.println("Error: could not create table, sql error");
                    e.printStackTrace();
                    return;
                }
            }
        }
        if (args[0].equals("deleteTable")) {
            if(args.length < 2) {
                System.out.println("Insufficient arguments.");
            }
            String tblName = args[1];


            try {
                String deleteStmt = "DROP TABLE IF EXISTS ?";
                PreparedStatement stmt = conn.prepareStatement(deleteStmt);
                stmt.setString(1, tblName);
                stmt.execute();
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred.");
                e.printStackTrace();
                return;
            }
            System.out.println("Table " + args[1] + " was either deleted, or did not exist. Either way, it doesn't exist now.");
            return;
        }
        if (args[0].equals("readUsers")) {
            String getStmt = "SELECT * FROM pUserData";
            try {
                PreparedStatement stmt = conn.prepareStatement(getStmt);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    checkUser(rs, conn);
                }
            } catch (SQLException e) {
                System.out.println("Error: could not read users: SQL exception");
                e.printStackTrace();
                return;
            }
        }
        if (args[0].equals("deleteUser")) {
            if (args.length < 2) {
                System.out.println("Insufficient arguments");
                return;
            } else if (args.length > 2) {
                System.out.println("Too many arguments");
                return;
            }

            try{
                String deleteStmt = "DELETE * FROM userData WHERE userName = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteStmt);
                stmt.setString(1, args[1]);
                stmt.execute();
            } catch (SQLException e) {
                System.out.println("Error: could not delete user properly");
                e.printStackTrace();
                return;
            }
            try{
                String deleteStmt = "DELETE * FROM tblData WHERE author = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteStmt);
                stmt.setString(1, args[1]);
                stmt.execute();
            } catch (SQLException e){
                System.out.println("Error: could not delete user's comments");
                e.printStackTrace();
                return;
            }
            System.out.println("User " + args[1] + " was either deleted successfully, or did not exist, either way, they're gone now.");
            return;
        }
        System.out.println("Sorry, I can\'t understand. Run this program with help as an argument for directions.");
        return;
    }
}
