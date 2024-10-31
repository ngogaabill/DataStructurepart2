/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 
 * 8/2015 Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code  
 *****************************************************************************

 */

package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
/**
 * Search by Artist Prefix searches the artists in the song database 
 * for artists that begin with the input String
 * @author Bob Booth 
 */

public class SearchByArtistPrefix {
    Song.CmpArtist cmp = new Song.CmpArtist();

    // keep a local direct reference to the song array
    private Song[] songs;  

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with 
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match 
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
        ArrayList<Song> tempResults = new ArrayList<Song>();
        Song tempSong = new Song(artistPrefix, "", "");
        int location = -Arrays.binarySearch(songs, tempSong, cmp);
        if (location < 0)
        {
            location = -location;
        }
        System.out.println("Index from binary search is " + -location);
        int binarySearchComparisons = cmp.getCmpCnt();
        System.out.println("Binary search comparisons: " + binarySearchComparisons);
        int curIndex = location;
        while (songs[curIndex].getArtist().toLowerCase().startsWith(artistPrefix.toLowerCase()) && cmp.compare(songs[curIndex], songs[curIndex]) == 0)
        {
            tempResults.add(songs[curIndex]);
            curIndex--;
        }
        System.out.println("Front found at " + (curIndex + 1));
        
        curIndex = location + 1;
        while (songs[curIndex].getArtist().toLowerCase().startsWith(artistPrefix.toLowerCase()) && cmp.compare(songs[curIndex], songs[curIndex]) == 0)
        {
            tempResults.add(songs[curIndex]);
            curIndex++;
        }
        
        Song[] results;
        results = tempResults.toArray(Song[]::new);
        int listComparisons = cmp.getCmpCnt() - binarySearchComparisons;
        System.out.println("Comparisons to build the list: " + listComparisons);
        int actualComplexity = cmp.getCmpCnt();
        System.out.println("Actual complexity is: " + actualComplexity);
        int k = results.length;
        System.out.println("k: " + results.length);
        int logN = (int) (Math.log(songs.length) / Math.log(2));
        System.out.println("Log n: " + logN);
        System.out.println("Theoretical complexity: " + (logN + k));
        return results;
    }

    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties - 
     * the first argument is the data file name and the second is the partial 
     * artist name, e.g. be which should return beatles, beach boys, bee gees,
     * etc.
     */
    public static void main(String[] args) throws FileNotFoundException{
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        

        
        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);
                

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);
            
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}
