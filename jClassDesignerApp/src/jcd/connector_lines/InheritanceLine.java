/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import jcd.data.ClassDiagramObject;
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
        
        this.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty());
        this.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty());

        double finalX = endDiagram.getRootContainer().getLayoutX();
        double finalY = endDiagram.getRootContainer().getLayoutY();
        
        triangleHead = new Polygon();
        triangleHead.getPoints().addAll(new Double[]{
            finalX, finalY,
            finalX + 25.0, finalY + 10.0,
            finalX + 10.0, finalY + 25.0});

        //triangleHead.translateXProperty().bind(finalX);
        triangleHead.layoutXProperty().bind(this.endXProperty().subtract(15));
        triangleHead.layoutYProperty().bind(this.endYProperty().subtract(15));
        initStyle();
        putOnCanvas(canvas);
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

//        triangleHead.setTranslateX();
//        triangleHead.setTranslateY();
        putOnCanvas(canvas);
    }

    public Polygon getTriangleHead() {
        return triangleHead;
    }

    private void putOnCanvas(Pane canvas) {
        canvas.getChildren().add(this);
        canvas.getChildren().add(triangleHead);
    }

    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);

        this.triangleHead.setFill(Color.WHITE);
        this.triangleHead.setStroke(Color.BLACK);
        this.triangleHead.setStrokeWidth(2);
    }
}
