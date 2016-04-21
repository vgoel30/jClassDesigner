/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import jcd.file.FileManager;

/**
 *
 * @author varungoel
 */
public class TestLoad {
    
    //arraylist fo diagrams to be loaded from the filemanager
    
    public static void main(String[] args) throws IOException{
        FileManager myManager = new FileManager();
        
        myManager.testLoadData("./work/DesignSaveTest.json");
    }
    
    //public static TYPE getData1()
    //returns some particular data value found in the arraylist of diagrams
    
    //public TYPE getData2()
    //returns some particular data value found in the arraylist of diagrams
    
    //public TYPE getData3()
    //returns some particular data value found in the arraylist of diagrams
    
}
