import java.text.DecimalFormat;
/**
 * Lab10
 * 
 * Adam Stewart
 * 10/30/14
 */
public class Song
{       private String artist;
        private String title;
        private int minutes;
        private int seconds;
        
    public Song(String pArtist, String pTitle, int pMinutes, int pSeconds){
        artist=pArtist;
        title=pTitle;
        minutes=pMinutes;
        seconds=pSeconds;
    }
    
    public String getArtist(){
        return artist;
    }
    
    public String getTitle(){
        return title;
    }
    
    public int getMinutes(){
        return minutes;
    }
    
    public int getSeconds(){
        return seconds;
    }
    
    public String toString(){
        DecimalFormat df = new DecimalFormat("##00");
        return "song: " + getTitle() + " by: " + getArtist() + " length: " + getMinutes() + ":" + df.format(getSeconds());
    }
}
