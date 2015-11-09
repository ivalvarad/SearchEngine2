/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

/**
 *
 * @author Iva
 */
public class Posting {
    private String docID;
    private String termFreq;
    public Posting(String docID, String termFreq){
        this.docID = docID;
        this.termFreq = termFreq;
    }
    public void setDocID(String docID){
        this.docID = docID;
    }
    public void setTermFreq(String termFreq){
        this.termFreq = termFreq;
    }
    public String getDocID(){
        return docID;
    }
    public String getTermFreq(){
        return termFreq;
    }
}
