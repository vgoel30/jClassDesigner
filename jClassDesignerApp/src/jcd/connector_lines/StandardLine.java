/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import jcd.data.Diagram;

/**
 *
 * @author varungoel
 */
public class StandardLine extends ConnectorLine{
    
    Ellipse connectionPoint ;
    
    public StandardLine(){
    }
    
    public StandardLine(Diagram startDiagram, double endX, double endY, Pane canvas){
        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());
        
        this.setEndX(endX);
        this.setEndY(endY);
        
        putOnCanvas(canvas);
        initStyle();
    }
    
    public StandardLine(Diagram startDiagram, ConnectorLine parentLine , Pane canvas){
        
        
        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());
       
        this.endXProperty().bind((parentLine.startXProperty().add(parentLine.endXProperty())).divide(2));
        this.endYProperty().bind((parentLine.startYProperty().add(parentLine.endYProperty())).divide(2));
        //(parentLine.startXProperty().add(parentLine.endXProperty())).divide(2)
        
        connectionPoint = new Ellipse();
        
        connectionPoint.centerXProperty().bind(this.endXProperty());
        connectionPoint.centerYProperty().bind(this.endYProperty());
        
        connectionPoint.setRadiusX(5);
        connectionPoint.setRadiusY(5);
        
        putOnCanvas(canvas);
        initStyle();
    }
    
    private void initStyle(){
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
        connectionPoint.setFill(Color.WHITE);
        connectionPoint.setStroke(Color.BLACK);
       
    }
    
    public void removeFromCanvas(Pane canvas){
       canvas.getChildren().remove(this);
       if(connectionPoint != null)
           canvas.getChildren().remove(connectionPoint);
    }
    
    private void putOnCanvas(Pane canvas){
        canvas.getChildren().add(this);
        canvas.getChildren().add(connectionPoint);
        connectionPoint.toBack();
    }
}
