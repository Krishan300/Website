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
     *tests constructor that gson is initialized and index is incremented
     */
    public void testConstructor()
    {
        App app = new App(true);
        assertNotNull(app.gson);
    }


    /**
     *tests the getAllData Method for tables with 0, 1, or multiples datums
     */
  /*  public void testGetMessage()
    {
        App app = new App(true);
        String output = app.getMessage();
        //not sure what output will look like because backend isnt done
        assertEquals(output, "[]");

        Message m = new Message();
        m.user_id = 1;
        m.message_id=2;
        m.title = "test";
        m.body = "placeholder";
        m.uploadDate = new Date(117, 1, 16, 20, 10);
        //d.lastLikeDate = new Date(117, 1, 16, 20, 10);

        app.insertMessage(m);
        output = app.getMessage();
        assertEquals(output, "{\"user_id\":1,\"message_id\":2, \"title\":\"test\",\"body\":\"placeholder\",\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\"}");

        Message d2 = new Message();
        d2.user_id = 2;
        d2.message_id=1;
        d2.title = "test2";
        d2.body = "test message2";

        d2.uploadDate = new Date(117, 1, 16, 20, 10);

        app.insertMessage(d2);

        output = app.getMessage();

        assertEquals(output, "[{\"user_id\":1,\"message_id\":2, \"title\":\"test\",\"body\":\"placeholder\",\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\"},{\"user_id\":2, \"message_id\":1, \"title\":\"test2\", \"body\":\"test message2\",\"uploadDate\":\"Feb 16, 2017 8:10:00 PM\"}]");
    } */




    /**
     *tests the getUser Method for tables with 0, 1, or multiples datums
     */
    public void testGetUser () {
        App app=new App(true);
        String output;
        User u=new User();
        u.user_id=2;
        u.username="John";
        u.realname="Catherine";
        u.email="genericemail@aol.com";
        app.insertUser(u);
        output=app.getUser();
        assertEquals(output, "[{\"user_id\":2,\"username\": \"John\", \"realname\": \"Catherine\", \"email\": \"genericemail@aol.com\"}]");

        User u2=new User();
        u.user_id=1;
        u.username="Jimmy";
        u.realname="Jimmyrealname";
        u.email="dumbemail@aol.com";
        app.insertUser(u2);
        output=app.getUser();
        assertEquals(output, "[{\"user_id\":2,\"username\": \"John\", \"realname\": \"Catherine\", \"email\": \"genericemail@aol.com\"}, {\"user_id\":1,\"username\": \"Jimmy\", \"realname\": \"Jimmyrealname\" \"email\": \"dumbemail@aol.com\"}]");


    }

    /**
     *tests the insertDatum Method on both a null datum and one in good form
     */
  /*  public void testInsertMessage()
    {
        App app = new App(true);

        Message m = new Message();
        m.user_id = 1;
        m.message_id=2;
        m.title = null;
        m.body = "placeholder";

        //bad date because title and dates are null
        assertEquals(app.insertMessage(m), "{\"res\":\"bad data\"}");

        m.title = "test title";
        m.uploadDate = new Date(117, 1, 16, 20, 10);


        //works because everything now has initial value
        assertEquals(app.insertMessage(m), "{\"res\":\"ok\"}");
    }*/

  public void testInsertUser(){

      App app = new App();
      User u= new User();
      u.user_id=1;
      u.username="The User";
      u.realname="John";
      u.email="Placeholder";

      assertEquals(app.insertUser(u), "{\"res\":\"ok\"}");


      u.realname=null;


      assertEquals(app.insertUser(u),"{\"res\":\"bad data\"}");







  }


    /**
     *tests the updateLike Method at initial time, and after a like
     * and a dislike
     */
    public void testUpdateLike()
    {
        App app = new App(true);
        Vote v = new Vote();
        v.user_id = 1;
        v.message_id=2;
        v.isLiked=true;

        String output = null;
        try
        {
            app.updateLike(v);
            output = app.getUpvotes();
        }
        catch(Exception e)
        {
            fail("error: updatelike could not increment likes");
            e.printStackTrace();
        }
        assertEquals(output, "{\"user_id\":1,\"message_id\":2}");
        // EXPECTED {"index":1,"title":"test","comment":"test comment","numLikes":1,"uploadDate":"Feb 16, 2017 8:10:00 PM","lastLikeDate":"Feb 16, 2017 8:10:00 PM"}
        // HAD

        try {
            v.isLiked=false;
            app.updateLike(v);
            output = app.getDownvotes();
        }
        catch(Exception e)
        {
            fail("error: update like could not decrement likes");
            e.printStackTrace();
        }
        assertEquals(output, "[{\"user_id\":1,\"message_id\":2}]");
    }

    /**
     *tests the createDB Method by initializing an app which calls
     * createDB() and then increments index from 0
     */
    public void testCreateDB()
    {
        App app = new App(true);
        assertNotNull(app.gson);
    }
}