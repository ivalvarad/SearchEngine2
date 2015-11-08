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
public class IndexEntry {
    String term;
    int frequency; //# of documents the terms appears on
    ArrayList<String> postingsList;
    
    public IndexEntry(String term){
        this.term = term;
        this.frequency = -1;
        this.postingsList = new ArrayList<>();
    }
    
    public IndexEntry(){
        this.term = "";
        this.frequency = -1;
        this.postingsList = new ArrayList<>();
    }
    
    public void setTerm(String term){
        this.term = term;
    }
    
    public String getTerm(){
        return this.term;
    }
    
    public void addDocument(String docID){
        postingsList.add(docID);
    }
    
    public String getDocument(int index){
        return postingsList.get(index);
    }
    
    public boolean hasDocument(String docID){
        boolean result = false;
        for(int i = 0; i < postingsList.size(); i++){
            if( postingsList.get(i).compareTo(docID)==0 ){
                result = true;
            }
        }
        return result;
    }
    
    public void incrementFrecuency(){
        frequency++;
    }
    
    public int getFrecuency(){
        return frequency;
    }
    
    public ArrayList getPostingsList(){
        return postingsList;
    }
    
    @Override
    public String toString(){
        String result = "";
        result += "Term: "+term+", "+"Documents: ";
        if(postingsList.size()>0){
            result += postingsList.get(0);
        }
        if(postingsList.size()>1){
            for(int i = 1; i < postingsList.size(); i++){
                result += " | "+ postingsList.get(i);
            }
        }
        return result;
    }
    
}
