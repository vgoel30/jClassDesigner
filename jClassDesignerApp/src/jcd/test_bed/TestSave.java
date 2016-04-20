/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import jcd.file.FileManager;

/**
 *
 * @author varungoel
 */
public class TestSave {
   
    public static void main(String[] args) throws FileNotFoundException{
        FileManager fileManager = new FileManager();
        
        //store all the classes
        ArrayList<ClassDiagramObject> classDiagramObjects = new ArrayList<>();
        
        //hard coding the first class
        ArrayList<VariableObject> variables1 = new ArrayList<>();
        ArrayList<MethodObject> methods1 = new ArrayList<>();
        
        VariableObject app = new VariableObject("app", "ThreadExample", false, "private");
        VariableObject counter = new VariableObject("counter", "int", false, "private");
        variables1.add(app);
        variables1.add(counter);
        
        MethodObject call = new MethodObject("call", false, false, new ArrayList<>(), "void", "protected");
        methods1.add(call);
        
        ClassDiagramObject CounterTask = new ClassDiagramObject("CounterTask","class", methods1, variables1);
        CounterTask.setClassNameText("CounterTask"); 
        classDiagramObjects.add(CounterTask);
        //the first class is done. Onto the second one.
        
        //hard coding the second class (DateTask.java)
        ArrayList<VariableObject> variables2 = new ArrayList<>();
        ArrayList<MethodObject> methods2 = new ArrayList<>();
        
        VariableObject dateTaskApp = new VariableObject("app", "ThreadExample", false, "private");
        VariableObject now = new VariableObject("now", "Date", false, "private");
        variables2.add(dateTaskApp);
        variables2.add(now);
        
        MethodObject callDateTask = new MethodObject("call", false, false, new ArrayList<>(), "void", "protected");
        methods2.add(call);
        
        ClassDiagramObject DateTask = new ClassDiagramObject("DateTask","class", methods2, variables2);
        DateTask.setClassNameText("DateTask"); 
        classDiagramObjects.add(DateTask);
        //second class is done
        
        fileManager.testSaveData(classDiagramObjects,"./work/DesignSaveTest.json");
    }

}
