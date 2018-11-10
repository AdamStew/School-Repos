/**
 * A very simple Library
 * 
 * @author CIS162 
 * @version Spring/Summer 2014
 */
import java.util.ArrayList;
public class Library
{
    private ArrayList < Book > books = new ArrayList < Book > ();
    
    public Library() {
        
        books.add( new Book("Tom Sawyer","Mark Twain"));
        books.add( new Book("A Tale of Two Cities","Charles Dickens"));
        books.add( new Book("The Great Gatsby","Scott Fitzgerald"));
        books.add( new Book("The Old Man and the Sea","Ernest Hemingway"));
        books.add( new Book("The Grapes of Wrath","John Steinbeck"));
    }
    
    public String authorContains(String key) {
        String result = "";
        for(Book thisBook : books ) {
            String thisAuthor = thisBook.getAuthor();
            if (thisAuthor.contains(key)) {
                result = result + thisBook.toString()+"\n";
            }
        }
        return result;
    }
    
     public String titleContains(String key) {
        String result = "";
          for(Book thisBook : books ) {
            String thisTitle = thisBook.getTitle();
            if (thisTitle.contains(key)) {
                result = result + thisBook.toString()+"\n";
            }
        }
        return result;
    }
    
}