/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import jcd.data.Diagram;

/**
 *
 * @author varungoel
 */
public class AggregateLine extends ConnectorLine {

    public StandardLine standardChildLine;
    public AggregateLine aggregateChildLine;

    Rectangle rectangleHead;

    public AggregateLine() {

    }

    /**
     * Default constructor
     *
     * @param startDiagram
     * @param endDiagram is where the diamond head is located
     * @param canvas
     */
    public AggregateLine(Diagram startDiagram, Diagram endDiagram, Pane canvas) {
        this.startDiagram = startDiagram;
        this.endDiagram = endDiagram;

        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());

        this.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty().subtract(15));
        this.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty().subtract(15));

        rectangleHead = new Rectangle();

        rectangleHead.xProperty().bind(this.endXProperty());
        rectangleHead.yProperty().bind(this.endYProperty());

        rectangleHead.setHeight(20);
        rectangleHead.setWidth(20);

        initStyle();
        putOnCanvas(canvas);
    }

    public AggregateLine(Diagram endDiagram, AggregateLine parentLine, Pane canvas) {
        rectangleHead = null;

        this.startXProperty().bind((parentLine.startXProperty().add(parentLine.endXProperty())).divide(2));
        this.startYProperty().bind((parentLine.startYProperty().add(parentLine.endYProperty())).divide(2));

        this.endXProperty().bind(parentLine.endXProperty());
        this.endYProperty().bind(parentLine.endYProperty());

        initStyle();

        putOnCanvas(canvas);
    }

    private void putOnCanvas(Pane canvas) {
        if (rectangleHead != null) {
            canvas.getChildren().add(0, rectangleHead);
        }
        canvas.getChildren().add(0, this);
    }

    public void removeFromCanvas(Pane canvas) {
        if (rectangleHead != null) {
            canvas.getChildren().remove(this.rectangleHead);
        }
        canvas.getChildren().remove(this);

    }

    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(4);

        if (this.rectangleHead != null) {
            this.rectangleHead.setFill(Color.WHITE);
            this.rectangleHead.setStroke(Color.BLACK);
            this.rectangleHead.setStrokeWidth(2);
        }

    }

    public void handleDoubleClick(Pane canvas) {
        if (this.standardChildLine == null) {
            StandardLine standardLineToAdd = new StandardLine(this.getStartDiagram(), this, canvas);
            this.standardChildLine = standardLineToAdd;
            standardLineToAdd.toBack();

            AggregateLine childAggregateLine = new AggregateLine(endDiagram, this, canvas);
            this.aggregateChildLine = childAggregateLine;
            this.setVisible(false);

        } else {
            System.out.println("OPTION2");
            this.standardChildLine.removeFromCanvas(canvas);
            this.aggregateChildLine.removeFromCanvas(canvas);
            this.standardChildLine = null;
            this.aggregateChildLine = null;
        }
    }
}
