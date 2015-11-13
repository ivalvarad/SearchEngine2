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
    private String term;
    private int df; //# of documents the terms appears on
    private double  idf; //# of documents the terms appears on
    //ArrayList<String> postingsList;
    ArrayList<Posting> postingsList;
    
    public IndexEntry(String term){
        this.term = term;
        this.df = -1;
        this.postingsList = new ArrayList<>();
    }
    
    public IndexEntry(){
        this.term = "";
        this.df = -1;
        this.postingsList = new ArrayList<>();
    }
    
    public void setTerm(String term){
        this.term = term;
    }
    
    public String getTerm(){
        return this.term;
    }
    
    public void addDocument(String docID, String nlized){
        //postingsList.add(docID);
        postingsList.add(new Posting(docID,nlized));
        incrementDf();
    }
    
    /*public String getDocument(int index){
        return postingsList.get(index);
    }*/
    
    public Posting getDocument(int index){
        return postingsList.get(index);
    }
    
    public boolean hasDocument(String docID){
        boolean result = false;
        for(int i = 0; i < postingsList.size(); i++){
            if( postingsList.get(i).getDocID().compareTo(docID)==0 ){
                result = true;
            }
        }
        return result;
    }
    
    public void incrementDf(){
        df++;
        //calculateIdf();
    }
    
    public int getDf(){
        return df;
    }
    
    public void calculateIdf(){ //(int N){
        idf = Math.log10(50/df); //CHANGE N = #docs in the collection!!
        // log10(N/tf)
    }
    
    public double getIdf(){
        return idf;
    }
    
    public ArrayList getPostingsList(){
        return postingsList;
    }
    
    @Override
    public String toString(){
        String result = "";
        result += "Term: "+term+", "+"docFrequency: "+getDf()+", "+"Documents: ";
        if(postingsList.size()>0){
            result += postingsList.get(0).getDocID()+"||"+postingsList.get(0).geNlized();;
        }
        if(postingsList.size()>1){
            for(int i = 1; i < postingsList.size(); i++){
                result += " | "+ postingsList.get(i).getDocID()+"||"+postingsList.get(i).geNlized();
            }
        }
        System.out.println(result);
        return result;
    }
    
}
