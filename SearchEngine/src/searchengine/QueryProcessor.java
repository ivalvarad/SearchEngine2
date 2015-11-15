/*
 * This class is in charge of processing the query from the Interface and returning
 * the results found.
 */
package searchengine;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iva
 */

public class QueryProcessor 
{
    // receives a string with the search query from the user.
    // in some way, it has to fix the query,
    // then it has to process the query and
    // look in the Index for the resulting docs.
    // then it has to return the results of the query to the controller.
    
    private Index index;
    double[][] rankingTable;
    private ArrayList<String> stopwords;
    private static final int collectionSize = 50;

    // loads what need the instance to function correctly.
    public QueryProcessor(Index index)
    {
        this.index = index;
        stopwords = new ArrayList<>();
        try 
        {
            // loads the stop-words file to main memory.
            loadStopWords("..\\stopwords.txt");
        }
        catch(FileNotFoundException ex){}
    }
    
    // processes a file with the stop-words
    // and loads them to memory.
    // stop-words are supposed to be one in each line of the file.
    public final void loadStopWords(String path) throws FileNotFoundException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try
        {
            String line = br.readLine();
            while (line != null) 
            {
                // obtains the whole line and saves it in the list of stop-words.
                stopwords.add(line);
                line = br.readLine();                
            }
            br.close();
        } 
        catch (IOException ex){}
    }
    
    // receives String with the query
    // returns an ArrayList with the id of the documents found
    public ArrayList<String> processQuery(String query)
    {
        // ArrayList with the id of the documents
        // which match the boolean retrival.
        ArrayList<String> queryWords = new ArrayList<>();
        ArrayList<String> rawQuery = new ArrayList<>();
        ArrayList<String> cleanQuery = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        // ignore white-spaces and all that stuff.
	queryWords = separateWords(query);
	// eliminates the stop-words.
	rawQuery = eliminateWords(queryWords);
        // eliminates repeated words and calculate the tf for the query.
        cleanQuery = calculateTF(rawQuery);
        for(int k = 0; k < cleanQuery.size(); ++k)
        {
            System.out.println(cleanQuery.get(k));
        }
        calculateTFW();
        result = processWords(cleanQuery);
        if(result.isEmpty())
        {
            result.add("Results not found\n");
        }
		printRankingTable();
        return result;
    }
    
    // generates an array of words according to
    // a query that was originally a simple string of chars.
    public ArrayList<String> separateWords(String query)
    {
        ArrayList<String> result = new ArrayList<>();
        char c;
	String aux;
	int pos1 = 0;
	int pos2 = 0;

	for(int i = 0; i < query.length(); ++i)
	{
            c = query.charAt(i);
            // white spaces are ignored.
            if(validateChar(c) == true)
            {
                pos1 = i;
                // leaves the index on an invalid char.
                while((validateChar(c) == true) && i < query.length())
                {
                    pos2 = i;
                    ++i;
                    if(i < query.length()-1)
                    {
                        c = query.charAt(i);
                    }
                }
                if(pos2 == query.length())
                {
                    --pos2;
                }
                aux = query.substring(pos1, pos2+1);
                // saves the sub-string.
                result.add(aux);
            }		
        }
        return result;
    }
    
    // only alphanumeric substrings of the query will be taken into account.
    // verifies if the given char is alphanumeric or not.
    // true if it is alphanumeric, otherwise false.
    public boolean validateChar(char c)
    {
        boolean ans = false;
        // verifies if the character is alphanumeric or not.
        if(Character.isDigit(c) || Character.isLetter(c))
        {
            ans = true;
        }    
        return ans;
    }
    
     // checks if the word has to be ignored or not.
    // none stop-word will be processed.
    // could generate an empty array of words for being processed as a query.
    public ArrayList<String> eliminateWords(ArrayList<String> queryWords)
    {
        ArrayList<String> result = new ArrayList<>();
        String aux;
        String stopword;
	for(int i = 0; i < queryWords.size(); ++i)
	{
            boolean includeW = true;
            aux = queryWords.get(i);
            for(int j = 0; j < stopwords.size() && includeW != false; ++j)
            {
                stopword = stopwords.get(j);
                // verifies if both string of characters are equal.
                if(aux.compareToIgnoreCase(stopword) == 0)
		{
                    // this word in the query will be not taken into account.
                    includeW = false;					
		}
            }
            if(includeW == true)
            {
                result.add(aux);				
            }
	}
	return result;		
    }
    
    // looks for the post-lists for each term, 
    // then uses interyection to create the result to be shown.
    public ArrayList<String> processWords(ArrayList<String> queryWords)
    {
        ArrayList<String> postListF = new ArrayList<>();
        ArrayList<String> postList = new ArrayList<>();
        ArrayList<String> sortedList = new ArrayList<>();
        ArrayList<String> ans = new ArrayList<>();
        IndexEntry entry = new IndexEntry();
        String aux;
        
        for(int i = 0; i < queryWords.size(); ++i)
        {
            // word to analize.
            aux = queryWords.get(i);
            // get the post-list for the word.
            entry = index.getEntry(aux);
            // term was not found in the index.
            if(entry != null)
            {
                postList = toStringList(entry.getPostingsList());
                // calculates the Document Frequency.
                calculateDF(i, postList.size());
                
                if(postListF.isEmpty())
                {
                    postListF = postList;
                }
                else
                {
                    postListF = setIntersection(postListF, postList);
                }
            }
        }
        calculateIDF();
        calculateTFIDFW();
        /*for(int k = 0; k < postListF.size(); ++k)
        {
            System.out.println(postListF.get(k).getDocID());
        }*/
        sortedList = sortPostingList(queryWords, postListF);
        /* for(int k = 0; k < sortedList.size(); ++k)
        {
            System.out.println(sortedList.get(k).getDocID());
        }*/
        ans = sortedList;
        return ans;
    }
    
    // intersection between two sets of words.
    // the structure of the set model was simulated as an array.
    public ArrayList<String> setIntersection(ArrayList<String> S1, ArrayList<String> S2)
    {
        ArrayList<String> ans = new ArrayList<>();
        String word1;
        String word2;
        for(int i = 0; i < S1.size(); ++i)
        {
            word1 = S1.get(i);
            word1 = word1.trim();
            for(int j = 0; j < S2.size(); ++j)
            {
                word2 = S2.get(j);
                word2 = word2.trim();
                // element is part of the sets intersection.
                if(word1.compareToIgnoreCase(word2) == 0)
                {
                    ans.add(S1.get(i));
                    // it is not needed to end the intern cycle.
                }
            }
        }
        return ans;
    }    
    
    public ArrayList<String> sortPostingList(ArrayList<String> queryWords, ArrayList<String> documentList)
    {        
        // calculate the ranking according to a query in a specific document.
        ArrayList<Posting> postList = new ArrayList<>();
        IndexEntry entry = new IndexEntry();
        double[] rankingValues;
        String queryTerm;
        String doc;
        int docQ;
        
        docQ = documentList.size();
        if(docQ > 0)
        {
            // value of a query in a document.
            rankingValues = new double[docQ];
        }
        else
        {
            rankingValues = new double[1];
        }
        
        for(int i = 0; i < documentList.size(); ++i)
        {
            doc = documentList.get(i).trim();
            for(int j = 0; j < queryWords.size(); ++j)
            {
                // word to analize.
                queryTerm = queryWords.get(j);
                // get the post-list for the word.
                entry = index.getEntry(queryTerm);
                // term was not found in the index.
                if(entry != null)
                {
                    postList = entry.getPostingsList();              
                    for(int k = 0; k < postList.size(); ++k)
                    {
                        if(postList.get(k).getDocID().trim().compareToIgnoreCase(doc) == 0)
                        {
                            rankingValues[i] += rankingTable[j][4] * postList.get(k).geNlized();
                        }
                    }
                }
            }
        }
        // end.
        
        for(int i = 0; i < docQ;++i)
        {
            System.out.println(rankingValues[i]);
        }
        
        // position of the greatest number in the sublist.
        int gPos;
        double a;
        String d;
        for(int i = 0; i < docQ; ++i)
        {
            gPos = i;
            for(int j = i; j < docQ; ++j)
            {
                if(rankingValues[j] > rankingValues[gPos])
                {
                    gPos = j;
                }
            }
            // swap the values.
            a = rankingValues[i];
            rankingValues[i] = rankingValues[gPos];
            rankingValues[gPos] =  a;
            d = documentList.get(i);
            documentList.set(i, documentList.get(gPos));
            documentList.set(gPos, d);            
        }
        return documentList;
    }
    
    // transform a postinglist of posting objects into
    // a new list only with the id of the documents
    // of the postinglist.
    public ArrayList<String> toStringList(ArrayList<Posting> L)
    {
        ArrayList<String> ans = new ArrayList<>();
        for(int i = 0; i < L.size(); ++i)
        {
            ans.add(L.get(i).getDocID());
        }
        return ans;
    }
    
    // initialites the ranking table.
    // calculates the tf for every different term.
    // returns an array with the different terms of the query.
    // TF: Term Frequency, how many times a term is in the query.
    public ArrayList<String> calculateTF(ArrayList<String> queryWords)
    {
        ArrayList<String> difWords = new ArrayList<>();
        int difTerms = calculateDifTerms(queryWords);
        String word1;
        String word2;

        if(difTerms > 0)
        {
            // ft-raw || tf-weight || df || idf || weight.
            rankingTable = new double[difTerms][5];
        }
        
        for(int i = 0; i < queryWords.size(); ++i)
        {
            word1 = queryWords.get(i);
            if(!difWords.contains(word1))
            {
                rankingTable[difWords.size()][0] = 1.0;
                for(int j = i+1; j < queryWords.size(); ++j)
                {
                    word2 = queryWords.get(j);
                    if(word1.compareToIgnoreCase(word2) == 0)
                    {
                        rankingTable[difWords.size()][0] += 1.0;
                    }
                }
                difWords.add(word1);
            }
        }
        return difWords;
    }
    
    // counts how many different terms the query has.
    public int calculateDifTerms(ArrayList<String> queryWords)
    {
        int cont = 1;
        if(queryWords.isEmpty())
        {
            return 0;
        }
        boolean rep;
        for(int i = 0; i < queryWords.size(); ++i)
        {
            rep = false;
            for(int j = i+1; j < queryWords.size(); ++j)
            {
                if(queryWords.get(i).compareToIgnoreCase(queryWords.get(j)) == 0)
                {
                    rep = true;
                }
                if((j == queryWords.size()-1) && (rep == false))
                {
                    ++cont;
                }                
            }
        }
        return cont;
    }
    
    // calculates the TFW for every word in the query.
    // TFW: Term Frequency Weight, 
    public void calculateTFW()
    {
        for(int i = 0; i < rankingTable.length; ++i)
        {
            rankingTable[i][1] = 1 + Math.log10(rankingTable[i][0]);
        }
    }
    
    // calculates the DF for every word in the query.
    // DF: Document Frequency, in how many documents a word is.
    public void calculateDF(int pos, int s)
    {
        rankingTable[pos][2] = s;
    }
    
    // calculates the IDF for every word in the query.
    // IDF: Document Frequency, in how many documents a word is.
    public void calculateIDF()
    {
        for(int i = 0; i < rankingTable.length; ++i)
        {
            if(rankingTable[i][2] > 0)
            {
                rankingTable[i][3] = 1 + Math.log10(collectionSize/rankingTable[i][2]);
            }
        }
    }
    
    // calculates the TFIDFW for every word in the query.
    // IDF: tf-idf weight.
    public void calculateTFIDFW()
    {
        for(int i = 0; i < rankingTable.length; ++i)
        {
            rankingTable[i][4] = rankingTable[i][1] * rankingTable[i][3];
        }
    }
	
	public void printRankingTable()
    {
        for(int i = 0; i < rankingTable.length; ++i)
        {
            for(int j = 0; j < rankingTable[i].length; ++j)
            {
                System.out.print(rankingTable[i][j] + "\t");
            }
            System.out.println("");
        }
    }
}

