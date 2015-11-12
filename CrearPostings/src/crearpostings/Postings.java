/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crearpostings;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author JN1
 */
public class Postings {
    private String[] diccionario;
    private String[] postings;
    
    public Postings(String ruta){
        File archivo = new File(ruta+"diccionario.txt");
        String linea = "";
        String temp = "";
        try{
          Scanner entrada = new Scanner(archivo);
          do{
             linea = entrada.nextLine();
             temp += linea + " ";
          }while(entrada.hasNextLine());
          entrada.close();
        }catch(Exception e){}
        diccionario = temp.split(" ");
        postings = new String[diccionario.length];
        ordenar();
        for(int i = 0; i < postings.length; ++i){
            postings[i] = "";
        }
        
    }
    
    private void ordenar(){
        quickSort(diccionario, 0, diccionario.length-1);
    }
    
    public void modificarArchivo(File archivo){
       String linea = "";
       String[] temp;
       try{
          Scanner entrada = new Scanner(archivo);
          do{
             linea = entrada.nextLine();
             agregarPosting(linea, archivo.getName());
          }while(entrada.hasNextLine());
          entrada.close();
       }catch(Exception e){}
    }
    
    private void agregarPosting(String palabra, String id){
        //compara la palabra con el diccionario para agregarla a la lista de postings
        for(int i = 0; i < diccionario.length; ++i){
            if(palabra.contentEquals(diccionario[i])){
                String[] temp = postings[i].split(" ");
                if(!id.contentEquals(temp[temp.length-1])){
                    postings[i] += id+" ";
                }
            }
        }
    }
    
    public void escribirArchivo(File archivo){
       try{
          PrintWriter escritura = new PrintWriter(archivo);
          //escribir lista de postings
          for(int i= 0; i < diccionario.length; ++i){
              escritura.println(diccionario[i]+ " " + postings[i]);
          }
          escritura.close();
       }catch(Exception e){}
    }
    
    //codigo del quicksort tomado de stackoverflow.com/questions/29294982/using-quicksort-on-a-string-array
    private static void quickSort(String[] a, int start, int end)
{
        // index for the "left-to-right scan"
        int i = start;
        // index for the "right-to-left scan"
        int j = end;

        // only examine arrays of 2 or more elements.
        if (j - i >= 1)
        {
            // The pivot point of the sort method is arbitrarily set to the first element int the array.
            String pivot = a[i];
            // only scan between the two indexes, until they meet.
            while (j > i)
            {
                // from the left, if the current element is lexicographically less than the (original)
                // first element in the String array, move on. Stop advancing the counter when we reach
                // the right or an element that is lexicographically greater than the pivot String.
                while (a[i].compareTo(pivot) <= 0 && i < end && j > i){
                    i++;
                }
                // from the right, if the current element is lexicographically greater than the (original)
                // first element in the String array, move on. Stop advancing the counter when we reach
                // the left or an element that is lexicographically less than the pivot String.
                while (a[j].compareTo(pivot) >= 0 && j > start && j >= i){
                    j--;
                }
                // check the two elements in the center, the last comparison before the scans cross.
                if (j > i)
                    swap(a, i, j);
            }
            // At this point, the two scans have crossed each other in the center of the array and stop.
            // The left partition and right partition contain the right groups of numbers but are not
            // sorted themselves. The following recursive code sorts the left and right partitions.

            // Swap the pivot point with the last element of the left partition.
            swap(a, start, j);
            // sort left partition
            quickSort(a, start, j - 1);
            // sort right partition
            quickSort(a, j + 1, end);
        }
    }
    /**
     * This method facilitates the quickSort method's need to swap two elements, Towers of Hanoi style.
     */
    private static void swap(String[] a, int i, int j)
    {
    String temp = a[i];
    a[i] = a[j];
    a[j] = temp;
    }
}
