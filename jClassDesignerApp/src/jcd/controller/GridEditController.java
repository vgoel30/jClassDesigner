/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import jcd.data.ClassDiagramObject;
import jcd.gui.Workspace;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class GridEditController {

    //THE CURRENT SELECTED CLASS DIAGRAM
    static ClassDiagramObject selectedClassDiagram = null;
    //THE CURRENT SELECTED INTERFACE DIAGRAM

    public static void addClassDiagram(Pane canvas) {
        if (selectedClassDiagram != null) {
            restoreSelectedProperties();
            selectedClassDiagram = null;
            //disable the appropriate buttons
            Workspace.changeControlsAbility(true);
        }

        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            
            if (Workspace.drawingActive) {
                //dynamic scrolling 
                if (x > canvas.getWidth() - 150) {
                    canvas.setMinWidth(canvas.getWidth() + 500);
                }
                if (y > canvas.getHeight() - 300) {
                    canvas.setMinHeight(canvas.getHeight() + 500);
                }

                ClassDiagramObject objectToPut = new ClassDiagramObject(canvas, x, y);
                DiagramEditController.attachClassDiagramEventHandlers(objectToPut);
            }

        });

    }

    /**
     * Restores the appearance of the selected button after it has been
     * deselected
     */
    public static void restoreSelectedProperties() {
        if (selectedClassDiagram != null) {
            //remove the highlighting effect
            selectedClassDiagram.getRootContainer().getStyleClass().remove(SELECTED_DIAGRAM_CONTAINER);
        }
    }

}
