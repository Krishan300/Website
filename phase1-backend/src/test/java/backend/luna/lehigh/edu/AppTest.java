package backend.luna.lehigh.edu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Calendar;
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
        assertTrue(app.index > 0);
    }


    /**
     *tests the getAllData Method for tables with 0, 1, or multiples datums
     */
    public void testGetAllData()
    {
        App app = new App();
        String output = app.getAllData();
        //not sure what output will look like because backend isnt done
        assertEquals(output, "");

        Datum d = new Datum();
        d.index = 1;
        d.title = "test";
        d.comment = "test comment";
        d.numLikes = 3;


        d.uploadDate = createDate();
        d.lastLikedDate = createDate();

        app.insertDatum(d);
        output = app.getAllData();
        //not sure what output will look like because backend isnt done
        //will update later with serialized json
        assertEquals(output, "");

        Datum d2 = new Datum();
        d2.index = 2;
        d2.title = "test2";
        d2.comment = "test comment2";
        d2.numLikes = 4;
        app.insertDatum(d2);
        d2.uploadDate = createDate();
        d2.lastLikedDate = createDate();

        output = app.getAllData();
        //not sure what output will look like because backend isnt done
        //will update later with serialized json
        assertEquals(output, "");
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
        d.numLikes = 3;
        //bad date because title and dates are null
        assertEquals(app.insertDatum(d), "{\"res\":\"bad data\"}");

        d.title = "test title";
        d.uploadDate = createDate();
        d.lastLikedDate = createDate();

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
        d.numLikes = 3;
        d.uploadDate = createDate();
        d.lastLikedDate = createDate();

        assertTrue(d.numLikes == 0);

        //idx is index, but should be either 0 or 1 right now. not sure
        //if counting starts at 0 here
        try
        {
            //int idx = Integer.parseInt(req.params("id"));
            app.updateLike(1, d.numLikes, d.lastLikedDate, 1);
        }
        catch(Exception e)
        {
            fail("error: updatelike could not increment likes");
            e.printStackTrace();
        }
        assertTrue(d.numLikes == 1);

        //idx is index, but should be either 0 or 1 right now. not sure
        //if counting starts at 0 here
        try {
            app.updateLike(1, d.numLikes, d.lastLikedDate, -1);
        }
        catch(Exception e)
        {
            fail("error: update like could not decrement likes");
            e.printStackTrace();
        }
        assertEquals(d.numLikes, 0);

    }



    /**
     *tests the createDB Method by initializing an app which calls
     * createDB() and then increments index from 0
     */
    public void testCreateDB()
    {
        App app = new App();
        assertTrue(app.index > 0);
    }

    /*creates a date object to be used to initialize several datum
     *objects used in testing
     */
    public java.sql.Date createDate()
    {
        Calendar cal = Calendar.getInstance();
        // set Date portion to January 10, 2017
        cal.set( cal.YEAR, 2017 );
        cal.set( cal.MONTH, cal.JANUARY );
        cal.set( cal.DATE, 10 );
        cal.set( cal.HOUR_OF_DAY, 10 );
        cal.set( cal.MINUTE, 10 );
        cal.set( cal.SECOND, 10 );
        cal.set( cal.MILLISECOND, 10 );
        //creates a date object to initialize upload dates and lastLiked date
        java.sql.Date jsqlD = new java.sql.Date( cal.getTime().getTime() );

        return jsqlD;
    }
}