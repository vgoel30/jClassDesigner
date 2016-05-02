/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import jcd.connector_lines.InheritanceLine;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.Diagram;
import jcd.data.ExternalParent;
import jcd.gui.GridLine;
import jcd.gui.Workspace;
import maf.AppTemplate;

/**
 *
 * @author varungoel
 */
public class GridEditController {
    
    public static final String EXTERNAL_PARENT = "external_parent";
    public static final String EXTERNAL_INTERFACE = "external_interface";
    public static final String EXTERNAL_PACKAGE = "external_package";

    AppTemplate app;

    DataManager dataManager;

    public GridEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }

    /**
     * Adds a new diagram to the canvas
     *
     * @param canvas is the canvas on which the diagram is rendered
     * @param type is the diagram type (class/interface)
     */
    public void addDiagram(Pane canvas, String type) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        if (dataManager.selectedClassDiagram != null) {
            ClassDiagramObject selectedClassObject = (ClassDiagramObject)dataManager.selectedClassDiagram;
            dataManager.restoreSelectedProperties(selectedClassObject);
            dataManager.selectedClassDiagram = null;

            workspace.disableButtons(true);
        }

        canvas.setOnMouseClicked((MouseEvent e) -> {
            if (workspace.drawingActive) {
                double x = e.getX();
                double y = e.getY();

                //dynamic scrolling 
                if (x > canvas.getWidth() - 150) {
                    canvas.setMinWidth(canvas.getWidth() + 300);
                    canvas.setPrefWidth(canvas.getWidth() + 300);
                }
                if (y > canvas.getHeight() - 300) {
                    canvas.setMinHeight(canvas.getHeight() + 500);
                    canvas.setPrefHeight(canvas.getHeight() + 500);
                }

                //initalize a class diagram object
                ClassDiagramObject objectToPut = new ClassDiagramObject(x, y, type);
                //render it on the canvas
                objectToPut.putOnCanvas(canvas);
                
             
                dataManager.attachClassDiagramEventHandlers(objectToPut);
                workspace.disableButtons(true);
                
//                InheritanceLine inheritanceLine = new InheritanceLine(132, 132, 264, 264, objectToPut);
//                canvas.getChildren().add(inheritanceLine.getTriangleHead());
                
                dataManager.addClassDiagram(objectToPut);
            }
            
        });

        if(workspace.snapIsActive()){
            System.out.println("SNAP IS ACTIVE");
            System.out.println(dataManager.classesOnCanvas);
            snapToGrid(dataManager.classesOnCanvas);
        }
        
    }
    
    /**
     * Renders and external class/package/interface box on canvas
     * @param name
     * @param type
     * @param canvas 
     */
    public Diagram renderExternalDiagramBox(String name, String type, Pane canvas){
       // if(type.equals(EXTERNAL_PARENT)){
            ExternalParent externalParent = new ExternalParent(name);
            externalParent.putOnCanvas(canvas);
            dataManager.attachExternalDiagramHandlers(externalParent);
            return externalParent;
        //}
    }

    public void processSnapshot() {

        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        File file = new File("Pose.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ioe) {
        }
    }

    /**
     * Renders the grid lines on the canvas
     *
     * @param canvas
     */
    public void renderGridLines(Pane canvas) {
        //this will clear out all the old canvas lines
        removeGridLines(canvas);

        int firstBound = (int) canvas.getWidth();
        int secondBound = (int) canvas.getHeight();

        if (canvas.getPrefWidth() > 0) {
            firstBound = (int) canvas.getPrefWidth();
        }

        if (canvas.getPrefHeight() > 0) {
            secondBound = (int) canvas.getPrefHeight();
        }

        for (int i = 0; i < firstBound; i = i + 15) {
            GridLine gridLine = new GridLine();
            gridLine.setStartY(0);
            gridLine.setStartX(i);

            gridLine.setEndX(i);
            gridLine.setEndY(secondBound);
            //this sets them to the back (enforces it)
            canvas.getChildren().add(0, gridLine);
        }

        for (int i = 0; i < secondBound; i = i + 15) {
            GridLine gridLine = new GridLine();
            gridLine.setStartY(i);
            gridLine.setStartX(0);

            gridLine.setEndX(firstBound);
            gridLine.setEndY(i);
            //this sets them to the back (enforces it)
            canvas.getChildren().add(0, gridLine);
        }
    }

    public void removeGridLines(Pane canvas) {
        ArrayList<GridLine> linesToRemove = new ArrayList();

        for (Node objectOnGrid : canvas.getChildren()) {
            if (objectOnGrid instanceof GridLine) {
                linesToRemove.add((GridLine) objectOnGrid);
            }
        }

        canvas.getChildren().removeAll(linesToRemove);
    }

    /**
     * Method that snaps all the diagrams to the grid
     * @param classesOnCanvas 
     */
    public void snapToGrid(ArrayList<ClassDiagramObject> classesOnCanvas) {
        for(ClassDiagramObject diagramToFix: classesOnCanvas){
                diagramToFix.getRootContainer().setLayoutX(diagramToFix.getX() - (diagramToFix.getX()%15));
                diagramToFix.getRootContainer().setLayoutY(diagramToFix.getY() - (diagramToFix.getY()%15));
                }
    }

    public void zoomIn(Pane canvas) {
        canvas.setScaleX(canvas.getScaleX() + 0.05);
        canvas.setScaleY(canvas.getScaleY() + 0.05);
    }
    
    public void zoomOut(Pane canvas) {
        canvas.setScaleX(canvas.getScaleX() - 0.05);
        canvas.setScaleY(canvas.getScaleY() - 0.05);
    }
}
