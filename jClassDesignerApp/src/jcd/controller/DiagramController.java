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
import jcd.connector_lines.AggregateLine;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.ExternalDataType;
import jcd.data.MethodObject;
import jcd.data.VariableObject;

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
    public void updateParentNamePicker(ClassDiagramObject selectedClassDiagram, ComboBox<String> parentNamePicker, ArrayList<ClassDiagramObject> classesOnCanvas) {
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
//        if(!selectedClassDiagram.getParentName().equals("") || selectedClassDiagram.getParentName() != null)
//            parentNamePicker.getSelectionModel().select(selectedClassDiagram.getParentName());
        parentNamePicker.setItems(potentialParents);
        parentNamePicker.getItems().add("NONE");
        parentNamePicker.getSelectionModel().select(selectedClassDiagram.getParentName());
    }

    /**
     * Updates the variable table to show the variables of the selected class
     *
     * @param selectedClassDiagram
     * @param variablesTable
     */
    public void updateVariablesTable(ClassDiagramObject selectedClassDiagram, TableView<VariableObject> variablesTable) {

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

    public void updateMethodsTable(ClassDiagramObject selectedClassDiagram, TableView<MethodObject> methodsTable) {

        //clear the previous values to make space for the new
        methodsTable.getItems().clear();

        ArrayList<ArgumentObject> arguments = new ArrayList<>();

        ObservableList<MethodObject> value = FXCollections.observableArrayList();

        for (MethodObject method : selectedClassDiagram.getMethods()) {
            String name = method.getName();
            String returnType = method.getReturnType();
            boolean isStatic = method.getIsStatic();
            boolean isAbstract = method.getIsAbstract();
            String access = method.getAccess();
            arguments = method.getArguments();
            value.add(new MethodObject(name, isStatic, isAbstract, arguments, returnType, access));
            methodsTable.setItems(value);
        }
    }

    /**
     * Renders the variables and adds to the list of variables.
     *Also renders the box if the variable is an external data type
     * @param diagram
     * @param toAdd
     */
    public void addVariable(ClassDiagramObject diagram, VariableObject toAdd, DataManager dataManager) {
        //add to the list of variables for the class
        diagram.getVariables().add(toAdd);

        //renders it on the diagram
        Label variableText = new Label(toAdd.toString());
        variableText.getStyleClass().add("diagram_text_field");
        diagram.getVariablesContainer().getChildren().add(variableText);

        variableText.toFront();

        String variableDataType = toAdd.getType();

        boolean isPrimitive = variableDataType.equals("byte") || variableDataType.equals("short")
                || variableDataType.equals("int") || variableDataType.equals("long")
                || variableDataType.equals("float") || variableDataType.equals("double")
                || variableDataType.equals("boolean") || variableDataType.equals("char");

        //if the data type is non primitive and isn't already there in the data manager
        if (!isPrimitive && !dataManager.externalDataTypes.contains(variableDataType)) {
            //make a new box
            ExternalDataType dataTypeToAdd = new ExternalDataType(variableDataType);
            //render it on the canvas
            dataTypeToAdd.putOnCanvas(dataManager.getRenderingPane());
            //add the external data type to list of external data types
            dataManager.externalDataTypes.add(variableDataType);
            //add the diagram box to the list of external data type diargams on canvas
            dataManager.externalDataTypesOnCanvas.add(dataTypeToAdd);
            //attach the event handlers for the box
            dataManager.attachExternalDataTypeBoxHandlers(dataTypeToAdd);
            //make the aggregate line object
            AggregateLine aggregateLineToAdd = new AggregateLine(diagram, dataTypeToAdd, dataManager.getRenderingPane());

            //attach the event handlers for the line
            dataManager.attachConnectorLineHandlers(aggregateLineToAdd);

            //lines emitted by the the external data type box
            dataTypeToAdd.emittedLines.add(aggregateLineToAdd);
            //the data type is used by the current selected diagram
            dataTypeToAdd.usedBy.add(diagram);
        } //non-primitive data type but already there on canvas
        else if (!isPrimitive && dataManager.externalDataTypes.contains(variableDataType)) {
            //iterate over all the external data type
            for (ExternalDataType externalDataType : dataManager.externalDataTypesOnCanvas) {
                //if the box's name matches the name that is to be added
                if (externalDataType.getName().equals(variableDataType)) {
                    //make a new line
                    AggregateLine aggregateLineToAdd = new AggregateLine(diagram, externalDataType, dataManager.getRenderingPane());

                    //attach event handlers for the line (splitting and stuff)
                    dataManager.attachConnectorLineHandlers(aggregateLineToAdd);

                    //lines emitted by the the external data type box
                    externalDataType.emittedLines.add(aggregateLineToAdd);
                    //the data type is used by the current selected diagram
                    externalDataType.usedBy.add(diagram);
                    break;
                }
            }
        }
    }

    /**
     * Removes the variable from the list and removes it from the diagram
     *
     * @param diagram
     * @param toRemove
     */
    public void removeVariable(ClassDiagramObject diagram, VariableObject toRemove) {
        VariableObject toRemoveTemp = new VariableObject();

        for (VariableObject variable : diagram.getVariables()) {
            if (variable.equals(toRemove)) {
                toRemoveTemp = variable;
                break;
            }
        }

        diagram.getVariables().remove(toRemoveTemp);

        Label labelToRemove = new Label();

        for (Node child : diagram.getVariablesContainer().getChildren()) {
            if (child instanceof Label) {
                if (((Label) child).getText().equals(toRemove.toString())) {
                    labelToRemove = (Label) child;
                    break;
                }
            }
        }
        diagram.getVariablesContainer().getChildren().remove(labelToRemove);
    }

    public void removeMethod(ClassDiagramObject diagram, MethodObject toRemove) {
        MethodObject toRemoveTemp = new MethodObject();

        for (MethodObject method : diagram.getMethods()) {
            if (method.equals(toRemove)) {
                toRemoveTemp = method;
                break;
            }
        }
        diagram.getMethods().remove(toRemoveTemp);

        Label labelToRemove = new Label();

        for (Node child : diagram.getMethodsContainer().getChildren()) {
            if (child instanceof Label) {
                if (((Label) child).getText().equals(toRemove.toString())) {
                    labelToRemove = (Label) child;
                    break;
                }
            }
        }

        diagram.getMethodsContainer().getChildren().remove(labelToRemove);
    }

    /**
     * Adds a method to the diagram and renders it
     *
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

}
