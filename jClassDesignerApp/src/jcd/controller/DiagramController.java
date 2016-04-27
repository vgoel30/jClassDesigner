/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
      System.out.println("Update variables table called");
        
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
    }

    /**
     * Renders the variables and adds to the list of variables
     * @param diagram
     * @param toAdd 
     */
    public void addVariable(ClassDiagramObject diagram, VariableObject toAdd) {
        //add to the list of variables for the class
        diagram.getVariables().add(toAdd);
        
        Label variableText = new Label(toAdd.toString());
        variableText.getStyleClass().add("diagram_text_field");
        diagram.getVariablesContainer().getChildren().add(variableText);
        
        //ariableText.getStyleClass().add("diagram_text_field");
    }

    public void removeVariable(ClassDiagramObject diagram, VariableObject toRemove) {
        VariableObject toRemoveTemp = new VariableObject();
        
        for(VariableObject variable: diagram.getVariables()){
            if(variable.equals(toRemove)){
                toRemoveTemp = variable;
                break;
            }
        }
        
        diagram.getVariables().remove(toRemoveTemp);
        
        double heightToSubtract = 0;
        
        Label labelToRemove = new Label();
        
        for(Node child: diagram.getVariablesContainer().getChildren()){
            if(child instanceof Label){
                if(((Label) child).getText().equals(toRemove.toString())){
                    labelToRemove = (Label)child;
                    heightToSubtract = ((Label) child).getHeight();
                    break;
                }
            }
        }
        diagram.getVariablesContainer().getChildren().remove(labelToRemove);
        //decreases the height of the container to compensate for the removal of the variable 
        diagram.getVariablesContainer().setPrefHeight(diagram.getVariablesContainer().getHeight()-heightToSubtract);
    }
    
}
