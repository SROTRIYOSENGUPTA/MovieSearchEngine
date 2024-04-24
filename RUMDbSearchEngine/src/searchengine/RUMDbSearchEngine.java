package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;




/*
 * This class builds a hash table of words from movies descriptions. Each word maps to a set
 * of movies in which it occurs.
 * 
 * @author Srotriyo Sengupta 193008050 ss3414
 * @author Ana Paula Centeno
 * 
 */ 
public class RUMDbSearchEngine // this is the beginning of the program
{ 
    
    private int    hashSize;   
    // the hash table size
    private double threshold;  
    // load factor threshold. load factor = wordCount/hashSize
    private int    wordCount;  
    // the number of unique words in the table
    private WordOccurrence[] hashTable;  
    // the hash table

    private ArrayList<String> noiseWords; 
    // noisewords are not to be inserted in the hash table

    /* 
     * Constructor initilizes the hash table.
     * 
     *  @param hashSize is the size for the hash table 
     *     @param threshold for the hash table load factor. Rehash occurs when the ratio 
     *     wordCount : hashSize exceeds the threshold.
     *  @param noiseWordsFile contains words that will not be inserted into the hash table.
     */
    public RUMDbSearchEngine (int hashSize, double threshold, String noiseWordsFile)
    {

        this.hashSize   = hashSize;
        this.hashTable  = new WordOccurrence[hashSize];
        this.noiseWords = new ArrayList<String>();
        this.threshold  = threshold;
        this.wordCount  = 0;

        // Read noise words from file
        StdIn.setFile(noiseWordsFile);
        while ( !StdIn.isEmpty() ) {
            String word = StdIn.readString();
            if ( !noiseWords.contains(word) )
                noiseWords.add(word);
        }
    }

    /*
     * Method used to map a word into an array index.
     * 
     * @param word the word
     * @return array index within @hashTable
     */
    private int hashFunction ( String word ) 
    {
        int hashCode = Math.abs(word.toLowerCase().replaceAll("/[^a-z0-9]/","").hashCode());
        return hashCode % hashSize;
    }

    /*
     * Returns the hash table load factor
     * @return the load factor
     */ 
    public double getLoadFactor () 
    {
        return (double)wordCount/hashSize;
    }

    /*
     * This method reads movies title and description from the input file.
     * 
     * @param inputFile the file to be read containg movie's titles and descriptions.
     * 
     * The inputFile format:
     *         Each line describes a movie's title, and a short description on the movie.
     *         title| word1 word2 word3;
     * 
     *      Note that title can have multiple words, there is no space between the last 
     *      word on the tile and '|'
     *         No duplicate movie name accepted.
     * 
     * @return ArrayList of ArrayList of Strings, each inner ArrayList refers to a movie, 
     *         the first index contains the title, the remaining indices contain the movie's
     *         description words (one word per index). 
     * 
     *  Example: 
     *         [
     *             [full title1][word1][word2]
     *             [full title2][word1]
     *             [full title3][word1][word2][word3][word4]
     *         ]
     */
    public ArrayList<ArrayList<String>> readInputFile ( String inputFile ) 
    {

        ArrayList<ArrayList<String>> allMovies = new ArrayList<ArrayList<String>>();
        StdIn.setFile(inputFile);
        
        String[] read = StdIn.readAllStrings();
        
        for ( int i = 0; i < read.length; i++ ) 
        {
            ArrayList<String> movie = new ArrayList<String>();
            String a = "";
            do 
            {
                a += " "+read[i];
            } while ( read[i++].indexOf('|') == -1 );
            movie.add(a.substring(1,a.length()-1).toLowerCase().replaceAll("/[^a-z0-9]/",""));
            while ( i < read.length ) 
            {
                if ( read[i].indexOf(';') != -1 ) 
                {
                    movie.add(read[i].substring(0,  read[i].indexOf(';')));
                    break;
                }
                movie.add(read[i].toLowerCase().replaceAll("/[^a-z0-9]/","") );
                i++;
            }
            allMovies.add(movie);
        }
        return allMovies;
    }
    
