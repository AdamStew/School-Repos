
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.*;
import javax.imageio.*;
import java.io.*;
import java.applet.*;

/**
 * 
 * Adam Stewart 
 * Real Estate Advertisement (Project 1)
 */
public class Test extends JApplet
{
    /**
     * Paint method for applet.
     * 
     * @param  g   the Graphics object for this applet
     */
    public void paint(Graphics g)
    {
        //Housing Flyer
        Font myFont = new Font("serif", Font.PLAIN, 32);
        g.setFont(myFont);
        g.drawString("House Sellr", 350, 40); //Realtor Info
        g.setColor(Color.lightGray);
        g.drawString(("2014 Adduhm Avenue"), 20, 40); //Street/House Name
        g.setColor(Color.black);
        g.drawRect(0, 0, 500, 300);//border
        g.drawRect(30, 50, 240, 240);//house floor plan
        g.drawLine(110, 50, 110, 290); //walls
        g.drawLine(190, 50, 190, 290);
        g.drawLine(30, 130, 270, 130);
        g.drawLine(30, 210, 270, 210);
        g.setColor(Color.blue);
        g.fillOval(280, 120, 30, 50);//pool
        Font rmFont = new Font("serif", Font.PLAIN, 12);//Room Labels
        g.setFont(rmFont);
        g.setColor(Color.black);
        g.drawString(("Dining"), 55, 257);
        g.drawString(("Foyer"), 135, 257);
        g.drawString(("Nook"), 215, 257);
        g.drawString(("Kitchen"), 55, 177);
        g.drawString(("Family"), 135, 177);
        g.drawString(("Bedroom"), 215, 177);
        g.drawString(("Bath"), 55, 97);
        g.drawString(("Study"), 135, 97);
        g.drawString(("Bedroom"), 215, 97);
        g.drawString(("Pool"), 282, 120);
        g.drawLine(320,0, 320, 300); //card seperator
        BufferedImage photo = null;  //Inserting Picture of House Sellr
            try 
            { 
              URL u = new URL(getCodeBase(), "MyPhoto.jpg");
              photo = ImageIO.read(u);
            } 
            catch (IOException e) 
            {
                g.drawString("Problem reading the file", 100, 100);
            }
            g.drawImage(photo, 419, 50, 80, 60, null);  //image values
        g.drawString(("-Ranch Style"), 330, 80); //House Info    
        g.drawString(("-1600 Sq. Ft (40x40)"), 330, 100); 
        g.drawString(("-2 Bedroom, 1 Bath"), 330, 120); 
        g.drawString(("-Comes with Pool!"), 330, 140);
        g.drawString(("-Located in U.S."), 330, 160); 
        g.drawString(("-Call 1-800-505-HOME"), 330, 180); 
    }

   
}
