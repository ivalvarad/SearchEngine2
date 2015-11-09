/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;
import java.util.ArrayList;
/**
 *
 * @author Iva
 */

//has to be an ArrayList of ArrayList<IndexEntry>;
public class Index {
    
    private int nEntries;
    private ArrayList<ArrayList<IndexEntry>> table;
    
    public Index(int nEntries){
        this.nEntries = nEntries;
        this.table = new ArrayList<>(nEntries);
        for(int i = 0; i < nEntries; i++){
            table.add(new ArrayList<>());
        }
    }
    
    // applies the hash function to a term
    public int funHash(String term){
        char ch[] = term.toCharArray();
        int i, sum;
        for(sum=0, i=0; i < term.length(); i++){
          sum += ch[i];
        }
        return sum % nEntries;
    }
    
    //inserts a new IndexEntry in the Index
    public void insert(IndexEntry entry){
        int i = funHash(entry.getTerm());
        table.get(i).add(entry);
    }
    
    //inserts a new IndexEntry with term in the Index
    public void insert(String term){
        int i = funHash(term); 
        table.get(i).add(new IndexEntry(term));
    }
    
    //returns the object IndexEntry with the term "term"
    public IndexEntry getEntry(String term){
        IndexEntry index = null;
        for(int i=0; i< table.size(); i++){
            for(int j = 0; j < table.get(i).size(); j++){
                if( table.get(i).get(j).getTerm().compareTo(term)==0 )
                    index = table.get(i).get(j);
            }
        }
        return index;
    }
    
    //return the index where the object IndexEntry with the term "term" is stored at
    public ArrayList getIndex(String term){
        ArrayList index = new ArrayList(2);
        for(int i=0; i< table.size(); i++){
            for(int j = 0; i < table.get(i).size(); j++){
                if(table.get(i).get(j).getTerm().compareTo(term)==0 ){
                    index.add(0, i); //index in table
                    index.add(1, j); //index in table entry
                }
            }
        }
        return index;
    }
    
    //associated a docID with a term
    public void associate(String term, String docID, String termFreq){
        ArrayList<Integer> idx = getIndex(term);
        table.get(idx.get(0)).get(idx.get(1)).addDocument(docID, termFreq);
    }
    
    //returns true if the docID is already associated to the term, false otherwise
    public boolean isAssociated(String term, String docID){
        ArrayList<Integer> idx = getIndex(term);
        boolean result = false;
        if(table.get(idx.get(0)).get(idx.get(1)).hasDocument(docID)==true){
            result = true;
        }
        return result;
    }
    
    //returns a formatted String of this Index
    @Override
    public String toString(){
        String result = "";
        for(int i=0; i < table.size(); i++){
            result += "===> Index"+i+"\n";
            if(table.get(i)!=null){
                for(int j = 0; j < table.get(i).size(); j++){
                     if(table.get(i).get(j)!=null)
                        result += "\t"+table.get(i).get(j).toString()+"\n";
                }
            }
        }
        return result; 
    }
}
