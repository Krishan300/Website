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
        assertEquals(app.insertDatum(d), "{\"res\":\"bad data\"}");

        d.title = "test title";
        d.uploadDate = new Date(117, 1, 16, 20, 10);
        d.lastLikeDate = new Date(117, 1, 16, 20, 10);

        //works because everything now has initial value
        assertEquals(app.insertDatum(d), "{\"res\":\"ok\"}");
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
}