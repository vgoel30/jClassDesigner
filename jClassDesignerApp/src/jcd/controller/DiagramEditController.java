/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import jcd.data.ClassDiagramObject;
import jcd.gui.Workspace;

/**
 *
 * @author varungoel
 */
public class DiagramEditController {
    
    public static void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        diagram.setOnMouseClicked(mouseClicked -> {
            if(Workspace.selectionActive)
                System.out.println("Do something");
        });
    }
    
}
