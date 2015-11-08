/*
 * The purpose of this class is to control the other ones and organize things in order to them
 * to work properly.
 */
package searchengine;

/**
 *
 * @author Iva
 */
public class Controller {
    
    private static SearchEngine mySearchEngine; //Interface of the program
    private static Interface myInterface; //The entity that represents all the other parts unified
    
    public Controller(){
        mySearchEngine = new SearchEngine();
        myInterface = new Interface(mySearchEngine);
    }
    
    public void run(){
        myInterface.setVisible(true);
    }
    
    public static void main(String args[]){
        Controller theController = new Controller();
        theController.run();
    }
}
