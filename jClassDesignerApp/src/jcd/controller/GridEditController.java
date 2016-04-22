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
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.gui.Workspace;
import maf.AppTemplate;

/**
 *
 * @author varungoel
 */
public class GridEditController {
    AppTemplate app;

    DataManager dataManager;
    
     ArrayList<String> classes = new ArrayList<>();

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
//        if (DataManager.selectedClassDiagram != null) {
//            
//            DataManager.selectedClassDiagram = null;
//            
//            workspace.disableButtons(true);
//        }
        
        //dataManager.restoreSelectedProperties(DataManager.selectedClassDiagram);

        canvas.setOnMouseClicked((MouseEvent e) -> {
            if (workspace.drawingActive) {
                double x = e.getX();
                double y = e.getY();

                //dynamic scrolling 
                if (x > canvas.getWidth() - 150) {
                    canvas.setMinWidth(canvas.getWidth() + 500);
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
                
                String nameToAdd = objectToPut.getClassNameText().getText() + ":" + objectToPut.getPackageNameText().getText();
                dataManager.addClassPackageCombo(nameToAdd);
                classes.add(objectToPut.getClassNameText().getText());
                System.out.println("GEC: "  + classes);
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
}
