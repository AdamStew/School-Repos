/**
 * Window ia a JFrame with functionality similar to a BlueJ Terminal window with 
 * print and println methods, etc.
 * 
 * @author (your name) 
 * @version (10-20-2014)
 */

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    private TextArea textArea;
    private ScrollPaneForText scrollPane;
    private Dimension dimension;

    public Window( )
    {
        this( "Window", 100, 200, 350, 700, Color.GRAY, false );
    }

    public Window( String title, int x, int y, int width, int height, Color color, boolean disableClose )
    {
        super( title );
        
        if ( disableClose )
        {
            setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        }
        dimension = new Dimension( width, height );
        textArea = new TextArea( dimension, color ); 

        scrollPane = new ScrollPaneForText( textArea, dimension);

        this.add(BorderLayout.CENTER, scrollPane);
        this.setSize( dimension );
//        this.setLocation( new Point( (int) (Math.random() * 500), 0 ));
        setLocation( x, y );
        this.setVisible( true );
    }

    public void setFontSize( int fontSize )
    {
        textArea.setFontSize( fontSize );
    }

    public void print( String datum )
    {     
        textArea.print( datum );
    }

    public void print( int datum )
    {
        textArea.print( "" + datum );
    }

    public void print( long datum )
    {
        textArea.print( "" + datum );
    }

    public void print( double datum )
    {
        textArea.print( "" +  datum );
    }

    public void print( float datum )
    {
        textArea.print( "" +  datum );
    }

    public void print( boolean datum )
    {
        textArea.print( "" + datum );
    }

    public void print( char datum )
    {
        textArea.print( "" +  datum );
    }

    public void print( Object datum )
    {
        textArea.print( datum.toString() );
    }

    public void println( Object datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( String datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( int datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( long datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( double datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( float datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( boolean datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println( char datum )
    {
        this.print( datum );
        this.println( );
    }

    public void println(  )
    {
        this.print( "\n" );
    }

    public void clear()
    {
        textArea.setText( "" ); 
    }


    public static void main( String[] args)
    {
        Window window = new Window( "Example window", 100, 200, 350, 150, Color.BLUE, false );

        window.println( "Testing scrollable Window" );  
        window.println( 123 );
        window.println( 456 );
        window.println( 789 );
        window.println( 0 );
        window.println( 3.1495 );
        window.println( 2.7 );
    }    
}
