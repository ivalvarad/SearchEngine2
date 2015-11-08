/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineparse;

/**
 *
 * @author Iva
 */
public class Token {
    private int quant; //saves the amount of tokens repeated in the doc
    private String docID; //refers to the docID where I'm taking the tokens from
    private String token; //it's the word/string representing the token
    
    //creates a token for a docID without any appearances
    public Token(String token, String docID){
        this.token = token;
        this.docID = docID;
        this.quant = 1;
    }
    
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }
    
    public String getDocID(){
        return docID;
    }
    public void setDocID(String docID){
        this.docID = docID;
    }
    
    // these methods add a repetition for the token
    public void add(int quant){
        this.quant+=quant;
    }
    public void add(){
        this.quant++;
    }
    
    //returns the amount of appearances
    public int getTokens(){
        return quant;
    }
    
    //these method compares 2 tokens to see if they are the same
    public boolean compareTo(Token otro){
        boolean result = false; 
        if( this.getToken().compareTo(otro.getToken())==0 && this.getDocID().compareTo(otro.getDocID())==0 ){
            result = true;
        }
        return result;
    }
    
}
