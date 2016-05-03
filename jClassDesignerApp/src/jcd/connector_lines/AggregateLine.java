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

    Rectangle rectangleHead;

    public AggregateLine() {

    }
    
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

    public AggregateLine(double initialX, double initialY, double finalX, double finalY, Pane canvas) {
        this.setStartX(initialX);
        this.setStartY(initialY);

        this.setEndX(finalX);
        this.setEndY(finalY);

        rectangleHead = new Rectangle();

        rectangleHead.xProperty().bind(this.endXProperty());
        rectangleHead.yProperty().bind(this.endYProperty());

        rectangleHead.setHeight(20);
        rectangleHead.setWidth(20);

        
        initStyle();
        putOnCanvas(canvas);
    }

    private void putOnCanvas(Pane canvas) {
        canvas.getChildren().add(0, rectangleHead);
        canvas.getChildren().add(0, this);
    }
    
    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);

        this.rectangleHead.setFill(Color.WHITE);
        
        this.rectangleHead.setStroke(Color.BLACK);
        this.rectangleHead.setStrokeWidth(2);
    }
}
