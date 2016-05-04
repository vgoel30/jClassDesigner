/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import jcd.data.Diagram;

/**
 *
 * @author varungoel
 */
public class InheritanceLine extends ConnectorLine {
    
    Line mainLine;
    Polygon triangleHead;

   
    public InheritanceLine() {
    }

    public InheritanceLine(Diagram endDiagram, Diagram startDiagram, Pane canvas) {
        this.startDiagram = startDiagram;
        this.endDiagram = endDiagram;
        
        mainLine = new Line();
         
        mainLine.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        mainLine.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());
         
        mainLine.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty().subtract(7));
        mainLine.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty().subtract(7));

        double finalX = mainLine.getEndX();
        double finalY = mainLine.getEndY();
        
        triangleHead = new Polygon();
        triangleHead.getPoints().addAll(new Double[]{
            finalX+20.0, finalY+10.0,
            finalX-10, finalY-10,
            finalX + 10.0, finalY + 20.0});
        triangleHead.setRotate(135);
        initStyle();
        putOnCanvas(canvas);
    }
    
   
    
    /**
     * This method is a custom binding method that will change the position of the diamond head as the diagram is moved
     * @param endDiagram
     * @param startDiagram
     * @param canvas 
     */
    public void updateTriangleHead(Diagram endDiagram, Diagram startDiagram, Pane canvas){
        
        //System.out.println("BEING CALLED");
        this.triangleHead.getPoints().removeAll(this.triangleHead.getPoints());

        double finalX = mainLine.getEndX();
        double finalY = mainLine.getEndY();
        
        this.triangleHead.getPoints().addAll(new Double[]{
            finalX+20.0, finalY+10.0,
            finalX-10, finalY-10,
            finalX + 10.0, finalY + 20.0});
    }

    public InheritanceLine(double initialX, double initialY, double finalX, double finalY,Pane canvas) {
        mainLine.setStartX(initialX);
        mainLine.setStartY(initialY);

        mainLine.setEndX(finalX);
        mainLine.setEndY(finalY);

        triangleHead = new Polygon();
        triangleHead.getPoints().addAll(new Double[]{
            initialX, initialY,
            initialX + 28.0, initialY + 10.0,
            initialX + 10.0, initialY + 28.0});

        putOnCanvas(canvas);
    }

    public Polygon getTriangleHead() {
        return triangleHead;
    }

    private void putOnCanvas(Pane canvas) {
        canvas.getChildren().add(0,triangleHead);
        canvas.getChildren().add(0,mainLine);
    }
    
    public void removeFromCanvas(Pane canvas){
        triangleHead.getPoints().removeAll(triangleHead.getPoints());
        canvas.getChildren().remove(mainLine);
        canvas.getChildren().remove(this.triangleHead);
    }

    private void initStyle() {
        mainLine.setStroke(Color.BLACK);
        mainLine.setStrokeWidth(2);

        this.triangleHead.setFill(Color.WHITE);
        
        this.triangleHead.setStroke(Color.BLACK);
        this.triangleHead.setStrokeWidth(2);
    }
}
