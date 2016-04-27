/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Custom line to render grid lines on the canvas
 * @author varungoel
 */
public class GridLine extends Line{
    
    public GridLine(){
        initStyle();
    }
    
    public void initStyle(){
        this.setStroke(Color.web("#BDC3C7", 0.2));
        this.setStrokeWidth(2);
    }
    
}
