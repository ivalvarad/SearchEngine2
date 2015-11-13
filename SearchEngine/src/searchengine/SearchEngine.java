/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iva
 */
public class SearchEngine {
    private Index myIndex; //a esto hay que pasarle la cantidad de términos del diccionario
    private Parser myParser;
    private QueryProcessor myQP;
    
     /*
       The Interface is going to call this method once the search button is clicked, so here
       is where all the things are done.
       Then, the interface is going to take the ArrayList this method returns and is going to show
       the output based on that. 
    */
    
    public SearchEngine(){
        myParser = new Parser("..\\postings.txt");
        myIndex = new Index(100);
        /*try {
            myIndex = new Index(myParser.getNumberLines()); //a esto hay que pasarle la cantidad de términos del diccionario
        } catch (FileNotFoundException ex) {}*/
        myQP = new QueryProcessor(myIndex);
        buildIndex();
    }
    
    //builds the index
    public void buildIndex(){
        String postings = "";
        try{
            postings = myParser.getStrFile();
        }catch(FileNotFoundException ex){System.out.println("ERROR: failed to convert the file.");}
        String lines[] = postings.split("\n"); //array with a line in each field
        for(int i = 0; i < lines.length; ++i){
            String line = lines[i];
            int blankIdx = line.indexOf(' '); //index of the first blank
            String term = line.substring(0, line.indexOf(' ')); //cut from beginning to the first blank
            IndexEntry newEntry = new IndexEntry(term); //create a new IndexEntry containing the term
            line = line.substring(blankIdx+1); //remove the term from the line
            String parts[] = line.split(" ");
            for(String part : parts){
                String docID = part.substring(0, part.indexOf("|"));
                String termFreq = part.substring(part.indexOf("|")+1, part.length()); ///////////////
                newEntry.addDocument(docID,termFreq);
            }
            myIndex.insert(newEntry);
        }
        //System.out.println(myIndex.toString());
    }
    
    public void processQuery(String query)
    //public ArrayList<String> processQuery(String query)
    {
        // System.out.println(myIndex.toString());
        //return myQP.processQuery(query);
        buildIndex();
        System.out.println(myIndex.toString());
    }
}
