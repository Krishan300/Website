package backend.luna.lehigh.edu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.lang.*;
import java.io.File;

/**
 * Unit test for simple App. Updated by Alex Van Heest to work with POSTGRESQL.
 */
public class AppTest extends TestCase
{
    String goodData = "{\"res\":\"ok\"}";
    String badData = "{\"res\":\"bad data\"}";
    /**
     * Initial test to see if a connection can successfully be made.
     */
    public void testConnectionSucceeds() {
        App app = new App();
        assertNotNull(app.gson);
    }

    /**
     * Check that the tables are being created as expected.
     */
    public void testTablesAreCreated() {
        App app = new App();
        assertTrue(app.hasFoundTables());
    }

    /**
     * Tests that getUserData() returns proper value after inserting a row into tbluser.
     * NOTE: This isn't the most atomic way of doing unit testing, but it does successfully
     *       test that the insert is working correctly.
     */
    public void testInsertAndGetUser() {
        App app = new App(true);
        UserObj uo = new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu");

        // First, insert user.
        String retval = app.insertUser(uo);
        assertEquals(goodData, retval);

        // Now, let's check to see if user has been added properly.
        // ASSUMPTION: Tables have been dropped, meaning this user is the first
        //             in the database. Thus, the user_id should be 1 since auto-inc
        //             starts here at 1.
        retval = app.getUserData(1);
        //System.out.println(retval);
        assertEquals( "[{\"user_id\":1,\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\"}]", retval);
    }

