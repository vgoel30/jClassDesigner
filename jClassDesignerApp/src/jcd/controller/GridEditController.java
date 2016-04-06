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
            
            if(x > canvas.getWidth() - 150)
                canvas.setMinWidth(canvas.getWidth() + 500);
            if(y > canvas.getHeight() - 300)
                canvas.setMinHeight(canvas.getHeight() + 500);

           ClassDiagramObject objectToPut = new ClassDiagramObject(canvas,x,y);


        });
    }
    
}
