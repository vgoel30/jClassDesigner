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
            DiagramEditController.restoreSelectedProperties(selectedClassDiagram);
            selectedClassDiagram = null;
            Workspace.disableButtons(true);
        }

        canvas.setOnMouseClicked(e -> {
            if (Workspace.drawingActive) {
                double x = e.getX();
                double y = e.getY();

                //dynamic scrolling 
                if (x > canvas.getWidth() - 150) {
                    canvas.setMinWidth(canvas.getWidth() + 500);
                }
                if (y > canvas.getHeight() - 300) {
                    canvas.setMinHeight(canvas.getHeight() + 500);
                }

                ClassDiagramObject objectToPut = new ClassDiagramObject(canvas, x, y);
                DiagramEditController.attachClassDiagramEventHandlers(objectToPut);
                Workspace.disableButtons(true);
            }

        });

    }

}
