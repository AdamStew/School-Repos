

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestGame.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestGame
{
    /**
     * Default constructor for test class TestGame
     */
    public TestGame()
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
    public void testConstructor() {
        Game game = new Game();
        // After instantiating the Game, the message should not be null
        String m = game.getMessage();
        String nullString = null;
        assertThat(m, (not(nullString)));
        
    }
    
    @Test
    public void testHelp() {
        Game game = new Game();
        // After invoking the help command, the message should not be null
        game.help();
        String m = game.getMessage();
        String nullString = null;
        assertThat(m, (not(nullString)));
        
    }
    
    @Test
    public void testLook() {
        Game game = new Game();
        // After invoking the look command, the message should not be null
        game.look();
        String m = game.getMessage();
        String nullString = null;
        assertThat(m, (not(nullString)));
        
    }
}
