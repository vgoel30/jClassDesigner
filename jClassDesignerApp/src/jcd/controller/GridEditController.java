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
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.gui.GridLine;
import jcd.gui.Workspace;
import maf.AppTemplate;

/**
 *
 * @author varungoel
 */
public class GridEditController {
    AppTemplate app;

    DataManager dataManager;

    public GridEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }

    /**
     * Adds a new diagram to the canvas
     * @param canvas is the canvas on which the diagram is rendered
     * @param type is the diagram type (class/interface)
     */
    public void addDiagram(Pane canvas, String type) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        if (dataManager.selectedClassDiagram != null) {
            dataManager.restoreSelectedProperties(dataManager.selectedClassDiagram);
            dataManager.selectedClassDiagram = null;
            
            workspace.disableButtons(true);
        }

        canvas.setOnMouseClicked((MouseEvent e) -> {
            if (workspace.drawingActive) {
                double x = e.getX();
                double y = e.getY();

                //dynamic scrolling 
                if (x > canvas.getWidth() - 150) {
                    canvas.setMinWidth(canvas.getWidth() + 500);
                    canvas.setMinHeight(canvas.getHeight() + 500);
                    
                    
                }
                if (y > canvas.getHeight() - 300) {
                    canvas.setMinHeight(canvas.getHeight() + 500);
                }
                
                //initalize a class diagram object
                ClassDiagramObject objectToPut = new ClassDiagramObject(x, y, type);
                //render it on the canvas
                objectToPut.putOnCanvas(canvas);
                dataManager.attachClassDiagramEventHandlers(objectToPut);
                workspace.disableButtons(true);
                
                dataManager.addClassDiagram(objectToPut);
            }
            
        });

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
     * @param canvas 
     */
    public void renderGridLines(ScrollPane canvasScrollPane, Pane canvas) {
        //this will clear out all the old canvas lines
        removeGridLines(canvas);
        
        System.out.println("canvasScrollPane.getVvalue()    " + canvas.getWidth());
        
        for(int i = 0; i < canvas.getWidth(); i = i + 15){
                GridLine gridLine = new GridLine();
                 gridLine.setStartY(0);
                 gridLine.setStartX(i);
                 
                 gridLine.setEndX(i);
                 gridLine.setEndY(canvas.getHeight());
                 //this sets them to the back (enforces it)
                 canvas.getChildren().add(0, gridLine);
            }
            
            for(int i = 0; i < canvas.getHeight(); i = i + 15){
                GridLine gridLine = new GridLine();
                 gridLine.setStartY(i);
                 gridLine.setStartX(0);
                 
                 gridLine.setEndX(canvas.getWidth());
                 gridLine.setEndY(i);
                 //this sets them to the back (enforces it)
                 canvas.getChildren().add(0, gridLine);
            }
    }

    public void removeGridLines(Pane canvas) {
        ArrayList<GridLine> linesToRemove = new ArrayList();
        
        for(Node objectOnGrid: canvas.getChildren()){
            if(objectOnGrid instanceof GridLine){
                linesToRemove.add((GridLine) objectOnGrid);
            }
        }
        
        canvas.getChildren().removeAll(linesToRemove);
    }
}
