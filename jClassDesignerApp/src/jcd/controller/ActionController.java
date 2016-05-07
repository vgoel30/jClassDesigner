/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import maf.AppTemplate;

/**
 *
 * @author varungoel
 */
public class ActionController {

    AppTemplate app;

    DataManager dataManager;

    DiagramController diagramController;

    public ActionController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
        diagramController = new DiagramController();
    }

    public void handleResizeRightUndo(double initialWidth, ClassDiagramObject diagram) {
        diagram.getRootContainer().setPrefWidth(initialWidth);
    }

    public void handleResizeLeftUndo(double initialWidth, double initialX, ClassDiagramObject diagram) {
        diagram.getRootContainer().setPrefWidth(initialWidth);
        diagram.getRootContainer().setLayoutX(initialX);
    }

    public void handleMoveDiagramUndo(double initialPositionX, double initialPositionY, ClassDiagramObject diagram) {
        diagram.getRootContainer().setLayoutX(initialPositionX);
        diagram.getRootContainer().setLayoutY(initialPositionY);
    }

    public void handleResizeRightRedo(double finalWidth, ClassDiagramObject diagram) {
        diagram.getRootContainer().setPrefWidth(finalWidth);
    }
    
    public void handleResizeLeftRedo(double finalWidth, double finalX, ClassDiagramObject diagram) {
        diagram.getRootContainer().setPrefWidth(finalWidth);
        diagram.getRootContainer().setLayoutX(finalX);
    }

    public void handleMoveDiagramRedo(double finalPositionX, double finalPositionY, ClassDiagramObject diagram) {
        diagram.getRootContainer().setLayoutX(finalPositionX);
        diagram.getRootContainer().setLayoutY(finalPositionY);
    }

}
