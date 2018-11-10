import java.util.*;
import java.io.*;
/**
 * Write a description of class Lab10 here.
 * 
 * Adam Stewart 
 * 10/30/14
 */
public class Lab10
{   

    public static void main(String[] args){
        read1("songs.txt");
        read2("songs.txt");
        System.out.println("");
        read3("songs.txt");
    }

    public static void read1(String music){
        try{
            Scanner sc = new Scanner(new File(music));
            while(sc.hasNext()){
                String songLine = sc.nextLine();
                System.out.println(songLine);
            }
        }catch(IOException e){System.out.println(e.toString());}
        System.out.println("");
    }

    public static void read2(String music){

        try{
            Scanner sc = new Scanner(new File(music));
            while(sc.hasNext()){
                String songLine = sc.nextLine();
                String[] parts = songLine.split(",");
                String artist=parts[0];
                String title=parts[1];
                int minutes=Integer.parseInt(parts[2].substring(0,1));
                int seconds=Integer.parseInt(parts[2].substring(2,4));
                System.out.println("Title: " + title + " & Minutes: " + minutes);
            }
        }catch(IOException e){System.out.println(e.toString());}
    }       

    public static void read3(String music){
        ArrayList <Song> listOfSongs = new ArrayList <Song>();

                try{
            Scanner sc = new Scanner(new File(music));
            while(sc.hasNext()){
                String songLine = sc.nextLine();
                String[] parts = songLine.split(",");
                String artist=parts[0];
                String title=parts[1];
                int minutes=Integer.parseInt(parts[2].substring(0,1));
                int seconds=Integer.parseInt(parts[2].substring(2,4));
                Song s1 = new Song(artist, title, minutes, seconds);
                listOfSongs.add(s1);
            }
        }catch(IOException e){System.out.println(e.toString());}

        for(Song list : listOfSongs){
            String allSongs = list.toString()+"\n";
            System.out.println(allSongs);
        }
        
    }
}
