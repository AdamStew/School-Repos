import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestTreeDB.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestTreeDB
{
    TreeDB db; 
    /**
     * Default constructor for test class TestTreeDB
     */
    public TestTreeDB()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        db = new TreeDB();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testToString()
    {
        java.lang.String string1 = db.toString();
        assertEquals("Pin Oak\t59\t72\nWhite Pine\t50\t80\nSilver Maple\t50\t80\nTulip Tree\t70\t90\nRiver Birch\t40\t70\n", string1);
    }

    @Test
    public void testRangeLow()
    {
        assertEquals("", db.queryByPossibleHeight(10));
    }

    @Test
    public void testRangeHigh()
    {
        assertEquals("", db.queryByPossibleHeight(200));
    }

    @Test
    public void testRangeValid()
    {
        assertEquals("White Pine\t50\t80\nSilver Maple\t50\t80\nTulip Tree\t70\t90\n",db.queryByPossibleHeight(75));
    }

    @Test
    public void testQueryByName()
    {
        assertEquals("White Pine\t50\t80\n", db.queryByName("White Pine"));
    }
    @Test
    public void testGetMaxHeight()
    {
        assertEquals(90, db.getMaxHeight());
    }
    
    @Test
    public void testGetMinHeight()
    {
        assertEquals(40, db.getMinHeight());
    }
  
    @Test
    public void testGetAverageHeight()
    {
        assertEquals(66.1, db.getAverageHeight(),0.1);
    }
    
}