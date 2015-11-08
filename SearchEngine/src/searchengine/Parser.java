/*
 * This class is on charge of manipulate stream from a file 
 */
package searchengine;

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
public class Parser {
    
    private String path;
    
    public Parser(String path){
        this.path = path;
    }
    
    public String getStrFile() throws FileNotFoundException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        String everything = "";
        try {
            String line = br.readLine();
            while (line != null) {
                everything += line + "\n";
                line = br.readLine();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.print(everything);
        return everything;
    }
    
    public int getNumberLines() throws FileNotFoundException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        int lines = 0; 
        try {
            while (br.readLine() != null) {
                lines += 1;
            }
            br.close();
        } catch (IOException ex) {}
        return lines;
    }
    
}
