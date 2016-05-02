/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import jcd.data.ClassDiagramObject;

/**
 *
 * @author varungoel
 */
public class InheritanceLine extends ConnectorLine {

    Polygon triangleHead;

    public InheritanceLine() {
    }

    public InheritanceLine(ClassDiagramObject diagram) {
        double x = diagram.getRootContainer().getLayoutX() + diagram.getRootContainer().getMinWidth();
        double y = diagram.getRootContainer().getLayoutY() + diagram.getRootContainer().getMinHeight() + (diagram.getRootContainer().getMinWidth() / 2);

        triangleHead = new Polygon();
//triangleHead.getPoints().addAll(new Double[]{
//    0.0, 0.0,
//    20.0, 10.0,
//    10.0, 20.0 });

        triangleHead.getPoints().addAll(new Double[]{
            x, y,
            x - 1, y + 1,
            x + 1, y - 1});

        initStyle();
    }

    public InheritanceLine(double initialX, double initialY, double finalX, double finalY, ClassDiagramObject diagram) {
        this.setStartX(initialX);
        this.setStartY(initialY);

        this.setEndX(finalX);
        this.setEndY(finalY);

        double x = diagram.getRootContainer().getLayoutX() + diagram.getRootContainer().getPrefWidth();
        double y = diagram.getRootContainer().getLayoutY() + diagram.getRootContainer().getPrefHeight() + (diagram.getRootContainer().getPrefWidth() / 2);

        triangleHead = new Polygon();
        triangleHead.getPoints().addAll(new Double[]{
            initialX, initialY,
            initialX - 5, initialY + 5,
            initialX - 5, initialY + 5});

        initStyle();
    }

    public Polygon getTriangleHead() {
        return triangleHead;
    }

    public void initStyle() {
        this.setStroke(Color.web("#BDC3C7", 0.2));
        this.setStrokeWidth(2);

        this.triangleHead.setFill(Color.BLACK);
        this.triangleHead.setStroke(Color.RED);
        this.triangleHead.setStrokeWidth(2);
    }
}
