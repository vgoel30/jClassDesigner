/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcd.test_bed.TestLoad;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jcd.file.FileManager;
import jcd.test_bed.TestSave;
/**
 *
 * @author varungoel
 */
public class LoadTest {
    FileManager testFileManager;
    public LoadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            TestSave.main(null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   
    /**
     * Test of main method, of class TestLoad.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        TestLoad.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //assert 10 different things here
        
    }
    
    @Test
    public void testGetData1(){
        assertEquals("- app : ThreadExample", TestLoad.getData1());
    }
    
    @Test
    public void testGetData2(){
        assertEquals("class", TestLoad.getData2());
    }
    
    @Test
    public void testGetData3(){
        assertEquals(0, TestLoad.getData3(), 0);
    }
    
    @Test
    public void testGetData4(){
        assertEquals(0, TestLoad.getData4(), 0);
    }
    
    @Test
    public void testGetData5(){
        assertEquals(300, TestLoad.getData5(), 0);
    }
    
    @Test
    public void testGetData6(){
        assertEquals("- appPane : BorderPane", TestLoad.getData6());
    }
    
    @Test
    public void testGetData7(){
        assertEquals(2, TestLoad.getData7());
    }
    
    @Test
    public void testGetData8(){
        assertEquals(1, TestLoad.getData8());
    }
    
    @Test
    public void testGetData9(){
        assertEquals("- app : ThreadExample", TestLoad.getData9());
    }
}
