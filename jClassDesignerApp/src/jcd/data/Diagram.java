/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.VBox;

/**
 *
 * @author varungoel
 */
public abstract class Diagram {
    VBox rootContainer;
    String name;

    public VBox getRootContainer() {
        return rootContainer;
    }
    
    public String getName(){
        return name;
    }
    
    public double getX() {
        return rootContainer.getLayoutX();
    }

    public double getY() {
        return rootContainer.getLayoutY();
    }
    
}
