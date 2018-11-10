
/**
 * Write a description of class LanguageArrayList here.
 *
 * @author Adam Stewart
 * @version 09/06/2017
 */
import java.util.ArrayList;
public class ArrayListImplementation
{
    private String[ ] alphabet;
    private Object[ ] language;
    private int       generations;


    public ArrayListImplementation( String[ ] alphabet, int generations )
    {
        this.alphabet    = alphabet;
        this.generations = generations;

        this.language = new Object[ this.generations + 1];
        for (int n = 0; n <= this.generations; n++ )
        {
            this.language[ n ] = new ArrayList<String>( );            
        }

        this.generateStrings( );
    }
    

    private void generateStrings( )
    {
		((ArrayList<String>)this.language[0]).add("");  //First generation is always empty string.
		for (int i=1; i <= this.generations; i++) { //Loop for every generation.
			int prevSize = (int)Math.pow(this.alphabet.length, (i-1));
			for (int j=0; j < this.alphabet.length; j++) { //Loop for ever letter in the alphabet.
				for (int k=0; k < (((int)Math.pow(this.alphabet.length, i))/this.alphabet.length); k++) { //Loop for every string the letter must concat with.
					((ArrayList<String>)this.language[i]).add(((ArrayList<String>)this.language[i-1]).get(k) + this.alphabet[j]);
				}
			}
		}
    }


    private String stringFor( String[ ] str )
    {
        if (str.length == 0)
        {
            return "{ }";
        }

        String st = "{";
        for (String s: str)
        {
            st += " " + s + ",";
        }
        st = st.substring(0, st.length( )-1) + " }";
        return st;
    }

    public void print()
    {
        System.out.println( "A language with string lengths <= " + this.generations + " over alphabet " + stringFor( this.alphabet ) );
        for( int n = 0; n <= this.generations; n++ )
        {
            System.out.print( n + ": " );
            System.out.println( (ArrayList<String>)this.language[ n ] );
        }                
    }

    public static void main( String[] args )
    {
        String[ ] alphabet1 = { "$" };        
        ArrayListImplementation language1 = new ArrayListImplementation( alphabet1, 9 );
        language1.print( );
        System.out.println( );

        String[] alphabet2 = { "0", "1" };        
        ArrayListImplementation language = new ArrayListImplementation( alphabet2, 4 );
        language.print( );
        System.out.println( );

        String[ ] alphabet3 = { "a", "b", "c" };        
        ArrayListImplementation language3 = new ArrayListImplementation( alphabet3, 3 );
        language3.print( );
    }
}
