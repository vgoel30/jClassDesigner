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

    Polygon triangleHead;
   public StandardLine standardChildLine;
   public InheritanceLine inheritanceChildLine;

    public InheritanceLine() {
    }

    public InheritanceLine(Diagram endDiagram, Diagram startDiagram, Pane canvas) {
        this.startDiagram = startDiagram;
        this.endDiagram = endDiagram;

        standardChildLine = null;
        inheritanceChildLine = null;
        
        this.startXProperty().bind(startDiagram.getRootContainer().layoutXProperty());
        this.startYProperty().bind(startDiagram.getRootContainer().layoutYProperty());

        this.endXProperty().bind(endDiagram.getRootContainer().layoutXProperty().subtract(7));
        this.endYProperty().bind(endDiagram.getRootContainer().layoutYProperty().subtract(7));

        double finalX = this.getEndX();
        double finalY = this.getEndY();

        triangleHead = new Polygon();
        triangleHead.getPoints().addAll(new Double[]{
            finalX + 20.0, finalY + 10.0,
            finalX - 10, finalY - 10,
            finalX + 10.0, finalY + 20.0});
        triangleHead.setRotate(135);
        initStyle();
        putOnCanvas(canvas);
    }

    public InheritanceLine(Diagram endDiagram, InheritanceLine parentLine, Pane canvas) {
        triangleHead = null;
        
        this.startXProperty().bind((parentLine.startXProperty().add(parentLine.endXProperty())).divide(2));
        this.startYProperty().bind((parentLine.startYProperty().add(parentLine.endYProperty())).divide(2));
        
        this.endXProperty().bind(parentLine.endXProperty());
        this.endYProperty().bind(parentLine.endYProperty());

        initStyle();

        putOnCanvas(canvas);
    }

    InheritanceLine(Diagram endDiagram, AggregateLine aThis, Pane canvas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public StandardLine getStandardChildLine() {
        return standardChildLine;
    }

    /**
     * This method is a custom binding method that will change the position of
     * the diamond head as the diagram is moved
     *
     * @param endDiagram
     * @param startDiagram
     * @param canvas
     */
    public void updateTriangleHead(Diagram endDiagram, Diagram startDiagram, Pane canvas) {
        this.triangleHead.getPoints().clear();

        double finalX = this.getEndX();
        double finalY = this.getEndY();

        this.triangleHead.getPoints().addAll(new Double[]{
            finalX + 20.0, finalY + 10.0,
            finalX - 10, finalY - 10,
            finalX + 10.0, finalY + 20.0});
    }

    public Polygon getTriangleHead() {
        return triangleHead;
    }

    private void putOnCanvas(Pane canvas) {
        if (triangleHead != null) {
            canvas.getChildren().add(0, triangleHead);
        }
        canvas.getChildren().add(0, this);
    }

    public void removeFromCanvas(Pane canvas) {
        if (triangleHead != null) {
            triangleHead.getPoints().removeAll(triangleHead.getPoints());
            canvas.getChildren().remove(this.triangleHead);
        }
        canvas.getChildren().remove(this);

    }

    private void removeLineFromCanvas(Pane canvas) {
        canvas.getChildren().remove(this);
    }

    private void initStyle() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(4);

        if (triangleHead != null) {
            this.triangleHead.setFill(Color.WHITE);
            this.triangleHead.setStroke(Color.BLACK);
            this.triangleHead.setStrokeWidth(2);
        }
    }

    public void handleDoubleClick(Pane canvas) {
        if (this.standardChildLine == null) {
            StandardLine standardLineToAdd = new StandardLine(this.getStartDiagram(), this, canvas);
            this.standardChildLine = standardLineToAdd;
            standardLineToAdd.toBack();

            InheritanceLine childInheritanceLine = new InheritanceLine(endDiagram, this, canvas);
            this.inheritanceChildLine = childInheritanceLine;
            this.setVisible(false);

        } else {
            System.out.println("OPTION2");
            this.standardChildLine.removeFromCanvas(canvas);
          this.inheritanceChildLine.removeFromCanvas(canvas);
            this.standardChildLine = null;
            this.inheritanceChildLine = null;
        }
    }
}
