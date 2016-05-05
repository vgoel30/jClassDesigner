/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import jcd.data.Diagram;

/**
 *
 * @author varungoel
 */
public class StandardLine extends ConnectorLine {

    Ellipse connectionPoint;

    public StandardLine() {
    }

    public StandardLine(Diagram startDiagram, double endX, double endY, Pane canvas) {
        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());

        this.setEndX(endX);
        this.setEndY(endY);

        putOnCanvas(canvas);
        initStyle();
    }

    public StandardLine(Diagram startDiagram, ConnectorLine parentLine, Pane canvas) {

        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());

        this.endXProperty().bind((parentLine.startXProperty().add(parentLine.endXProperty())).divide(2));
        this.endYProperty().bind((parentLine.startYProperty().add(parentLine.endYProperty())).divide(2));
        //(parentLine.startXProperty().add(parentLine.endXProperty())).divide(2)

        connectionPoint = new Ellipse();

        connectionPoint.centerXProperty().bind(this.endXProperty());
        connectionPoint.centerYProperty().bind(this.endYProperty());

        connectionPoint.setRadiusX(7);
        connectionPoint.setRadiusY(7);

        connectionPoint.setOnMouseClicked(e -> {

            //if the connection point is clicked twice, we want to remove that point
            if (e.getClickCount() == 2) {
                //if it's an inheritance line
                if (parentLine instanceof InheritanceLine) {
                    InheritanceLine inheritanceParentLine = (InheritanceLine) parentLine;

                    inheritanceParentLine.standardChildLine.removeFromCanvas(canvas);
                    inheritanceParentLine.inheritanceChildLine.removeFromCanvas(canvas);

                    inheritanceParentLine.standardChildLine = null;
                    inheritanceParentLine.inheritanceChildLine = null;
                    //restore the original parent line
                    inheritanceParentLine.setVisible(true);
                } else if (parentLine instanceof AggregateLine) {
                    AggregateLine aggregateLine = (AggregateLine) parentLine;

                    aggregateLine.standardChildLine.removeFromCanvas(canvas);
                    aggregateLine.aggregateChildLine.removeFromCanvas(canvas);

                    aggregateLine.standardChildLine = null;
                    aggregateLine.aggregateChildLine = null;
                    //restore the original parent line
                    aggregateLine.setVisible(true);
                }
            }

        });

        //when being dragged, change the pivoting
        connectionPoint.setOnMouseDragged(e -> {
            if (parentLine instanceof InheritanceLine) {
                InheritanceLine inheritanceParentLine = (InheritanceLine) parentLine;

                inheritanceParentLine.standardChildLine.endXProperty().unbind();
                inheritanceParentLine.standardChildLine.endYProperty().unbind();

                inheritanceParentLine.standardChildLine.setEndX(e.getX());
                inheritanceParentLine.standardChildLine.setEndY(e.getY());

                inheritanceParentLine.inheritanceChildLine.startXProperty().unbind();
                inheritanceParentLine.inheritanceChildLine.startYProperty().unbind();

                inheritanceParentLine.inheritanceChildLine.setStartX(e.getX());
                inheritanceParentLine.inheritanceChildLine.setStartY(e.getY());
                
            } else if (parentLine instanceof AggregateLine) {
                AggregateLine aggregateLine = (AggregateLine) parentLine;

                aggregateLine.standardChildLine.endXProperty().unbind();
                aggregateLine.standardChildLine.endYProperty().unbind();

                aggregateLine.standardChildLine.setEndX(e.getX());
                aggregateLine.standardChildLine.setEndY(e.getY());

                aggregateLine.aggregateChildLine.startXProperty().unbind();
                aggregateLine.aggregateChildLine.startYProperty().unbind();

                aggregateLine.aggregateChildLine.setStartX(e.getX());
                aggregateLine.aggregateChildLine.setStartY(e.getY());
            }
        });
        

        putOnCanvas(canvas);
        initStyle();
    }

    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(4);
        connectionPoint.setFill(Color.WHITE);
        connectionPoint.setStroke(Color.BLACK);

    }

    public void removeFromCanvas(Pane canvas) {
        canvas.getChildren().remove(this);
        if (connectionPoint != null) {
            canvas.getChildren().remove(connectionPoint);
        }
    }

    private void putOnCanvas(Pane canvas) {
        canvas.getChildren().add(this);
        canvas.getChildren().add(connectionPoint);
        connectionPoint.toBack();
    }
}
