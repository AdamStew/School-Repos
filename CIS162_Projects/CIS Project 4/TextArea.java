import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.*;

/**
 * Write a description of class MyTextArea here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TextArea extends JTextArea
{
    public TextArea(Dimension dimension, Color color )
    {
        setSize( dimension );
        setForeground( color );
        setFont( new Font( "Courier New", Font.BOLD, 13 ) );
    }

    public void setFontSize( int fontSize )
    {
        setFont( new Font( "Courier New", Font.BOLD, fontSize ) );    
    }

    public void print( String text )
    {
        this.append( text );
    }

    public void clear()
    {
        this.setText( "" );
    }
}
