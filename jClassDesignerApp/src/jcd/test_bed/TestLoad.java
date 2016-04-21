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
    
    public static void main(String[] args) throws IOException{
        FileManager myManager = new FileManager();
        
        myManager.testLoadData("./work/DesignSaveTest.json");
    }
    
}
