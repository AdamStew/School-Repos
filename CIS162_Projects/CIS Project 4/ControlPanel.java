import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JDialog;
import java.util.*;

public class ControlPanel extends JPanel implements ActionListener
{
    // Fields (instance variables/attributes)
    private JLabel      phoneNumberLabel;  // a reference to the phoneNumberLabel that is displayed in the PhoneNumberDisplayPanel.

    private Contact     nameAndNumber;     // Holds the name and number for a phone Contact.
    // Contact lists;
    private ContactList contacts;          // Encapsulates a list of saved contacts, ie.e phone Contacts.
    private ContactList callsMade;         // Encapsulates a list of calls made, i.e. phone Contacts.

    // File names
    private String      contactsFileName;
    private String      callsMadeFileName;

    // Two auxiliary windows for a display of phone contacts and a log of phone calls smade.
    private Window      contactsWindow;   
    private Window      callsMadeWindow;

    // Panel controls -- arranged in the order in which they are to appear on the screen, i.e. rows of labels and buttons 
    private JLabel      dummy;
    private JLabel      nameLabel;
    private JButton     setNameButton;

    private JButton     sendButton;
    private JButton     clearNameButton;   
    private JButton     findNameButton;

    private JButton     saveContactButton;
    private JButton     exitButton;
    private JButton     findNumberButton;

    /**
     * Constructor for an object of class ControlPanel.
     * 
     * @param  phoneNumberLabel   a reference to the JLabel that is 
     *                            displayed by the PhoneNumberDisplayPanel.
     */
    public ControlPanel( JLabel phoneNumberLabel )
    {
        this.phoneNumberLabel = phoneNumberLabel;

        setBorder( BorderFactory.createLineBorder( Color.MAGENTA, 3 ) );
        setLayout( new GridLayout( 4, 3 ) ); 
        setBackground( Color.WHITE );

        nameAndNumber = new Contact( null );

        dummy             = createLabel(  "Name",         JLabel.CENTER,  "Cambria", Color.BLACK, 14  );
        nameLabel         = createLabel(  "",             JLabel.CENTER,  "Cambria", Color.RED,   12  );
        setNameButton     = createButton( "Set name",     JButton.CENTER, "Cambria", Color.BLACK, 14, this );       

        sendButton        = createButton( "Send call",    JButton.CENTER, "Cambria", Color.BLACK, 14, this );
        clearNameButton   = createButton( "Clear name",   JButton.CENTER, "Cambria", Color.BLACK, 14, this ); 
        findNameButton    = createButton( "Find name",    JButton.CENTER, "Cambria", Color.BLACK, 14, this );

        saveContactButton = createButton( "Save contact", JButton.CENTER, "Cambria", Color.BLACK, 14, this ); 
        exitButton        = createButton( "Exit",         JButton.CENTER, "Cambria", Color.BLACK, 14, this );   
        findNumberButton  = createButton( "Find number",  JButton.CENTER, "Cambria", Color.BLACK, 14, this );

        add( new JLabel( ) );
        add( new JLabel( ) );
        add( new JLabel( ) );

        /*******************************************************************
         * add the rest of the 9 labels and buttons to this panel
         */

        add(dummy);
        add(nameLabel);
        add(setNameButton);
        add(sendButton);
        add(clearNameButton);
        add(findNameButton);
        add(saveContactButton);
        add(exitButton);
        add(findNumberButton);

        FileChooser textFile = new FileChooser();
        int x;
        int y;
        int width;
        int height;
        boolean disableCloseButton;

        contactsFileName  = textFile.chooseFile( "open", "contacts" );        
        callsMadeFileName = textFile.chooseFile( "open" ,"calls sent" );

        x = 410;
        y = 25;
        width  = 325;
        height = 200;
        disableCloseButton = true;
        contactsWindow  = new Window( "Contact list", x, y,       width, height, Color.MAGENTA, disableCloseButton );
        callsMadeWindow = new Window( "Calls sent",   x, y + 210, width, height, Color.BLUE,    disableCloseButton );

        contacts  = new ContactList( contactsFileName,  contactsWindow );
        callsMade = new ContactList( callsMadeFileName, callsMadeWindow );
    }

    /**
     * Creates a JButton
     * 
     * @param text       caption for the button
     * @param alignment  JButton.CENTER | JButton.LEFT | JButton.RIGHT
     * @param fontName   e.g. "Cambria"
     * @param fontColor  e.g. Color.BLACK
     * @param fontSize   e.g. 14
     * @param listener   e.g. this
     * @return           the created JButton 
     */
    public JButton createButton( String text, int alignment, String fontName, Color fontColor, int fontSize, ActionListener listener )
    {
        JButton button = new JButton( text );

        button.setPreferredSize( new Dimension( 120, 25 ) );        

        if (!fontName.equals( "Dialog" ))
        {
            button.setFont( new Font( fontName, 1, fontSize ) );
        }
        button.setForeground( fontColor );
        button.addActionListener( listener );

        return button;
    }    

    /**
     * Creates a JLabel
     * 
     * @param text       text for the label
     * @param alignment  JButton.CENTER | JButton.LEFT | JButton.RIGHT
     * @param font  
     * @param fontColor   
     * @param fontSize  
     * @return           the created JLabel 
     */
    public JLabel createLabel( String text, int alignment, String font, Color fontColor, int fontSize )
    {
        JLabel label = new JLabel( text );  //super( text );

        label.setPreferredSize( new Dimension( 100, 25 ) );        

        if (!font.equals( "Dialog" ))
        {
            label.setFont( new Font( "Cambria", 1, fontSize ) );
        }
        label.setHorizontalAlignment( alignment );
        label.setForeground( fontColor );

        return label;
    }

