/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import static jcd.controller.GridEditController.classPackageCombos;
import static jcd.controller.GridEditController.selectedClassDiagram;
import jcd.gui.Workspace;
import maf.AppTemplate;
import maf.components.AppDataComponent;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class DataManager implements AppDataComponent{
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    //this will keep track of all the classes currently on the canvas
    public ArrayList<ClassDiagramObject> classesOnCanvas = new ArrayList<>();
    //THE CURRENT SELECTED CLASS DIAGRAM
   public static ClassDiagramObject selectedClassDiagram = null;
    public static  ArrayList<String> classNames = new ArrayList<>();
    public static ArrayList<String> packageNames = new ArrayList<>();
   public static ArrayList<String> classPackageCombos = new ArrayList<>();
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }
    
    public void addClassDiagram(ClassDiagramObject diagramToAdd){
        classesOnCanvas.add(diagramToAdd);
       // ((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(diagramToAdd.getRootContainer());
        //System.out.println(((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren());
    }
    
    public Pane getRenderingPane(){
        return ((Workspace)app.getWorkspaceComponent()).getCanvas();
    }
    
    public void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        diagram.getRootContainer().setOnMouseClicked(mouseClicked -> {
            if (workspace.selectionActive) {
                System.out.println("Layout X:" + diagram.getRootContainer().getLayoutX());
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
        //System.out.println(classPackageCombos);
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
        //System.out.println(classPackageCombos);
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
            selectedClassDiagram.getPackageNameText().setText("");
            packageNameField.setText("");
        }
        else {
            selectedClassDiagram.getPackageNameText().setText(newPackageName);
            classPackageCombos.add(className + ":" + newPackageName);
        }

    }

    @Override
    public void reset() {
        //remove all the children
        classesOnCanvas.clear();
        ((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
}
