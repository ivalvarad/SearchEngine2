package creardiccionario;

import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JN1
 */
public class Diccionario {
    
    private String texto=null;
    
    public Diccionario(){
        
    }
    
    public void modificarArchivo(File archivo){
       String linea = "";
       String[] temp;
       try{
          Scanner entrada = new Scanner(archivo);
          do{
             linea = entrada.nextLine();
             if(texto == null){
                 texto = linea + "\n";
             }
             else{
                 comparar(linea);
             }
          }while(entrada.hasNextLine());
          entrada.close();
          
       }catch(Exception e){}
    }
    
    private void comparar(String palabra){
        String temp = palabra + "\n";
        //revisar si la palabra ya esta en el diccionario
        String[] temp1 = texto.split("\n");
        for(int i = 0; i < temp1.length; ++i){
            if(palabra.contentEquals(temp1[i])){
                temp = "";
            }
        }
        texto += temp;
    }
    
    public void escribirArchivo(File archivo){
       try{
          PrintWriter escritura = new PrintWriter(archivo);
          escritura.print(texto);
          escritura.close();
       }catch(Exception e){}
    }
}
