/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author varungoel
 */
public class DiagramController {

    public DiagramController() {

    }

    /**
     * Updates the parent name picker based on the current selected shape
     *
     * @param selectedClassDiagram
     * @param parentNamePicker
     * @param classesOnCanvas
     */
    public void updateParentNamePicker(ClassDiagramObject selectedClassDiagram, CheckComboBox<String> parentNamePicker, ArrayList<ClassDiagramObject> classesOnCanvas) {
        boolean isInterface = selectedClassDiagram.isInterface();

        parentNamePicker.getItems().clear();
        

        ObservableList<String> potentialParents = FXCollections.observableArrayList();

        if (!isInterface) {
            for (ClassDiagramObject potentialParent : classesOnCanvas) {
                if (!selectedClassDiagram.equals(potentialParent)) {
                    if (!potentialParent.isInterface()) {
                        potentialParents.add(potentialParent.toString());
                    }
                }
            }
        } else {
            for (ClassDiagramObject potentialParent : classesOnCanvas) {
                if (!selectedClassDiagram.equals(potentialParent)) {
                    potentialParents.add(potentialParent.toString());

                }
            }
        }
//       
        parentNamePicker.getItems().addAll(potentialParents);
        System.out.println("PARENTS: " + selectedClassDiagram.getParentsName());
        parentNamePicker.getCheckModel().clearChecks();
        System.out.println("ITEMS : " + parentNamePicker.getCheckModel().getCheckedIndices());
        for (String parent : selectedClassDiagram.getParentsName()) {
            parentNamePicker.getCheckModel().check(parent);
        }
    }

    /**
     * Updates the variable table to show the variables of the selected class
     *
     * @param selectedClassDiagram
     * @param variablesTable
     */
    public void updateVariablesTable(ClassDiagramObject selectedClassDiagram, TableView<VariableObject> variablesTable) {
        System.out.println("Update variables table called");

        //clear the previous values to make space for the new
        variablesTable.getItems().clear();

        ObservableList<VariableObject> value = FXCollections.observableArrayList();

        for (VariableObject variable : selectedClassDiagram.getVariables()) {
            String name = variable.getName();
            String type = variable.getType();
            boolean isStatic = variable.getIsStatic();
            boolean isFinal = variable.getIsFinal();
            String access = variable.getAccess();

            value.add(new VariableObject(name, type, isStatic, isFinal, access));
        }
        variablesTable.setItems(value);
    }
    
    public void updateMethodsTable(ClassDiagramObject selectedClassDiagram, TableView<MethodObject> methodsTable){
        System.out.println("Update methods table called");
        
        //clear the previous values to make space for the new
        methodsTable.getItems().clear();
        
        ArrayList<ArgumentObject> arguments = new ArrayList<>();
        
        ObservableList<MethodObject> value = FXCollections.observableArrayList();
        
        for(MethodObject method: selectedClassDiagram.getMethods()){
            String name = method.getName();
            String returnType = method.getReturnType();
            boolean isStatic = method.getIsStatic();
            boolean isAbstract = method.getIsAbstract();
            String access = method.getAccess();
             
            value.add(new MethodObject(name, isStatic, isAbstract, arguments, returnType, access));
        }
        System.out.println(value);
        methodsTable.setItems(value);
    }

    /**
     * Renders the variables and adds to the list of variables
     *
     * @param diagram
     * @param toAdd
     */
    public void addVariable(ClassDiagramObject diagram, VariableObject toAdd) {
        //add to the list of variables for the class
        diagram.getVariables().add(toAdd);

        Label variableText = new Label(toAdd.toString());
        variableText.getStyleClass().add("diagram_text_field");
        diagram.getVariablesContainer().getChildren().add(variableText);
        variableText.toFront();
    }
    
    /**
     * Adds a method to the diagram and renders it
     * @param diagram
     * @param toAdd 
     */
    public void addMethod(ClassDiagramObject diagram, MethodObject toAdd) {
        //add to the list of methods for the class
       diagram.getMethods().add(toAdd);

        Label variableText = new Label(toAdd.toString());
        variableText.getStyleClass().add("diagram_text_field");
        diagram.getMethodsContainer().getChildren().add(variableText);
        variableText.toFront();
    }

    public void removeVariable(ClassDiagramObject diagram, VariableObject toRemove) {
        VariableObject toRemoveTemp = new VariableObject();

        for (VariableObject variable : diagram.getVariables()) {
            if (variable.equals(toRemove)) {
                toRemoveTemp = variable;
                break;
            }
        }

        diagram.getVariables().remove(toRemoveTemp);

        double heightToSubtract = 0;

        Label labelToRemove = new Label();

        for (Node child : diagram.getVariablesContainer().getChildren()) {
            if (child instanceof Label) {
                if (((Label) child).getText().equals(toRemove.toString())) {
                    labelToRemove = (Label) child;
                    heightToSubtract = ((Label) child).getHeight();
                    break;
                }
            }
        }
        diagram.getVariablesContainer().getChildren().remove(labelToRemove);
        //decreases the height of the container to compensate for the removal of the variable 
        diagram.getVariablesContainer().setPrefHeight(diagram.getVariablesContainer().getHeight() - heightToSubtract);
    }

}
