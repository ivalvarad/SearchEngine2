/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creardiccionario;

import java.io.File;

/**
 *
 * @author JN1
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File temp = new File("");
        String ruta = temp.getAbsolutePath().replace("\\", "/");
        String[] temp1 = ruta.split("/");
        ruta = "";
        for(int i = 0; i < temp1.length-1; ++i){
            ruta += temp1[i] + "/";
        }
        Diccionario diccionario = new Diccionario();
        File[] archivos = new File(ruta+"dirTokens").listFiles();
        for(int i = 0; i < archivos.length; ++i){
            diccionario.modificarArchivo(archivos[i]);
        }
        diccionario.escribirArchivo(new File(ruta + "diccionario.txt"));
    }
    
}
