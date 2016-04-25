package jcd.actions;
import jcd.data.ClassDiagramObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Moving the diagram on the canvas.
 * @author varungoel
 */
public class MoveDiagram extends Action{
    
    double initialPositionX;
    double initialPositionY;

    double finalPositionX;
    double finalPositionY;
    
    ClassDiagramObject diagram;
    
    public MoveDiagram(ClassDiagramObject diagram){
        this.diagram = diagram;
        this.actionType = "move_diagram";
    }

    public double getInitialPositionX() {
        return initialPositionX;
    }

    public ClassDiagramObject getDiagram() {
        return diagram;
    }

    public double getInitialPositionY() {
        return initialPositionY;
    }

    public double getFinalPositionX() {
        return finalPositionX;
    }

    public double getFinalPositionY() {
        return finalPositionY;
    }
    
    
    public void setInitialPosition(double initialPositionX, double initialPositionY) {
        this.initialPositionX = initialPositionX;
        this.initialPositionY = initialPositionY;
    }
    
    public void setFinalPosition(double finalPositionX, double finalPositionY) {
        this.finalPositionX = finalPositionX;
        this.finalPositionY = finalPositionY;
    }

    
    
}