    /**
     * Writes one contact object to the specified file
     * 
     * @param fileName  the name of the output file
     * @param contact   the object to be written to file
     */
    private void writeToFile( String fileName, Contact contact )
    {
        try
        { 
            FileWriter writer = new FileWriter( fileName, true );
            contact.writeFile( writer );
            writer.close( );
        } 
        catch( IOException e )
        {
            System.out.println( "Error occured while opening the file: " + fileName );
        }        
    }

    /**
     * displays the Contact list of objects in a new Window
     * 
     * @param list    the list to be printed to the new window
     * @param title   the title for the newly created window
     */
    public void showArrayList( ArrayList<Contact> list, String title ) 
    {
        // creates a new window with the given title
        int x = 410;
        int y = 445;
        int width  = 350;
        int height = 200;
        boolean disableCloseButton = false;
        Window newWindow  = new Window( title, x, y, width, height, Color.RED, disableCloseButton );

        // Prints the list to the new Window.
        // Special case: with an empty list, it prints an appropriate message to the newWindow.
        if (list == null)
        {
            newWindow.println( "No contact information saved" );
        }
        else
        {
            for ( Contact c: list )
            {
                newWindow.println( c.toString() );
            }
        }
    }    

    /**
     * Uses the JOPtionPane.showInputDialog to set the name field in the nameAndNumber Contact object.
     */ 
    private void setName( )
    {
        nameAndNumber.setName( 
            (String)JOptionPane.showInputDialog(
                this, 
                "Enter name", 
                "Name dialog", 
                JOptionPane.PLAIN_MESSAGE, 
                null,
                null,
                "Jane Doe"
            )
        );

        nameLabel.setText( nameAndNumber.getName() );     
    }

    /*****************************************************************************
     * If a contact is not already saved in the contacts list, then it:
     *     adds a contact, comprising name and phoneNumber, to the list of contacts, 
     *     prints it on the contacts window, and 
     *     writes it to the file, called contactsFileName.
     * 
     * Special case: the name or the number field, or both, is not defined. To save a contact,
     *               both fields must have values.
     */ 
    private void saveContact( )
    {  
        Contact c = new Contact(null);
        c.setName(nameLabel.getText());
        c.setNumber(phoneNumberLabel.getText());
        if((contacts.found(c)==false) && (c.getName()!= "" || c.getNumber()!= "")){
            contacts.add(c);
            writeToFile(contactsFileName, c);
            contactsWindow.println(c);
        }
    }

    /*****************************************************************************
     * To send a call, it
     *     searches the contact list for an existing contact with that number,
     *     uses the result to define the name of the contact being called,
     *     adds the contact to the callsMade list, 
     *     adds the contact to the callsMadeWindow, and
     *     writes the contact to the file, identified with callsMadeFileName.
     * 
     * Special cases: No action is taken for a displayed phone number that has fewer than 12 characters.
     *                The method sends a call as "no name", if the contact list does not have an entry 
     *                for that number.
     */ 
    private void sendCall( )
    {  
        Contact c = new Contact(null);
        c.setNumber(phoneNumberLabel.getText());
        if((contacts.searchByNumber(phoneNumberLabel.getText())!=null)){
            if((phoneNumberLabel.getText().length()==12) ){
                if(contacts.found(c)==true){
                    c=contacts.searchByNumber(phoneNumberLabel.getText());
                    writeToFile(callsMadeFileName, c);
                    callsMadeWindow.println(c);
                    callsMade.add(c);
                    nameLabel.setText(contacts.searchByNumber(phoneNumberLabel.getText()).getName());
                }else{
                    callsMadeWindow.print("no name");
                    callsMadeWindow.println(c);
                    nameLabel.setText("no name");
                    c.setName("no name");
                    writeToFile(callsMadeFileName, c);
                    callsMade.add(c);
                }
            }
        }
    }

    /*******************************************************************************
     * Does a reverse directory lookup in the contacts using the phoneNumberLabel text.
     * Sets the nameLabel from the contact returned.
     * 
     * Special case. if no contact is found as a result of the search,
     *               the method clears the nameLabel.
     */
    private void findName( )
    {
        Contact c = new Contact(null);
        if((contacts.searchByNumber(phoneNumberLabel.getText())!=null)){
            c=contacts.searchByNumber(phoneNumberLabel.getText());
            nameLabel.setText(c.getName());
        }else{
            nameLabel.setText("");
        }
    }

    /*************************************************************************************
     * Does a directory lookup, contacts.serachByName, on the displayed name in the nameLabel.
     * The method displays the resulting ArrayList of matching contacts by calling showArrayList.
     * 
     * Special case: if no number is listed for the name, the method calls showArrayList with a null first argument.
     */
    private void findNumber( )
    {
        if(contacts.searchByName(nameLabel.getText()) !=null){
            showArrayList(contacts.searchByName(nameLabel.getText()), nameLabel.getText());
        }else{
            showArrayList(null, nameLabel.getText());
        }
    }

    /**********************************************************************
     * clears the displayed name
     */
    private void clearName( )
    {
        nameLabel.setText("");    
    }

    /**
     * Handles the pending event, whenever it is called.
     * 
     * @param event  
     */
    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if (source == setNameButton)
        {
            setName( );
        }
        else if (source == saveContactButton)
        {
            saveContact( );
        }
        else if (source == sendButton)
        {
            sendCall( );
        }
        else if (source == findNameButton)
        {
            findName( );
        }
        else if (source == clearNameButton)
        {
            clearName( );
        }
        else if (source == exitButton)
        {
            System.exit( 0 );
        }
        else if (source == findNumberButton)
        {
            findNumber( );
        }
    }
}