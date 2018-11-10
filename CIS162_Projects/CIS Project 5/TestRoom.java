

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestRoom.
 *
 * @author  CIS162
 * @version Fall 2014
 */
public class TestRoom
{
    /**
     * Default constructor for test class TestRoom
     */
    public TestRoom()
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
    public void testConstructorWithOneParameter()
    {
        Room room1 = new Room("maka1");
        assertEquals("maka1", room1.getDesc());
        assertEquals(null, room1.getItem());
    }

    @Test
    public void testConstructorWith2Parameters()
    {
        Item item1 = new Item("Laptop", "Mac Air", 2);
        Room room1 = new Room("maka2", item1);
        assertEquals("maka2", room1.getDesc());
        assertEquals(item1, room1.getItem());
    }

    @Test
    public void testGetDescription() 
    {
        Room room1 = new Room("maka1");
        assertEquals("maka1", room1.getDesc());     
    }

    @Test
    public void testGetItem()
    {
        Item item1 = new Item("Laptop", "Mac Air", 2);
        Room room1 = new Room("maka2", item1);
        assertEquals("maka2", room1.getDesc());
        assertEquals(item1, room1.getItem());
    }

    @Test
    public void testAddItem() {
        Room room1 = new Room("maka1");
        assertEquals(null, room1.getItem());
        Item item1 = new Item("Laptop", "Mac Air", 2);
        room1.addItem(item1);
        assertEquals(item1, room1.getItem());
        //------------------------------------

        Room room2 = new Room("maka2", item1);
        assertEquals(item1, room2.getItem());
        Item item2 = new Item("Tablet","iPad",1);
        room2.addItem(item2);
        assertEquals(item2, room2.getItem());
    }

    @Test
    public void testHasItem() {
        Room room1 = new Room("maka1");
        Item item1 = new Item("Laptop", "Mac Air", 2);
        Room room2 = new Room("maka2", item1);
        assertEquals(false,room1.hasItem());
        assertEquals(true,room2.hasItem());
    }

    @Test 
    public void testRemoveItem() {
        // Two parts to this test
        // Part 1: Instantiate a room without an item
        Room room1 = new Room("maka1");
        assertEquals(null, room1.removeItem());
        assertEquals(null, room1.getItem());
        // Part 2: Instantiate a room with an item
        Item item1 = new Item("Laptop", "Mac Air", 2);
        Room room2 = new Room("maka2", item1);
        assertEquals(item1, room2.removeItem());
        assertEquals(null, room2.getItem());
    }

    @Test
    public void testGetLongDescription() {
        // Two parts to this test
        // Part 1: Instantiate a room without an item
        Room room1 = new Room("maka1");
        assertEquals("You are maka1",room1.getLongDescription());
        // Part 2: Instantiate a room with an item
        Item item1 = new Item("Laptop", "Mac Air", 2);
        Room room2 = new Room("maka2", item1);
        assertEquals("You are maka2 You see Laptop",room2.getLongDescription());
    }

    @Test
    public void testAddNeighborAndGetNeighbor() {
        Room room1 = new Room("maka1");
        Room room2 = new Room("maka2");
        room1.addNeighbor("north",room2);
        Room otherRoom = room1.getNeighbor("north");
        assertEquals(room2,otherRoom);
        otherRoom  = room1.getNeighbor("south");
        assertEquals(null,otherRoom);
    }
        
}

