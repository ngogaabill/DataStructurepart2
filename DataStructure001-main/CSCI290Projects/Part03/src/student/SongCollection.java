/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */

 package student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * SongCollection.java 
 * Reads the specified data file and build an array of songs.
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore 
     * not portable. Java runs on a virtual machine that IS portable. So this 
     * is permissable because we are programming in Java and Java runs on a 
     * virtual machine not directly on the hardware.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) throws FileNotFoundException{
        ArrayList<Song> tempSongs = new ArrayList<Song>();
	// use a try catch block
        // read in the song file and build the songs array
        // there are several ways to read in the lyrics correctly.  
        // the line feeds between lines and the blank lines between verses
        // must be retained.
        try {
            
            FileInputStream stream = new FileInputStream(filename);
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\"");
            while (scanner.hasNextLine())
            {
                scanner.next();
                if (scanner.hasNext())
                {
                    String artist = scanner.next();
                    //scanner.nextLine();
                    scanner.next();
                    String title = scanner.next();
                    scanner.next();
                    String lyrics = scanner.next();

                    Song newSong = new Song(artist, title, lyrics);
                    tempSongs.add(newSong);
                }
                
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File does not exist.");;
        }
        songs = new Song[tempSongs.size()];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = tempSongs.get(i);
        }
        Arrays.sort(songs);
        
        // sort the songs array using Arrays.sort (see the Java API)
        // this will use the compareTo() in Song to do the job.
        
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException{
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        // todo: show song count
        Stream.of(sc.getAllSongs()).limit(1000).forEach(System.out::println);
    }
}
