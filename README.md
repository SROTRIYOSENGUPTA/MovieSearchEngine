Overview:
The goal of this Project is to implement a separate-chaining hash table that stores movie’s titles and descriptions that allows fast search of words from movie descriptions.

The hash table is used here to search for words in the description of movies. The search returns the movie titles where both words appear in the description. For example, using the given input data file, if the searched words are “tragic” and “love” the search returns the following 4 movie titles: Anders als die andern, Namus, La rosa sulle rotaie, Sangue gitano. 

Implementation
We start with an input file that contains movies’ titles and short descriptions. The title as well as the description of a movie may contain one or more words.
 
The keys into the hash table are the words from the movies description. The values are word occurrences (each movie where the word appears and its respective location within the description).
 
The hash table uses chaining to resolve collisions, meaning that each hash table (array) index is the beginning of a linked list of word occurrences.

![image](https://github.com/SROTRIYOSENGUPTA/RUMDb-Search-Engine/assets/69280834/9d5aaf32-af1e-426e-890e-ef6eb4bb03c6)

In this example, the hash table size is 8 and the it contains 5 words. Each table index holds the front to a linked list of WordOccurrences.

A WordOccurrence (red box) holds the word and an ArrayList of Locations. For example, the WordOccurrence containing Word5 tells us that word5 is present at the descriptions of movies with title 7 at position 2, title 6 at positions 3 and 7.

A Location (blue box) holds the movie title where the word is present and its position within the description. For example, word2 is present at movies title 2 and title 3. In movie title 2 the word is at position 2 (the second word in the description). In movie title 3 the word is at position 18 (the 18th word in the description).

Overview of files
Location class holds the position of one word in a movie’s description. A movie’s description first word has position 1, the second word has position 2, and so on.
WordOccurrence class represents all the occurrences of a word in the entire movie database.
MovieSearchResult class represents the result of the search for two words. The searched words will belong to the same file.
RUMdbSearchEngine class contains some methods in addition to annotated method signatures for all the methods.
Driver class,here is used to run to test any of the methods. 
data.txt, contains input data.
dataSample.txt, contains a small sample of data.txt. Used on the examples below.
noisewords.txt, contains a list of “noise” words, one per line. Noise words are commonplace words (such as “the”) that must be ignored by the search engine. 

RUMDbSearchEngine.java
Methods provided to you:

constructor, that initializes instance variables and inputs noise words from file.
hashFunction, that is used to map a word to an index into the hash table.
getLoadFactor, that computes the hash table load factor.
readInputFile, that reads movies title and description from the input data file.
The method returns an ArrayList of ArrayList of Strings where each inner ArrayList represents a movie: the first index contains the title, the remaining indices contain the movie’s description words (one word per index).
isWord, which given a word returns the word stripped of any trailing punctuation or null if the word is a noise word.
print, which prints the entire hash table.

Methods to be implemented by you:
rehash
Rehashes the hash table to newHashSize.
Rehash happens when the load factor is greater than the threshold.
The load factor is the ratio wordCount : hashSize.
getWordOccurrence
Searches the hash table for the WordOccurrence object containing the target word.
Returns the WordOccurrence object containing the target word, or null otherwise.
insertWordLocation
This method inserts a Location object into the hash table.
Use getWordOccurrence() to check if the word is present in the hash table.
If the word is present, insert location into the matching WordOccurrence object.
If the word is not present create a new WordOccurrence object and insert location into it, then insert WordOccurrence into the hash table.
In the image above a red box is a WordOccurrence, a blue box is a word Location.
Each hash table index holds the front of a linked list of WordOccurrence.
Insert a new WordOccurence at the beginning of the linked list.
Rehash if the load factor becomes greater than threshold. When rehashing, double the length of the array.
insertMoviesIntoHashTable
Implement this method to read the input text file and insert its movies into the hash table.
Use the method readInputFile() to read the input file and use its output to insert the words into the hash table.
Use isWord() to discard noise words, remove trailing punctuation, and to transform the word into all lowercase character. The hash table will contain all lowercase words.
Use insertWordLocation() to insert the word into the hash table.
This method must be implemented correctly for you to receive any credit in this assignment.
Once the previous methods have been written you can test using the driver and dataSample.txt provided. The output is shown below.

Note that the word madame (at table index 3) is present in the descriptions whose titles are “madame dubarry” and “la ragazza con la cappelliera”. At “madame dubarry” madame appears as the 4th word in the description and at “la ragazza con la cappelliera” the word appears twice as the 14th and the 16th words.

Also note the collisions, different words mapping into the same array index. For example, the words score and restored both map into array index 10.

The searched words “tragic” and “love” are not present in the database.

![image](https://github.com/SROTRIYOSENGUPTA/MovieSearchEngine/assets/69280834/191eec29-e9a7-4789-8383-99b18735f5e1)

createMovieSearchResult
Creates and returns an ArrayList of MovieSearchResult objects.
A MovieSearchResult refers to one movie. It contains all the locations of wordA and wordB within the movie’s description.
DO NOT compute the minimum distance between two locations of wordA and wordB at this point. See next method.
Returns null if one or both words are not present in the hash table.
calculateMinDistance
Computes the minimum distance between the two words, wordA and wordB, in a MovieSearchResult object.
Updates the MovieSearchResult with the computed minimum distance.
Recall that a MovieSearchResult refers to ONE movie. Therefore, this method is computing how close these two words appear in the description.
See an example algorithm to compute the minimum distance in the Java file.
topTenSearch
This method’s purpose is to search the movie database to find movies that contain two words (wordA and wordB) in their description.
The method searches and returns the top 10 movies where two words (wordA and wordB) appear closer together in the descriptions.
The search result is sorted by the words location distance in the movie description. The first movie will have the words closer together, and the last movie will have the words farther apart.
When the words “tragic” and “love” are searched in the hash table when the input file is “data.txt” the output movie titles are:

![image](https://github.com/SROTRIYOSENGUPTA/MovieSearchEngine/assets/69280834/de2027b6-1baf-4c43-9719-346fbd5a7930)

Note that these on the movie “Anders all die andern” the distance between these words is 10 (ten), love is at position 6 and tragic is at position 16. 





