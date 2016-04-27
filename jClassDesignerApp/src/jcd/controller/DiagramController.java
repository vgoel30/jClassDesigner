/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
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

    /**
     * Renders the variables and adds to the list of variables
     * @param diagram
     * @param toAdd 
     */
    public void addVariable(ClassDiagramObject diagram, VariableObject toAdd) {
        //add to the list of variables for the class
        diagram.getVariables().add(toAdd);
        
        Text variableText = new Text(toAdd.toString());
        diagram.getVariablesContainer().getChildren().add(variableText);
        
        variableText.getStyleClass().add("diagram_text_field");
    }
    
}
