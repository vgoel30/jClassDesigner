/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import static jcd.controller.GridEditController.selectedClassDiagram;
import static jcd.controller.GridEditController.classNames;
import static jcd.controller.GridEditController.packageNames;
import static jcd.controller.GridEditController.classPackageCombos;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.gui.Workspace;
import maf.AppTemplate;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class DiagramEditController {

    AppTemplate app;

    DataManager dataManager;

    public DiagramEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }

    /**
     * Restores the appearance of the selected diagram after it has been
     * deselected
     *
     * @param selectedClassDiagram
     */
    public void restoreSelectedProperties(ClassDiagramObject selectedClassDiagram) {
        selectedClassDiagram.getRootContainer().getStyleClass().remove(SELECTED_DIAGRAM_CONTAINER);
    }

    public void changeClassName(String oldValue, String newValue) {
        selectedClassDiagram.getClassNameText().setText(newValue);

    }

    public void doFancyNameShitForClass(String oldValue, String newValue, String classPackageName) {
        classPackageCombos.remove(oldValue + ":" + classPackageName);
        classPackageCombos.add(newValue + ":" + classPackageName);
        System.out.println(classPackageCombos);
    }

    /**
     * Validates the name to see if it already exists
     *
     * @param newName
     * @param classNameField
     * @param oldValue
     */
    public void validateClassName(String newName, TextField classNameField, String oldValue, String classPackageName) {
        String theClassName = classPackageCombos.get(classPackageCombos.size() - 1).split(":")[0];
        //classPackageCombos.remove(classPackageCombos.size() - 1)   
        classPackageCombos.remove(oldValue + ":" + classPackageName);

        if (classPackageCombos.contains(theClassName + ":" + classPackageName) && (classPackageCombos.indexOf(theClassName + ":" + classPackageName) != classPackageCombos.size() - 1)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Class name error");
            alert.setHeaderText(null);
            alert.setContentText("Class already exists in this package!");
            alert.showAndWait();
            selectedClassDiagram.getClassNameText().setText("Class Name");
            classNameField.setText("Class Name");
        } else {
            selectedClassDiagram.getClassNameText().setText(newName);
            classPackageCombos.add(newName + ":" + classPackageName);
        }
    }

    public void doFancyNameShitForPackage(String oldValue, String newValue, String className) {
        classPackageCombos.remove(className + ":" + oldValue);
        classPackageCombos.add(className + ":" + newValue);
        System.out.println(classPackageCombos);
    }
    
    public void validatePackageName(String newPackageName, TextField packageNameField, String oldValue, String className){
        String thePackageName = classPackageCombos.get(classPackageCombos.size() - 1).split(":")[1];
        classPackageCombos.remove(className + ":" + oldValue);
        
        if (classPackageCombos.contains(className + ":" + thePackageName) && (classPackageCombos.indexOf(className + ":" + thePackageName) != classPackageCombos.size() - 1)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Package name error");
            alert.setHeaderText(null);
            alert.setContentText("Package with this class already exists");
            alert.showAndWait();
            selectedClassDiagram.getClassNameText().setText("Class Name");
            packageNameField.setText("Package Name");
        }
        else {
            selectedClassDiagram.getPackageNameText().setText(newPackageName);
            classPackageCombos.add(className + ":" + newPackageName);
        }

    }
    
    

    public void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        diagram.getRootContainer().setOnMouseClicked(mouseClicked -> {
            if (workspace.selectionActive) {
                System.out.println("Clicked on class diagram object");
                diagram.getRootContainer().getStyleClass().add(SELECTED_DIAGRAM_CONTAINER);

                if (selectedClassDiagram != null) {
                    restoreSelectedProperties(selectedClassDiagram);
                }
                selectedClassDiagram = diagram;
                //reflect the selected changes
                workspace.classNameField.setText(diagram.getClassNameText().getText());
                workspace.packageNameField.setText(diagram.getPackageNameText().getText());

                workspace.disableButtons(false);
            }
        });

        //FOR MOVING THE diagram
        diagram.getRootContainer().setOnMouseDragged(rectangleDraggedEvent -> {

            if (selectedClassDiagram != null) {
                if (selectedClassDiagram.equals(diagram) && workspace.selectionActive) {
                    workspace.drawingActive = false;
                    diagram.getRootContainer().setLayoutY(rectangleDraggedEvent.getSceneY() - 50);
                    diagram.getRootContainer().setLayoutX(rectangleDraggedEvent.getSceneX() - 400);

                }
            }
        });
    }

}
