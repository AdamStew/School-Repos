/**
 * Contact encapsulates a name and a phone number.
 * 
 * @author (your name) 
 * @version (10-20-2014)
 */

import java.util.*;
import java.awt.event.*;
import java.io.*;

public class Contact
{
    private String name;
    private String number;

    /**
     * Constructor for a Contact. Calls the set method, which checks for a null reference.
     * 
     * @param  contact   a reference to an existing Contact, or possibly a null reference.
     */
    public Contact( Contact contact )
    {
        set(contact);
    }

    /**************************************************************************************
     * Appropriately sets the name and number fields from the contact object,
     * allowing for the possibility that the contact parameter is null.
     * 
     * @param  contact  
     */
    public void set( Contact contact )
    {
        if (contact == null)
        {
            name = "";
            number = "";
        }else{
            name = contact.name;
            number = contact.number;
        }
    }

    public String getName( )
    {
        return name;
    }

    public String getNumber( )
    {
       return number;
    }

    public void setName( String pName )
    {
        name = pName;
    }

    public void setNumber( String pNumber )
    {
        number = pNumber;
    }

    public String toString( )
    {
        return name + "," + number;
    }

    /**
     * reads and extracts text to define the name and number of this Contact instance.
     * 
     * @param  fileReader 
     */
    public void readFile( Scanner fileReader ) 
    {
        {
            String info;
            String[] str;

            try
            {
                // read one line of text
                info = fileReader.nextLine();

                str = info.split(",");

                // extract each field
                name   = str[ 0 ];
                if (name.length() == 0)
                {
                    name = "no name";
                }
                number = str[ 1 ];
            }
            catch (Exception e)
            {
                System.out.println( "Error" );
            }
        }
    }

    /**
     * writes text for this Contact instance to file.
     * 
     * @param  writer 
     */
    public void writeFile( FileWriter writer ) 
    {
        try
        { 
            writer.write( this.toString( )+  "\r\n" );
        } 
        catch( IOException e )
        {
            System.out.println( "Error occured while writing to file: " + writer );
        }        
    }
}
