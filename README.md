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