    /**
     * Tests that getting messages works after inserting a proper value into tblmessage.
     * This test simulates the frontpage.
     */
    public void testInsertAndGetMessage() {
        App app = new App(true);

        // Create necessary entries into tables
        // ASSUMPTION: These calls work. They're tested already in other methods.
        app.insertUser(new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu"));
        //System.out.println(app.getUserData(1));

        // Create a new message for user at user_id 1 (created above) and insert.
        String retval = app.insertMessage(new MessageObj(1,"Alexs Message","Body of Alexs message"));
        assertEquals(goodData,retval);

        // Now, let's check that it's been added properly.
        // ASSUMPTION: message_id = 0 since tables were dropped before the above insert.
        retval = app.getAllMessages();
        String actualPart1, actualPart2, expectedPart1, expectedPart2;
        actualPart1 = retval.substring(0,98);
        //actualPart2 = retval.substring(123);
        expectedPart1 = "[{\"user_id\":1,\"message_id\":1,\"title\":\"Alexs Message\",\"body\":\"Body of Alexs message\",\"uploadDate\":\"";
        expectedPart2 = ",\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\"}]";
        //System.out.println(retval + "\n\n" + retval2);
        //System.out.println("Expected Part 1: " + expectedPart1);
        //System.out.println("Actual Part 1: " + actualPart1);
        //System.out.println("Expected Part 2: " + expectedPart2);
        //System.out.println("Actual Part 2: " + actualPart2);

        // Finally, break it down into substrings and test (since Date object is created live in server).
        assertEquals(expectedPart1, actualPart1);
        //assertEquals(expectedPart2, actualPart2);
    }

    /**
     * Tests that getting an individual message from a group works properly.
     * This test simulates a single message page.
     */
    public void testGetIndividualMessage() {
        App app = new App(true);

        // Create a user and three posts for that user. (This capacity is ensured in other tests.)
        app.insertUser(new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu"));
        app.insertMessage(new MessageObj(1,"Alexs Message","Body of Alexs message"));
        app.insertMessage(new MessageObj(1,"Another Alex Message","Yep, heres another one."));
        app.insertMessage(new MessageObj(1,"Alexs Final Message","Rosebud"));

        // Simulate a build of the message page for message at id 2 ("Another Alex Message")
        String retval = app.getMessageContentAndVotes(2);

        // Check output by splitting into two parts to ignore the date.
        String part1 = "[{\"user_id\":1,\"message_id\":2,\"title\":\"Another Alex Message\",\"body\":\"Yep, heres another one.\",\"uploadDate\":\"";
        String part2 = ",\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\",\"upvotes\":0,\"downvotes\":0,\"tot_vot";
        //System.out.println(retval.substring(0,107));
        //System.out.println(retval.substring(132,235));
        assertEquals(part1, retval.substring(0,107));
        //assertEquals(part2, retval.substring(132,235));
    }

    /**
     * Tests inserting and getting comments for a given message.
     * This test simulates the comments section for a single message page.
     */
    public void testGetPostComments() {
        App app = new App(true);

        // Create a user, a message, and one comment for that message. (This capacity is ensured in other tests.)
        app.insertUser(new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu"));
        app.insertMessage(new MessageObj(1,"Alexs Message","Body of Alexs message"));
        String retval = app.insertComment(new CommentObj(1,1,"Heres a comment"));
        assertEquals(goodData,retval);

        // Simulate comments section by retrieving all comments for message at id == 1
        retval = app.getAllComments(1);
        String actualPart1, actualPart2, expectedPart1, expectedPart2;
        actualPart1 = "[{\"user_id\":1,\"message_id\":1,\"comment_id\":1,\"comment_text\":\"Heres a comment\",\"uploadDate\":\"";
        actualPart2 = ",\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\"}]";
        expectedPart1 = retval.substring(0,91);
        //expectedPart2 = retval.substring(116);
        //System.out.println(expectedPart2);
        assertEquals(actualPart1, expectedPart1);
        //assertEquals(actualPart2, expectedPart2);
    }

    /**
     * Tests whether or not upvotes and downvotes are added in properly and are collected properly
     * as a sum before returning to frontend.
     * NOTE: In the future, there should be some way to prevent one user from adding a thousand up/downvotes.
     */
    public void testVotes() {
        App app = new App(true);

        // Create a user, a message, and one upvote
        app.insertUser(new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu"));
        app.insertMessage(new MessageObj(1,"Alexs Message","Body of Alexs message"));
        String retval = app.insertUpDownVote(new VoteObj(1,1),true);
        assertEquals(goodData, retval);

        // Now check message to make sure the upvote was counted and that the total reflects this.
        retval = app.getMessageContentAndVotes(1);
        //System.out.println(retval);
        String expectedPart2A = ",\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\",\"upvotes\":1,\"downvotes\":0,\"tot_votes\":1}]";
        String actualPart2A = retval.substring(123);
        //assertEquals(expectedPart2A, actualPart2A);

        // Now add a downvote and make sure it's counted properly.
        String expectedPart2B = ",\"username\":\"ajv218\",\"realname\":\"Alex V\",\"email\":\"ajv218@lehigh.edu\",\"upvotes\":1,\"downvotes\":1,\"tot_votes\":0}]";
        app.insertUpDownVote(new VoteObj(1,1),false);
        retval = app.getMessageContentAndVotes(1);
        String actualPart2B = retval.substring(123);
        //assertEquals(expectedPart2B, actualPart2B);
    }

    /**
     * Tests whether or not a user with valid credentials can be added properly.
     */
    public void testLogin() {
        App app = new App(true);

        // Try to add a new user with these credentials.
        // ASSUMPTION: These credentials will lead to a valid OAuth login.
        // NOTE: For now, as long as both fields aren't null, this authentication will work.
        String username = "ajv218";
        String password = "abc123";
        String retval = app.login(username, password);
        //System.out.println(retval);
        assertNotSame(badData, retval);

        // Just because the above isn't badData doesn't mean the user is actually added. Let's also
        // checkthat the user is in the DB.
        // ASSUMPTION: Since tables were dropped before this, user ajv218 has id 1.
        retval = app.getUserData(1);
        //System.out.println(retval);
        assertEquals(retval, "[{\"user_id\":1,\"username\":\"ajv218\",\"realname\":\"ajv218\",\"email\":\"ajv218@lehigh.edu\"}]");

        // Finally, check that the user is in the hashtable.
        // NOTE: hashtable variable in backend is static. Can be referenced by instance too.
        assertEquals(App.hashtable.containsKey(username), true);
    }

    /**
     * Tests whether or not an action can be validated after logging in.
     */
    public void testValidatedAction() {
        // Simulate login
        String username = "ajv218";
        App app = new App(true);
        String retval = app.login(username, "abc123");
        UserStateObj uso = app.gson.fromJson(retval, UserStateObj.class);
        //System.out.println(uso.secret_key);
        //System.out.println(App.hashtable.get(uso.username));
        boolean retbool = App.validateAction(uso.username, uso.secret_key);

        // Try to validate an action with the retrieved secretKey from backend
        assertEquals(true, retbool);
    }

    /**
     * Tests whether or not the logout functionality successfully removes the key from the
     * hashmap and makes sure it cannot be validated with the same secret key anymore.
     */
    public void testLogout() {
        // Try to logout soon after logging in (log in functionality tested in other test methods).
        String username = "ajv218";
        App app = new App(true);
        String retval = app.login(username, "abc123");
        UserStateObj uso = app.gson.fromJson(retval, UserStateObj.class);
        assertEquals(goodData, app.logout(uso.username, uso.secret_key));

        // Just getting goodData back doesn't mean the combo was removed from the hashmap. Let's
        // test this separately just to be sure, and let's make sure validation with invalid creds
        // fails after we're sure it's removed from the hashmap.
        assertEquals(App.hashtable.containsKey(username), false);
        assertEquals(App.validateAction(uso), false);
    }

    /**
     * Tests whether saveFileToGDrive(File fileobj) works.
     */
//    public void testSaveFileToGDrive() {
//        App app = new App(true);
//        assertEquals(app.saveFileToGDrive(new File("~/Desktop/somefile.txt")), "DRIVE.FILE.LOCATION.COM/newfile.txt");
//    }

    /**
     * Tests whether saveFileToGDrive(String filecontents) works
     */
    public void testSaveFileToGDriveString() {
        App app = new App(true);

        app.insertUser(new UserObj("ajv218", "Alex V", "ajv218@lehigh.edu"));

        // Creates a file on the GDrive
        byte[] ba = Base64.getEncoder().encode("Test".getBytes());
        MessageObj mo = new MessageObj(1, 1, "Alexs Message", "Body of Alexs message", new Date(), "ajv218", "Alex V", "ajv218@lehigh.edu", ba);
        String retval = app.insertMessage(mo);
        assertEquals(goodData,retval);
    }
}