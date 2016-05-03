/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import jcd.data.Diagram;

/**
 *
 * @author varungoel
 */
public class InheritanceLine extends ConnectorLine {

    Polygon triangleHead;

    public InheritanceLine() {
    }

    public InheritanceLine(Diagram endDiagram, Diagram startDiagram, Pane canvas) {
        this.startDiagram = startDiagram;
        this.endDiagram = endDiagram;
         
        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());
         
        this.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty().subtract(7));
        this.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty().subtract(7));

        double finalX = this.getEndX();
        double finalY = this.getEndY();
        
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
        triangleHead.getPoints().removeAll(triangleHead.getPoints());
        
        this.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty().subtract(7));
        this.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty().subtract(7));

        double finalX = this.getEndX();
        double finalY = this.getEndY();
        
        triangleHead.getPoints().addAll(new Double[]{
            finalX+20.0, finalY+10.0,
            finalX-10, finalY-10,
            finalX + 10.0, finalY + 20.0});
    }

    public InheritanceLine(double initialX, double initialY, double finalX, double finalY,Pane canvas) {
        this.setStartX(initialX);
        this.setStartY(initialY);

        this.setEndX(finalX);
        this.setEndY(finalY);

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
        canvas.getChildren().add(0,this);
    }
    
    public void removeFromCanvas(Pane canvas){
        canvas.getChildren().remove(this);
        canvas.getChildren().remove(triangleHead);
    }

    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);

        this.triangleHead.setFill(Color.WHITE);
        
        this.triangleHead.setStroke(Color.BLACK);
        this.triangleHead.setStrokeWidth(2);
    }
}
