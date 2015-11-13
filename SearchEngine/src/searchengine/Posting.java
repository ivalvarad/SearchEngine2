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
    //private String tf;
    //private double wtf;
    //private double weight;
    private double nlized;
    
    public Posting(String docID, String nlized){
        this.docID = docID;
        try{
            this.nlized = Double.parseDouble(nlized);
        }catch (Exception e){System.out.println("Imposible convertir de String a double.");};
    }
    
    public void setDocID(String docID){
        this.docID = docID;
    }
    public void setNlized(String nlized){
       try{
            this.nlized = Double.parseDouble(nlized);
        }catch (Exception e){System.out.println("Imposible convertir de String a double.");};
    }
    public String getDocID(){
        return docID;
    }
    public double geNlized(){
        return nlized;
    }
}
