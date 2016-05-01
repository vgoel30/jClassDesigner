/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

import jcd.data.ClassDiagramObject;

/**
 *The resize to the right move. We are  concerned with the initial and final width and the initial and final x coordinates
 * @author varungoel
 */
public class ResizeLeft extends Action{
    
    ClassDiagramObject diagram;
    double initialWidth;
    double finalWidth;
    double initialX;
    double finalX;
    
    public ResizeLeft(ClassDiagramObject diagram){
        this.diagram = diagram;
        this.actionType = "resize_left";
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

    public double getInitialX() {
        return initialX;
    }

    public void setInitialX(double initialX) {
        this.initialX = initialX;
    }

    public double getFinalX() {
        return finalX;
    }

    public void setFinalX(double finalX) {
        this.finalX = finalX;
    }
    
    
    
    public ResizeLeft(double initialWidth, double finalWidth, double initialX, double finalX){
        this.initialWidth = initialWidth;
        this.finalWidth = finalWidth;
        this.initialX = initialX;
        this.finalX = finalX;
    }
    
    public String toString(){
        return actionType + "  " + initialWidth + "  " + finalWidth ;
    }
    
   
}
