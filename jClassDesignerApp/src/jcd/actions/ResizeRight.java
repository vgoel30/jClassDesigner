/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

import jcd.data.ClassDiagramObject;

/**
 *The resize to the right move. We are only concerned with the initial and final width.
 * @author varungoel
 */
public class ResizeRight extends Action{
    
    ClassDiagramObject diagram;
    double initialWidth;
    double finalWidth;
    
    public ResizeRight(ClassDiagramObject diagram){
        this.diagram = diagram;
        this.actionType = "resize_right";
    }

    public double getInitialWidth() {
        return initialWidth;
    }

    public ClassDiagramObject getDiagram() {
        return diagram;
    }

    public void setInitialWidth(double initialWidth) {
        this.initialWidth = initialWidth;
    }

    public double getFinalWidth() {
        return finalWidth;
    }

    public void setFinalWidth(double finalWidth) {
        this.finalWidth = finalWidth;
    }
    
    public ResizeRight(double initialWidth, double finalWidth){
        this.initialWidth = initialWidth;
        this.finalWidth = finalWidth;
    }
    
    public String toString(){
        return actionType + "  " + initialWidth + "  " + finalWidth ;
    }
    
   
}
