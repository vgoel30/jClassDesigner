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

    //THE CURRENT SELECTED CLASS DIAGRAM
    static ClassDiagramObject selectedClassDiagram = null;
    static ArrayList<String> classNames = new ArrayList<>();
    static ArrayList<String> packageNames = new ArrayList<>();
    static ArrayList<String> classPackageCombos = new ArrayList<>();
    

    AppTemplate app;

    DataManager dataManager;
    DiagramEditController diagramEditController;

    public GridEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
        diagramEditController = new DiagramEditController(initApp);
    }

    public void addClassDiagram(Pane canvas) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        if (selectedClassDiagram != null) {
            diagramEditController.restoreSelectedProperties(selectedClassDiagram);
            selectedClassDiagram = null;
            
            workspace.disableButtons(true);
        }

        canvas.setOnMouseClicked(e -> {
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

                ClassDiagramObject objectToPut = new ClassDiagramObject(canvas, x, y);
                diagramEditController.attachClassDiagramEventHandlers(objectToPut);
                workspace.disableButtons(true);
                dataManager.classesOnCanvas.add(objectToPut);
            }
            //System.out.println(canvas.getChildren());
            System.out.println(dataManager.classesOnCanvas);
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
