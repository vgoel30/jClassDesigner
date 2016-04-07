/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import static jcd.controller.GridEditController.selectedClassDiagram;
import jcd.data.ClassDiagramObject;
import jcd.gui.Workspace;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINER;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class DiagramEditController {

    /**
     * Restores the appearance of the selected diagram after it has been
     * deselected
     *
     * @param selectedClassDiagram
     */
    public static void restoreSelectedProperties(ClassDiagramObject selectedClassDiagram) {
        selectedClassDiagram.getRootContainer().getStyleClass().remove(SELECTED_DIAGRAM_CONTAINER);
    }
    
    public static void changeClassName(String oldValue, String newValue){
        if (Workspace.selectionActive) {
            if (selectedClassDiagram != null) {
                    selectedClassDiagram.getClassNameText().setText(newValue);
                }
        }
    }

    public static void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        diagram.getRootContainer().setOnMouseClicked(mouseClicked -> {
            if (Workspace.selectionActive) {
                System.out.println("Clicked on class diagram object");
                diagram.getRootContainer().getStyleClass().add(SELECTED_DIAGRAM_CONTAINER);

                if (selectedClassDiagram != null) {
                    restoreSelectedProperties(selectedClassDiagram);
                }
                selectedClassDiagram = diagram;
                //reflect the selected changes
                Workspace.classNameField.setText(diagram.getClassNameText().getText());

                Workspace.disableButtons(false);
            }
        });

        //FOR MOVING THE diagram
        diagram.getRootContainer().setOnMouseDragged(rectangleDraggedEvent -> {
            if (selectedClassDiagram != null) {
                if (selectedClassDiagram.equals(diagram) && Workspace.selectionActive) {
                    Workspace.drawingActive = false;
                    diagram.getRootContainer().setLayoutY(rectangleDraggedEvent.getSceneY()-50);
                    diagram.getRootContainer().setLayoutX(rectangleDraggedEvent.getSceneX()-400);
                            
                }
            }
        });
    }

}
