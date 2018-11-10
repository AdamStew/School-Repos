import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestTree.
 *
 * @author  CIS162
 * @version Fall 2014
 */
public class TestTree
{
    /**
     * Default constructor for test class TestTree
     */
    public TestTree()
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
    public void testConstructor()
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals("Pin Oak", tree1.getName());
        assertEquals(59, tree1.getLow());
        assertEquals(72, tree1.getHigh());
    }

    @Test
    public void testGetName() 
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals("Pin Oak", tree1.getName());
    }

    @Test
    public void testGetLow() 
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals(59, tree1.getLow());
    }

    @Test
    public void testGetHigh() 
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals(72, tree1.getHigh());
    }

    @Test
    public void testInRange() 
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals(false, tree1.inRange(50));
        assertEquals(true, tree1.inRange(59));
        assertEquals(true, tree1.inRange(65));
        assertEquals(true, tree1.inRange(72));
        assertEquals(false, tree1.inRange(80));
    }

    public void testToString() 
    {
        Tree tree1 = new Tree("Pin Oak", 59, 72);
        assertEquals("Pin Oak       59      72", tree1.inRange(50));
    }

}