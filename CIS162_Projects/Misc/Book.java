
/**
 * A very simple Book
 * 
 * @author CIS162 
 * @version Spring/Summer 2014
 */
public class Book
{
   private String title;
   private String author;
   
   public Book(String pTitle,String pAuthor) {
       title = pTitle;
       author = pAuthor;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String toString() {
        return title+" by "+author;
    }
}
