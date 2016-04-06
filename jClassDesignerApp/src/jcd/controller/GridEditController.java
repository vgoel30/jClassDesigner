/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.layout.Pane;
import jcd.data.ClassDiagramObject;

/**
 *
 * @author varungoel
 */
public class GridEditController {

    public static void addClassDiagram(Pane canvas) {
        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            
            System.out.println(canvas.getWidth());

           ClassDiagramObject objectToPut = new ClassDiagramObject(canvas,x,y);


        });
    }
    
}
