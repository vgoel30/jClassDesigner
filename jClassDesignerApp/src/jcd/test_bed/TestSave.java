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
        
        ArrayList<ClassDiagramObject> classDiagramObjects = new ArrayList<>();
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        ArrayList<MethodObject> methods = new ArrayList<>();
        
        VariableObject app = new VariableObject("app", "ThreadExample", false, "private");
        VariableObject counter = new VariableObject("counter", "int", false, "private");
        variables.add(app);
        variables.add(counter);
        
        MethodObject call = new MethodObject("call", false, false, new ArrayList<>(), "void", "protected");
        methods.add(call);
        
        ClassDiagramObject CounterTask = new ClassDiagramObject(methods, variables);
        
        classDiagramObjects.add(CounterTask);
        
        
        fileManager.testSaveData(classDiagramObjects,"./work/");
    }

}
