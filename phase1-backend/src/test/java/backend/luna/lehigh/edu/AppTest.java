package backend.luna.lehigh.edu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.lang.*;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * PHASE 1 TESTS: Written by Kieran, the following tests are aimed at determining
     * if the backend is sufficient for the set requirements:
     *
     * - testConstructor() -- to be moved to Admin App tests
     * - testGetAllData()
     * - testInsertDatum()
     * - testUpdateLike()
     * - testCreateDB() -- to be moved to Admin App tests
     *
     * This is left alone other than declaring the JSON responses as class variables.
     */

    String goodResponse = "{\"res\":\"ok\"}";
    String badResponse = "{\"res\":\"bad data\"}";

    /**
     *tests constructor that gson is initialized and index is incremented
     */
    public void testConstructor()
    {
        App app = new App();
        assertNotNull(app.gson);
    }


    /**
     *tests the getAllData Method for tables with 0, 1, or multiples datums
     */
    public void testGetAllData()
    {
        App app = new App();
        String output = app.getAllData();
        //not sure what output will look like because backend isnt done
        assertEquals(output, "[]");

        Datum d = new Datum();
        d.index = 1;
        d.title = "test";
        d.comment = "test comment";
        d.numLikes = 0;


        d.uploadDate = new Date(117, 1, 16, 20, 10);
        d.lastLikeDate = new Date(117, 1, 16, 20, 10);

        app.insertDatum(d);
        output = app.getAllData();
        assertEquals(output, "[{\"index\":1,\"title\":\"test\",\"comment\":\"test comment\",\"numLikes\":0,\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\",\"lastLikeDate\":\"Feb 16, 2017 8:10:00 PM\"}]");

        Datum d2 = new Datum();
        d2.index = 2;
        d2.title = "test2";
        d2.comment = "test comment2";
        d2.numLikes = 0;
        d2.uploadDate = new Date(117, 1, 16, 20, 10);
        d2.lastLikeDate = new Date(117, 1, 16, 20, 10);
        app.insertDatum(d2);

        output = app.getAllData();

        assertEquals(output, "[{\"index\":1,\"title\":\"test\",\"comment\":\"test comment\",\"numLikes\":0,\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\",\"lastLikeDate\":\"Feb 16, 2017 8:10:00 PM\"},{\"index\":2,\"title\":\"test2\",\"comment\":\"test comment2\",\"numLikes\":0,\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\",\"lastLikeDate\":\"Feb 16, 2017 8:10:00 PM\"}]");
    }

    /**
     *tests the insertDatum Method on both a null datum and one in good form
     */
    public void testInsertDatum()
    {
        App app = new App();

        Datum d = new Datum();
        d.index = 1;
        d.title = null;
        d.comment = "test comment";
        d.numLikes = 0;
        //bad date because title and dates are null
        assertEquals(app.insertDatum(d), badResponse);

        d.title = "test title";
        d.uploadDate = new Date(117, 1, 16, 20, 10);
        d.lastLikeDate = new Date(117, 1, 16, 20, 10);

        //works because everything now has initial value
        assertEquals(app.insertDatum(d), goodResponse);
    }


    /**
     *tests the updateLike Method at initial time, and after a like
     * and a dislike
     */
    public void testUpdateLike()
    {
        App app = new App();
        Datum d = new Datum();
        d.index = 1;
        d.title = "test";
        d.comment = "test comment";
        d.numLikes = 0;
        d.uploadDate = new Date(117, 1, 16, 20, 10);
        d.lastLikeDate = new Date(117, 1, 16, 20, 10);

        assertTrue(d.numLikes == 0);

        String output = null;
        try
        {
            app.insertDatum(d);
            app.updateLike(1, d.numLikes, d.lastLikeDate, true);
            output = app.getAllData();
        }
        catch(Exception e)
        {
            fail("error: updatelike could not increment likes");
            e.printStackTrace();
        }
        assertEquals(output, "[{\"index\":1,\"title\":\"test\",\"comment\":\"test comment\",\"numLikes\":1,\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\",\"lastLikeDate\":\"Feb 16, 2017 8:10:00 PM\"}]");
        // EXPECTED {"index":1,"title":"test","comment":"test comment","numLikes":1,"uploadDate":"Feb 16, 2017 8:10:00 PM","lastLikeDate":"Feb 16, 2017 8:10:00 PM"}
        // HAD

        try {
            app.updateLike(1, 1, d.lastLikeDate, false);
            output = app.getAllData();
        }
        catch(Exception e)
        {
            fail("error: update like could not decrement likes");
            e.printStackTrace();
        }
        assertEquals(output, "[{\"index\":1,\"title\":\"test\",\"comment\":\"test comment\",\"numLikes\":0,\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\",\"lastLikeDate\":\"Feb 16, 2017 8:10:00 PM\"}]");
    }

    /**
     *tests the createDB Method by initializing an app which calls
     * createDB() and then increments index from 0
     */
    public void testCreateDB()
    {
        App app = new App();
        assertNotNull(app.gson);
    }



    /**
     * PHASE 2 TESTS: Written by Alex, the following tests adapt phase 1 to the phase 2
     * requirements, which uses POSTGRES, adds multiple tables, adds new features, and more.
     *
     * - testLoginSuccess()             Should return some "success" as JSON.
     * - testLoginWrongUsername()       Should return an authentication error.
     * - testLoginWrongPassword()       Should return an authentication error.
     * - testLoginNoUser()              Should return a missing value error.
     * - testLoginNoPass()              Should return a missing value error.
     * - testRegisterSuccess()          Should return some "success" as JSON.
     * - testRegisterExistingUser()     Should return some existing username error.
     * - testRegisterExistingEmail()    Should return some existing username error.
     * - testRegisterInvalidUser()      Should return a warning about the username not meeting specs.
     * - testRegisterInvalidEmail()     Should return a warning about the username not meeting email specs.
     * - testRegisterInvalidPass()      Should return a warning about the password not meeting specs.
     * - testRegisterNoUser()           Should return a missing value error.
     * - testRegisterNoEmail()          Should return a missing value error.
     * - testRegisterNoPass()           Should return a missing value error.
     * - testCommentSuccess()           Should return some "success" as JSON.
     * - testCommentNoContent()         Should return a missing value error.
     * - testCommentInvalidPostId()     Should return some post-doesn't-exist error (as JSON).
     * - testCommentInvalidContent()    Should return a warning about the content not meeting specs.
     * - testPostSuccess()              Should return some "success" as JSON.
     * - testPostNoContent()            Should return a missing value error.
     * - testPostNoTitle()              Should return a missing value error.
     * - testChangePasswordSuccess()    Should return some "success" as JSON -- no way to test w/ email.
     * - testChangePasswordInvalidUser()Should return an authentication error.
     * - testChangePasswordInvalidPass()Should return an authentication error.
     * - testChangePasswordNoUser()     Should return a missing value error.
     * - testChangePasswordNoPass()     Should return a missing value error.
     * - testUpVoteSuccess()            Should return some "success" as JSON.
     * - testUpVoteInvalidPostId()      Should return some post-doesn't-exist error as JSON.
     * - testDownVoteSuccess()          Should return some "success" as JSON.
     * - testDownVoteInvalidPostId()    Should return some post-doesn't-exist error as JSON.
     * - testUpVoteDownVoteSuccess()    Should return a neutralized value for total votes.
     * - testDownVoteUpVoteSuccess()    Should return a neutralized value for total votes.
     *
     * To adapt this to Robert's finished code, not much needs to be done. Just the above adjustments and a
     * careful analysis of my clearly outlined assumptions should make adapting this, while odiously monotonous,
     * pretty simple.
     *
     * Also note that due to the complete lack of an admin app at this time, I haven't provided any
     * JUnit tests for it yet. Some are already completed and are featured below from Kieran's work -- others
     * will still need to be written pending details of Robert's implementation.
     *
     * The methods that won't be tested explicitly (they're called by other methods, not in routes):
     * getConnection()
     * getSalt()
     * getSecurePassword()
     * hashPreviousPass()
     * hashPass()
     * getSavedSalt()
     * getUserId()
     * comparePw()
     * makeToken()
     *
     * TODO: Add userpage tests using getUserData() method.
     * TODO: Adjust names for login, post, changePassword.
     */


    public void testLoginSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for login.
        App app = new App();

        // Sample user account to check against the database. Requires some registration beforehand -- do this
        // before testing (this is to make sure each test is atomic -- tests for login shouldn't rely on registration
        // stuff).
        String existingUsername = "user1";
        String existingPassword = "pass1";

        // Attempt to login. These credentials are correct and should receive a success.
        // ASSUMPTION: App.login returns a JSON string with either success or fail, as seen in other methods.
        String output = app.login(existingUsername, existingPassword);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, goodResponse);
    }

    public void testLoginWrongUsername() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for login.
        App app = new App();

        // Sample user account to check against the database.
        String wrongUsername = "WRONG USERNAME!!";
        String existingPassword = "pass1";

        // Attempt to login. These credentials are invalid and should receive an error.
        // ASSUMPTION: App.login returns a JSON string with either success or fail, as seen in other methods.
        String output = app.login(wrongUsername, existingPassword);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testLoginWrongPassword() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for login.
        App app = new App();

        // Sample user account to check against the database.
        String existingUsername = "user1";
        String wrongPassword = "WRONG PASSWORD!!";

        // Attempt to login. These credentials are invalid and should receive an error.
        // ASSUMPTION: App.login returns a JSON string with either success or fail, as seen in other methods.
        String output = app.login(existingUsername, wrongPassword);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testLoginNoUser() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for login.
        App app = new App();

        // Sample user account to check against the database.
        String emptyUsername = null;
        String existingPassword = "pass1";

        // Attempt to login. These credentials are invalid and should receive an error.
        // ASSUMPTION: App.login returns a JSON string with either success or fail, as seen in other methods.
        String output = app.login(emptyUsername, existingPassword);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testLoginNoPass() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for login.
        App app = new App();

        // Sample user account to check against the database.
        String existingUsername = "user1";
        String emptyPassword = null;

        // Attempt to login. These credentials are invalid and should receive an error.
        // ASSUMPTION: App.login returns a JSON string with either success or fail, as seen in other methods.
        String output = app.login(existingUsername, emptyPassword);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterSuccess() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This account isn't already registered.
        sd.userName = "testsuccessname";
        String password = "testsuccesspass";
        sd.eMail = "test@success.com";

        // Attempt to register.
        // ASSUMPTION: These credentials meet username, password, and email criteria.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, goodResponse);
    }

    public void testRegisterExistingUser() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This username is already registered.
        sd.userName = "user1";
        String password = "testsuccesspass";
        sd.eMail = "test2@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterExistingEmail() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This email is already registered.
        sd.userName = "testsuccessname2";
        String password = "testsuccesspass";
        sd.eMail = "test@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterInvalidUser() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This username doesn't meet specs and is unable to be registered.
        sd.userName = "(*#$(@#$&*bla bla bla you get the point";
        String password = "testsuccesspass";
        sd.eMail = "test@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterInvalidEmail() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This email doesn't meet specs and is unable to be registered.
        sd.userName = "testsuccessname2";
        String password = "testsuccesspass";
        sd.eMail = "no at sign in sight or period";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterInvalidPass() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This password doesn't meet specs and is unable to be registered.
        sd.userName = "testsuccessname2";
        String password = " ";                  // maybe too short?
        sd.eMail = "test@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterNoUser() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This username is null, which obviously shouldn't be allowed.
        sd.userName = null;
        String password = "testsuccesspassword";
        sd.eMail = "test@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterNoEmail() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This email is null, which obviously shouldn't be allowed.
        sd.userName = "testsuccessname2";
        String password = "testsuccesspassword";
        sd.eMail = null;

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testRegisterNoPass() {
        // Create signDatum object:
        signDatum sd = new signDatum();

        // Sample possible new user account.
        // ASSUMPTION: This password is null, which obviously shouldn't be allowed.
        sd.userName = "testsuccessname2";
        String password = null;
        sd.eMail = "test@success.com";

        // Attempt to register.
        String output = App.signUpUser(sd);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testCommentSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for comment.
        App app = new App();

        // Sample new comment on post with id 0 and valid body.
        // ASSUMPTION: Post with id 0 exists, validTitle is a valid value.
        int validPostId = 0;
        String validBody = "Guess what? We've successfully implemented comments. Pretty neat, huh?";

        // Attempt to post the comment.
        String output = app.comment(validPostId, validBody);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, goodResponse);
    }

    public void testCommentNoContent() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for comment.
        App app = new App();

        // Sample new comment on post with id 0 and no body.
        // ASSUMPTION: Post with id 0 exists.
        int validPostId = 0;
        String emptyBody = null;

        // Attempt to post the comment.
        String output = app.comment(validPostId, emptyBody);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testCommentInvalidContent() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for comment.
        App app = new App();

        // Sample new comment on post with id 0 and invalid content.
        // ASSUMPTION: Post with id 0 exists, invalidBody content is truly invalid.
        int validPostId = 0;
        String invalidBody = "You can't post all these symbols... can you? &Q#)$&!*$_!#*_!#$_)~~~~````:\"A>AW\"D";

        // Attempt to post the comment.
        String output = app.comment(validPostId, invalidBody);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testCommentInvalidPostId() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for comment.
        App app = new App();

        // Sample new comment on post with invalid id -1.
        // ASSUMPTION: Body content is as totally valid as it claims.
        int invalidPostId = -1;
        String validBody = "Totally valid comment body!";

        // Attempt to post the comment.
        String output = app.comment(invalidPostId, validBody);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testPostSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for post.
        App app = new App();

        // Sample post with valid title and body.
        // ASSUMPTION: Body content and title are as totally valid as they claim to be.
        String validTitle = "Valid Title!";
        String validBody = "Totally valid comment body!";

        // Attempt to make the new post.
        String output = app.post(validTitle, validBody);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, goodResponse);
    }

    public void testPostNoContent() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for post.
        App app = new App();

        // Sample post with valid title but invalid body.
        // ASSUMPTION: Body is invalid.
        String validTitle = "Valid Title!";
        String emptyBody = null;

        // Attempt to make the new post.
        String output = app.post(validTitle, emptyBody);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, badResponse);
    }

    public void testPostNoTitle() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for post.
        App app = new App();

        // Sample post with invalid title but valid body.
        // ASSUMPTION: Title is invalid.
        String emptyTitle = null;
        String validBody = "Totally valid comment body!";

        // Attempt to make the new post.
        String output = app.post(emptyTitle, validBody);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, badResponse);
    }

    public void testChangePasswordSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for change password.
        App app = new App();

        // Attempt to change password by passing in existing username and password.
        // ASSUMPTION: User password combo exists already.
        String changeUser = "user1";
        String changePass = "pass1";

        // Attempt to change password.
        String output = app.changePassword(changeUser, changePass);

        // Finally, make sure it gets an "ok" response.
        assertEquals(output, goodResponse);
    }

    public void testChangePasswordInvalidUser() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for change password.
        App app = new App();

        // Attempt to change password by passing in invalid username.
        // ASSUMPTION: Username isn't in table.
        String invalidUser = "NONEXISTENT USER!";
        String changePass = "pass1";

        // Attempt to change password.
        String output = app.changePassword(invalidUser, changePass);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testChangePasswordInvalidPass() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for change password.
        App app = new App();

        // Attempt to change password by passing in invalid password.
        // ASSUMPTION: Password isn't correct for this user. User is registered in table.
        String changeUser = "user1";
        String invalidPass = "THE WRONG PASSWORD";

        // Attempt to change password.
        String output = app.changePassword(changeUser, invalidPass);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testChangePasswordNoUser() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for change password.
        App app = new App();

        // Attempt to change password by passing in null user.
        // ASSUMPTION: Null usernames aren't allowed in table.
        String emptyUser = null;
        String changePass = "pass1";

        // Attempt to change password.
        String output = app.changePassword(emptyUser, changePass);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testChangePasswordNoPass() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for change password.
        App app = new App();

        // Attempt to change password by passing in null password.
        // ASSUMPTION: User exists, null passwords aren't allowed (which we should want).
        String changeUser = "user1";
        String emptyPass = null;

        // Attempt to change password.
        String output = app.changePassword(changeUser, emptyPass);

        // Finally, make sure it gets a "bad data" response.
        assertEquals(output, badResponse);
    }

    public void testUpVoteSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for upvote.
        App app = new App();

        // Try to add a positive vote for post with id 1.
        // NOTE: Current implementation isn't great for testing. This is because we can't with
        // certainty know that this post has a total votes of 0. So:
        // ASSUMPTION: Post with id 1 has totalVotes == 0
        int validPostId = 1;
        int assumedNumLikes = 0;
        boolean isLiked = true;
        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), isLiked);
        int expectedTotalLikes = 1;

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);

        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), !isLiked);   // return to normal for
                                                                                        // sake of atomicity
    }

    public void testUpVoteInvalidPostId() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for upvote.
        App app = new App();

        // Try to add a positive vote for post with invalid id 666.
        // ASSUMPTION: Post with id 666 doesn't exist
        int invalidPostId = 666;
        int assumedNumLikes = 0;
        boolean isLiked = true;
        String output = app.updateLike(invalidPostId, assumedNumLikes, new java.util.Date(), isLiked);
        // int expectedTotalLikes = 0;

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(output, badResponse);
    }

    public void testDownVoteSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for downvote.
        App app = new App();

        // Try to add a negative vote for post with id 1.
        // NOTE: Current implementation isn't great for testing. This is because we can't with
        // certainty know that this post has a total votes of -1. So:
        // ASSUMPTION: Post with id 1 has totalVotes == 0
        int validPostId = 1;
        int assumedNumLikes = 0;
        boolean isLiked = false;
        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), isLiked);
        int expectedTotalLikes = -1;

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);

        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), !isLiked);   // return to normal for
                                                                                        // sake of atomicity
    }

    public void testDownVoteInvalidPostId() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for downvote.
        App app = new App();

        // Try to add a negative vote for post with invalid id 666.
        // ASSUMPTION: Post with id 666 doesn't exist
        int invalidPostId = 666;
        int assumedNumLikes = 0;
        boolean isLiked = false;
        String output = app.updateLike(invalidPostId, assumedNumLikes, new java.util.Date(), isLiked);
        // int expectedTotalLikes = 0;

        assertEquals(output, badResponse);
    }

    public void testUpVoteDownVoteSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for up and downvote.
        App app = new App();

        // Try to add a positive vote for post with id 1.
        // NOTE: Current implementation isn't great for testing. This is because we can't with
        // certainty know that this post has a total votes of 0. So:
        // ASSUMPTION: Post with id 1 has totalVotes == 0
        int validPostId = 1;
        int assumedNumLikes = 0;
        boolean isLiked = true;
        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), isLiked);
        int expectedTotalLikes = 1;

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);

        // Now, revert back to 0 likes with a down vote:
        app.updateLike(validPostId, ++assumedNumLikes, new java.util.Date(), !isLiked);
        expectedTotalLikes = 0;

        // And finally, ensure that successive like-->dislike works as expected.

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);
    }

    public void testDownVoteUpVoteSuccess() {
        // Start of every test -- create a new connection.
        // ASSUMPTION: Phase 2 App includes a non-static method for up and down vote.
        App app = new App();

        // Try to add a negative vote for post with id 1.
        // NOTE: Current implementation isn't great for testing. This is because we can't with
        // certainty know that this post has a total votes of 0. So:
        // ASSUMPTION: Post with id 1 has totalVotes == 0
        int validPostId = 1;
        int assumedNumLikes = 0;
        boolean isLiked = false;
        app.updateLike(validPostId, assumedNumLikes, new java.util.Date(), isLiked);
        int expectedTotalLikes = -1;

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);

        // Now, revert back to 0 likes with an up vote:
        app.updateLike(validPostId, --assumedNumLikes, new java.util.Date(), !isLiked);
        expectedTotalLikes = 0;

        // And finally, ensure that successive dislike-->like works as expected.

        // This test needs some method to return number of likes for a specific posts...
        // This should be created during phase 2 at some point. Let's call it "getLikes()" for now:
        // ASSUMPTION: getLikes returns an int. And, well, that it exists in the final implementation.
        assertEquals(app.getLikes(validPostId), expectedTotalLikes);
    }
}