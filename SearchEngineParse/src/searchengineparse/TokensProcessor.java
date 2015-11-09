/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineparse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Iva
 */

public class TokensProcessor {
    
    private ArrayList<Token> tokens; 
    
    public TokensProcessor(){
        tokens = new ArrayList();
    }
    
    /*
    public void seeTokenList(){
        String text = "";
        BufferedWriter output = null;
        try {
            File file = new File("..\\output.txt");
            output = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<tokens.size();i++){
                text+="Token: "+tokens.get(i).getToken()+" | Number: "+tokens.get(i).getTokens()+" | DocID: "+tokens.get(i).getDocID();
                text+="\n";
            }
            output.write(text);
        } catch ( IOException e ) {System.out.println("ERROR: failed to create the output file.");} 
        finally {
            if ( output != null ) try {
                output.close();
            } catch (IOException ex) {System.out.println("ERROR: failed to close the file.");}
        }
    }*/
    
    //returns a string with the tokens in the format
    //term|number|docID
    public String getTokenList(){
        List<String> toOrder = new ArrayList(); 
        String text = "";
        for(int i=0;i<tokens.size();i++){
            toOrder.add(tokens.get(i).getToken()+"|"+tokens.get(i).getTokens()+"|"+tokens.get(i).getDocID());
        }
        Collections.sort(toOrder);
        for(String line : toOrder){
            text += line;
            text += "\n";
        }
        //System.out.print(text);
        return text;
    }
    
    //adds a new token found into the list of tokens
    private void addToken(String token, String docID){
        Token newToken = new Token(token, docID);
        boolean exists = false; 
        int indexWhereExists = -1;
        for(int i=0;i<tokens.size();i++){
            if(tokens.get(i).compareTo(newToken)==true){
                exists = true;
                indexWhereExists = i; 
            }
        }
        if(exists){
            tokens.get(indexWhereExists).add();
        }else{
            tokens.add(newToken);
        }
    }
    
    //returns the string representation of a file
    public String getStrFile(String path) throws FileNotFoundException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        String everything = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            everything = sb.toString();
        } catch (IOException ex) {System.out.println("ERROR: failed conversion from file to String.");}
        return everything;
    }
    
    //this method assumes EVERY TOKEN IS IN ONE LINE OF THE FILE
    public void processFile(File file){
        try {
            String docID = file.getName();
            String fileS = getStrFile(file.getAbsolutePath());
            String terms[] = fileS.split("[\\r\\n]+"); //NOT SURE IF THIS WORKS
            for(int i=0; i<terms.length;i++){
                addToken(terms[i].trim(), docID); //we are not working with composed tokens
            }
        } catch (FileNotFoundException ex) { System.out.print("ERROR: failed file processing."); }
    }
    
    public void processDir(String path){
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (!file.isDirectory())
               processFile(file);
        }
    }
    
    //this method should transform the output file into a list of postings in the format
    //term docID1 docId2 ... docIDn
    //assumes same token appears in continous lines
    public void processOutput(String output){
        String postings = "";
        String lines[] = output.split("\n");
        int initialIndex = 0;
        int finalIndex = initialIndex;
        while(finalIndex < lines.length){
            String term = lines[initialIndex].substring(0,lines[initialIndex].indexOf("|"));
            postings += term;
            int i = initialIndex+1;
            while( i < lines.length && lines[i].substring(0,lines[i].indexOf("|")).compareTo(term)==0 ){
                finalIndex++;
                i++;
            }
            for(int j = initialIndex; j <= finalIndex; j++){
                String line = lines[j];
                line = line.substring(line.indexOf("|")+1);
                //add the termFreq too
                String tf = line.substring(0, line.indexOf("|")); //leaves: term frequency
                String docID = line.substring(line.indexOf("|")+1, line.length()); //leaves: docID
                postings += " "+docID+"|"+tf;
                System.out.print(" DocID: "+docID);
            }
            //System.out.println();
            postings += "\n";
            finalIndex++;
            initialIndex = finalIndex;
        }
        
        BufferedWriter post = null;
        File file = new File("..\\postings.txt");
        try {
            post = new BufferedWriter(new FileWriter(file));
            post.write(postings);
        } catch (IOException ex) {System.out.println("ERROR: failed to write the file.");}
        if ( post != null ){
            try {
                post.close();
            } catch (IOException ex) {System.out.println("ERROR: failed to close the file.");}
        }
    }
    
    public void run(String path){
        processDir(path);
        processOutput(getTokenList());
        //getTokenList();
    }
    
}
