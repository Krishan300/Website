package backend.luna.lehigh.edu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
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
     *
     */
    public void testConstructor()
    {
        App app = new App();
        assertTrue(app.ip != null);
        assertTrue(app.port != null);
        assertTrue(app.user != null);
        assertTrue(app.pass != null);
        assertTrue(app.db != null);
    }


    /**
     *tests the getAllData Method
     */
    public void testGetAllData()
    {
        App app = new App();
        //String output = app.getAllData();

    }

    /**
     *tests the insertDatum Method
     */
    public void testInsertDatum()
    {
        App app = new App();

        Datum d = new Datum();
        d.index = 1;
        d.title = null;
        d.comment = "test comment";
        d.numLikes = 3;
        d.uploadDate = java.sql.Timestamp.valueOf("2017-02-16 10:10:10.0");
        d.lastLikedDate = java.sql.Timestamp.valueOf("2017-02-16 10:10:10.0");
        assertEquals(app.insertDatum(d), "{\"res\":\"bad data\"}");

        d.title = "test title";
        assertEquals(app.insertDatum(d), "{\"res\":\"ok\"}");
    }


    /**
     *tests the updateLike Method
     */
    public void testUpdateLike()
    {
        App app = new App();
    }

    /**
     *tests the createDB Method
     */
    public void testCreateDB()
    {
        App app = new App();
    }
}