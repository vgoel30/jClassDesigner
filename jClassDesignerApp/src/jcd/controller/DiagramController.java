/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.control.TableView;
import jcd.data.ClassDiagramObject;
import jcd.data.VariableObject;

/**
 *
 * @author varungoel
 */
public class DiagramController {
    
    public DiagramController(){
        
    }

    public void updateVariablesTable(ClassDiagramObject selectedClassDiagram, TableView<VariableObject> variablesTable) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       for(VariableObject variable: selectedClassDiagram.getVariables()){
           String name = variable.getName();
           String type = variable.getType();
           boolean isStatic = variable.getIsStatic();
           boolean isFinal = variable.getIsFinal();
           String access = variable.getAccess();    
       }
       
      // variablesTable.setI
       //System.out.println(variablesTable.getColumns().get(0).getCellData(0));
    }
    
}
