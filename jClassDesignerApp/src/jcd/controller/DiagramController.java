/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public  void updateVariablesTable(ClassDiagramObject selectedClassDiagram, TableView<VariableObject> variablesTable) {
       //clear the previous values to make space for the new
       variablesTable.getItems().clear();
       
       ObservableList<VariableObject> value = FXCollections.observableArrayList();
       
       for(VariableObject variable: selectedClassDiagram.getVariables()){
           String name = variable.getName();
           String type = variable.getType();
           boolean isStatic = variable.getIsStatic();
           boolean isFinal = variable.getIsFinal();
           String access = variable.getAccess();  
           
           value.add(new VariableObject(name, type, isStatic, isFinal, access));
       }
       variablesTable.setItems(value);
       //System.out.println(variablesTable.getColumns().get(0).getCellData(0));
    }
    
}
