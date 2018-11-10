
/**
 * Write a description of class LanguageArray here.
 *
 * @author Adam Stewart
 * @version 09/06/2017
 */
import java.util.ArrayList;
public class ArrayImplementation
{
    private String[ ]    alphabet;
    private String[ ][ ] language;
    private int          generations;

    public ArrayImplementation(String[] alphabet, int generations )
    {
        this.alphabet    = alphabet;
        this.generations = generations;
        int columns = 1;
        this.language = new String[ this.generations + 1 ][ ];
        for (int row = 0; row <= this.generations; row++)
        {
            this.language[ row ] = new String[ columns ];
            columns             *= alphabet.length;
        }

        this.generateStrings();
    }

    private void generateStrings( )
    {
		this.language[0][0] = "";  //First generation is always empty string.

		for (int i=1; i <= this.generations; i++) { //Loop for every generation.
			int prevSize = (int)Math.pow(this.alphabet.length, (i-1));
			for (int j=0; j < this.alphabet.length; j++) { //Loop for ever letter in the alphabet.
				for (int k=0; k < (((int)Math.pow(this.alphabet.length, i))/this.alphabet.length); k++) { //Loop for every string the letter must concat with.
					this.language[i][j*prevSize + k] = this.language[i-1][k] + this.alphabet[j];
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
            System.out.println( this.stringFor( this.language[ n ] ) );
        }                
    }

    public static void main( String[] args )
    {
        String[ ] alphabet1 = { "$" };        
        ArrayImplementation language1 = new ArrayImplementation( alphabet1, 9 );
        language1.print( );
        System.out.println( );
        System.out.println( );

        String[ ] alphabet2 = { "0", "1" };       
        ArrayImplementation language2 = new ArrayImplementation( alphabet2, 4 );
        language2.print( );
        System.out.println( );
        System.out.println( );

        String[ ] alphabet3 = { "a", "b", "c" };        
        ArrayImplementation language3 = new ArrayImplementation( alphabet3, 3 );
        language3.print( );
    }
}
