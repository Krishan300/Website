package edu.lehigh.cse216.luna;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String val1;
            if (args[0].equals("help")){
                System.out.println("createTable tblName # name1 name2 name# - create a new database table w/ # columns and each title \n" +
                "deleteTable tblName - drop database table\n" +
                "readUsers - read through possible users table and display them");
            }
    }
}
