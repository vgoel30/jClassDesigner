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

    public ActionController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }
    
    public void handleResizeRightUndo(double initialWidth, ClassDiagramObject diagram){
        diagram.getRootContainer().setPrefWidth(initialWidth);
    }

    public void handleMoveDiagramUndo(double initialPositionX, double initialPositionY, ClassDiagramObject diagram) {
        System.out.println("INVOKES HERE: " + initialPositionX + "AND HERE " + initialPositionY);
        diagram.getRootContainer().setLayoutX(initialPositionX);
        diagram.getRootContainer().setLayoutY(initialPositionY);
        System.out.println("FINAL FRUTYSA: " + diagram.getRootContainer().getLayoutX() + " y:  " + diagram.getRootContainer().getLayoutY());
    }
    
   
    
}