    /* 
     * This method calls readInputFile and uses its output to load the movies and their
     * descriptions words into the hashTable.
     * 
     * Use the result from readInputFile() to insert each word and its location
     * into the hash table.
     * 
     * Use isWord() to discard noise words, remove trailing punctuation, and to transform
     * the word into all lowercase character.
     * 
     * Use insertWordLocation() to insert each word into the hash table.
     * 
     * Use insertWordLocation() to insert the word into the hash table.
     * 
     * @param inputFile the file to be read containg movie's titles and descriptions
     * 
     */
    public void insertMoviesIntoHashTable ( String inputFile ) 
    {
        ArrayList<ArrayList<String>> allMovies = new ArrayList<ArrayList<String>>();
        allMovies = readInputFile(inputFile);
        for (int i = 0; i < allMovies.size(); i++) {
            String title = new String(allMovies.get(i).get(0));         // get title of descrp.
            for (int j = 1; j < allMovies.get(i).size(); j++) {
                String word = new String(allMovies.get(i).get(j));         // get word in description
                word = isWord(word);
                if (word != null) {                                     // if word isn't noise word
                    Location loc = new Location(title, j);
                    insertWordLocation(word, loc);                         // insert word
                }
            }
        }
    }

    /**
     * Given a word, returns it as a word if it is any word that, after being stripped of any
     * trailing punctuation, consists only of alphabetic letters and digits, and is not
     * a noise word. All words are treated in a case-INsensitive manner.
     * 
     * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
     * 
     * @param word Candidate word
     * @return word (word without trailing punctuation, LOWER CASE)
     */
    private String isWord ( String word ) 
    {
        int q = 0;
        char ch = word.charAt(word.length()-(q+1));
        while (ch == '.' || ch == ',' || ch == '?' || ch == ':' || ch == ';' || ch == '!') {
            q++;
            if ( q == word.length() ) {
                // the entire word is punctuation
                return null;
            }
            int index = word.length()-(q+1);
            if (index == -1) {
                System.out.flush();
            }
            ch = word.charAt(word.length()-(q+1));    
        }
        word = word.substring(0,word.length()-q);
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetterOrDigit(word.charAt(i))) {
                return null;
            }
        }
        word = word.toLowerCase();
        if (noiseWords.contains(word)) {
            return null;
        }
        return word;
    }

    /*
     * Prints the entire hash table
     */
    public void print () {

        for ( int i = 0; i < hashTable.length; i++ ) {
            
            StdOut.printf("[%d]->", i);
            for ( WordOccurrence ptr = hashTable[i]; ptr != null; ptr = ptr.next ) {

                StdOut.print(ptr.toString());
                if ( ptr.next != null ) {
                    StdOut.print("->");
                }
            }
            StdOut.println();
        }
    }

    /*
     * This method inserts a Location object @loc into the matching WordOccurrence object
     * in the hash table. If the word is not present into the hash table, add a new 
     * WordOccurrence object into hash table. 
     *         
     * @param word to be inserted
     * @param loc the word's position within the description.
     *
     */
    private void rehash (int newHashSize) // this is the start of the method rehash
    {
        hashSize = newHashSize;                                                            
        // this will change size variable
        WordOccurrence [] newTable = new WordOccurrence[newHashSize];                     
        // this will create new hashtable with double size
        for (int q = 0; q < hashTable.length; q++) 
        {
            if (hashTable[q] != null) {                    
            // here, if table[index] is empty, go to next one
            WordOccurrence ptr = hashTable[q];
            while (ptr != null) 
            {
                WordOccurrence temp = ptr;
                ptr = ptr.next;
                temp.next = null;                                                    
                int hash = hashFunction(temp.getWord());                                 
                // this will get the hashcode of word
                if (newTable[hash] == null) 
                {                                             
                    newTable[hash] = temp;                                             
                        // this will put the linked list into new table
                }
                else 
                {
                    WordOccurrence first = newTable[hash];                                 
                    // this is a reference to LL 
                    newTable[hash] = temp;                                                 
                    // this will put in LL from old table
                    temp.next = first;                                                     
                    // this will get first item of the old LL to point to the LL in new table
                }
            }
        }
        }
        hashTable = newTable;                                                             
        // here we will change reference
    } // this is the end of the method rehash
    public void insertWordLocation (String word, Location loc) 
    {
        int z = hashFunction(word);
        if (getWordOccurrence(word) == null)
         {                                 
            // this word is not yet in hashmap
            WordOccurrence wordOccurrence = new WordOccurrence(word);         
            // this will create new wordocc for word
            wordOccurrence.addOccurrence(loc);                                 
            // this will add location
            if (hashTable[z] == null) 
            {                                     
                // index is empty
                hashTable[z] = wordOccurrence;
            }
            else 
            {
                WordOccurrence first = hashTable[z];
                wordOccurrence.next = first;
                hashTable[z] = wordOccurrence;
                
            }
            wordCount++;                                                     
            // update wordcount
        } else {
            WordOccurrence wordInTable = getWordOccurrence(word);
            wordInTable.addOccurrence(loc);
        }
        double loadfactor = getLoadFactor();
        if (loadfactor > threshold) 
        {
            rehash(2*hashSize);                                            
            // this will rehash with double the size
        }
        
    } // this is the end of the method insertWordLocation
    /*
     * Rehash the hash table to newHashSize. Rehash happens when the load factor is
     * greater than the @threshold (load factor = wordCount/hashSize).
     * 
     * @param newHashSize is the new hash size
     */
    

    /* 
     * Find the WordOccurrence object with the target word in the hash table
     * @param word search target
     * @return @word WordOccurrence object
     */
    public WordOccurrence getWordOccurrence (String word) // this is the start of the method getWordOccurence
    {
        int z = hashFunction(word);
        WordOccurrence ptr = hashTable[z];
        while (ptr != null)
         {

            if (ptr.getWord().equals(word)) 
            {                                             
                // if the word is already in hashtable
                return ptr;                                                             
                // this will return wordocc that the ptr is pointing to
            }
            ptr = ptr.next;
        }
        return null;
    } // this is the end of getWordOccurence
    
    /*
     * Finds all occurrences of wordA and wordB in the hash table, and add them to an 
     * ArrayList of MovieSearchResult based on titles.
     *         (no need to calculate distance here)
     * 
     * @param wordA is the first queried word
     * @param wordB is the second queried word
     * @return ArrayList of MovieSearchResult objects.
     */
    public ArrayList<MovieSearchResult> createMovieSearchResult (String wordA, String wordB) // this is the start of the method createMovieSearchResult
     {
        // this is the start if the method createMovieSearchResult
        ArrayList<MovieSearchResult> results = new ArrayList<MovieSearchResult>();
        ArrayList<String> marked = new ArrayList<String>();                             
        // arraylist for marked/visited movie titles
        if (getWordOccurrence(wordA) == null || getWordOccurrence(wordB) == null) return null;
    WordOccurrence wA1 =  getWordOccurrence(wordA);                                     
        // get wordocc for wordA
        WordOccurrence wB1 = getWordOccurrence(wordB);                                     
        // and wordB
        if (wA1.getLocations() == null || wB1.getLocations() == null) return null;
        ArrayList<Location> aLoc = wA1.getLocations();                                    
        // get locations
        ArrayList<Location> bLoc = wB1.getLocations();
        //System.out.println(wA1.getWord()+ " " + wB1.getWord() + " " + hashFunction("tragic"));
        for (int z = 0; z < aLoc.size(); z++) 
        {
            Location temp = aLoc.get(z);                                                 
            // this will get temp location from 
            int positionA = temp.getPosition();                                         
            // this will get position A for title temp
            if (!marked.contains(temp.getTitle())) {
                MovieSearchResult msr = new MovieSearchResult(temp.getTitle());         
                // this will make new MovieSearch with title
                marked.add(temp.getTitle());                                             
                // this will add title to marked
                msr.addOccurrenceA(positionA);
                results.add(msr);
                //System.out.println("1");
            }
            else {
                for (int y = 0; y < results.size(); y++) {                                
                    // search for the occurence in arraylist
                    if (results.get(y).getTitle().equals (temp.getTitle())) {
                        results.get(y).addOccurrenceA(positionA);
                        break;
                    }
                }
                //System.out.println("2");
            }
        }
        for (int z = 0; z < bLoc.size(); z++) {                                         
            // do the same with arraylist for wordB
            Location temp = bLoc.get(z);                                                 
            // get temp location from 
            int positionB = temp.getPosition();                                         
            // get position A for title temp
            if (!marked.contains(temp.getTitle())) {
                MovieSearchResult msr = new MovieSearchResult(temp.getTitle());         
                // make new MovieSearch with title
                marked.add(temp.getTitle());                                             
                // add title to marked
                msr.addOccurrenceB(positionB);
                results.add(msr);
            }
            else {
                for (int y = 0; y < results.size(); y++) {                                
                    // search for the occurence in arraylist
                    if (results.get(y).getTitle().equals (temp.getTitle())) {
                        results.get(y).addOccurrenceB(positionB);
                        break;
                    }
                }
            }
        } 
        //System.out.println(results.size());
        return results;
    }
    // this is the end of the method createMovieSearchResult

    /*
     * 
     * Computes the minimum distance between the two wordA and wordB in @msr.
     * In another words, this method computes how close these two words appear 
     * in the description of the movie (MovieSearchResult refers to one movie).
     * 
     * If the movie's description doesn't contain one, or both words set distance to -1;
     * 
     *  NOTE: the ArrayLists for A and B will always be in order since the words were added in order.
     *         
     * The shortest distance between two words can be found by keeping track of the index 
     * of previous wordA and wordB, then find the next location of either word and calculate 
     * the distance between the word and the previous location of the other word.
     * 
     * For example:
     *         wordA locations: 1 3 5 11
     *         wordB locations: 4 10 12 
     *         start previousA as 1, and previousB as 4, calculate distance as abs(1-4) = 3
     *         because 1<4,     update previousA to 3,     abs(4-3)   = 1 , smallest so far
     *         because 3<4,     update previousA to 5,     abs(5-4)   = 1 
     *         because 5>4,     update previousB to 10, abs(5-10)  = 5
     *         because 5<10,     update previousA to 11, abs(11-10) = 1
     *         End because all elements from A have been used.
     *             
     * @param msr the MovieSearchResult object to be updated with the minimum distance between its 
     * words.
     */
    public void calculateMinDistance(MovieSearchResult msr) // // this is the start of the method calculateMinDistance
    {
        // this is the beginning of the method calculateMinDistance

        if (msr.getArrayListA().size()==0 || msr.getArrayListB().size()==0) 
        {
            msr.setMinDistance(-1);                                                         
            // if either of the arrays is empty, distance is -1
            return;
        }
        ArrayList<Integer> locA = msr.getArrayListA();
        ArrayList<Integer> locB = msr.getArrayListB();
        int prevA = locA.get(0);
        int prevB = locB.get(0);
        int minDistance = Math.abs(prevB - prevA);
        if (locA.size() == 1 && locB.size() == 1) {
            msr.setMinDistance(minDistance);
            return;
        }
        int z = 1;                                                                             
        // index for locA
        int y = 1;                                                                             
        // index for locB
        while (z < locA.size() || y < locB.size()) {                                        
            // do until z or y is not in the array 
            if (prevA < prevB) {                                                             
                // update prevA
                prevA = locA.get(z);
                z++;
            }
            else if (prevB < prevA) {                                                         
                // update prevB
                prevB = locB.get(y);
                y++;
            }
            int temp = Math.abs((prevB - prevA));                                             
            // calculate temp min
            if (temp <= minDistance) minDistance = temp;     
    }
        msr.setMinDistance(minDistance);
        return;
    }
    // this is the end of the method calculateMinDistance

    /*
     * This method's purpose is to search the movie database to find movies that 
     * contain two words (wordA and wordB) in their description.
     * 
     * @param wordA the first word to search
     * @param wordB the second word to search
     * 
     * @return ArrayList of MovieSearchResult, with length <= 10. Each
     * MovieSearchResult object returned must have a non -1 distance (meaning that
     * both words appear in the description). The ArrayList is expected to be 
     * sorted from the smallest distance to the greatest.
     *         
     *     NOTE: feel free to use Collections.sort( arrayListOfMovieSearchResult ); to sort.
     */
    public ArrayList<MovieSearchResult> topTenSearch(String wordA, String wordB){ // this is the start of the method topTenSearch
        // this is the beginning of the method topTenSearch
        ArrayList<MovieSearchResult> movies = new ArrayList<MovieSearchResult>();                 
        // this will create output
        movies = createMovieSearchResult(wordA, wordB);                                         
        // this will create the MSRs for worda and wordb
        for(int i=0; i< movies.size(); i++)
        {
            calculateMinDistance(movies.get(i));
            if (movies.get(i).getMinDistance() == -1)movies.remove(i--);    
        }
        
        if (movies.size() > 10) 
        {                                                                                    
            // if movies array is bigger than 10
            while (movies.size() > 10)
            {                                                         
                // this will shrink the movies array to a size of 10
                Collections.sort(movies);
                for(int b = 10; b < movies.size() ; b++)
                {
                    movies.remove(b);
                }
            }
        }
        Collections.sort(movies);                                                                
        // this will sort arraylist
        return movies;
    }
} // this is the end of the method topTenSearch

// this is the end of the entire program