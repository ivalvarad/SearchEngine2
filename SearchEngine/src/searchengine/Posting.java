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
    private String tf;
    private double wtf;
    private double weight;
    private double nlized;
    
    public Posting(String docID, String termFreq){
        this.docID = docID;
        this.tf= termFreq;
    }
    public void setDocID(String docID){
        this.docID = docID;
    }
    public void setTf(String termFreq){
        this.tf = termFreq;
    }
    public String getDocID(){
        return docID;
    }
    public String getTf(){
        return tf;
    }
    
    public void calculateWtf(){
        int tfInt = Integer.parseInt(tf.trim());
        if(tfInt>0){wtf = 1+Math.log10(wtf);}else{wtf =0;}
        // 1+log10(tf) if tf > 0
        // 0 otherwise
    }
    
    public void calculateWeight(double idf){
        weight =  wtf*idf;
    }
    
    public double getWeight(){
        return weight;
    }
     
    public double getWtf(){
        return wtf;
    }
}
