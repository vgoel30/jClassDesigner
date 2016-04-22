/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jcd.controller.GridEditController;
import jcd.gui.Workspace;
import maf.AppTemplate;
import maf.components.AppDataComponent;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class DataManager implements AppDataComponent {

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    //THE CURRENT SELECTED CLASS DIAGRAM
    public ClassDiagramObject selectedClassDiagram = null;

    public ArrayList<String> classNames = new ArrayList<>();

    //this will keep track of all the classes currently on the canvas
    public ArrayList<ClassDiagramObject> classesOnCanvas = new ArrayList<>();

    public ArrayList<String> packageNames = new ArrayList<>();

    public ArrayList<String> classPackageCombos = new ArrayList<>();

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
        // KEEP THE APP FOR LATER
        app = initApp;
    }

    public void addClassDiagram(ClassDiagramObject diagramToAdd) {
        System.out.print("A class was added  ");

        classesOnCanvas.add(diagramToAdd);
        classNames.add(diagramToAdd.getClassNameText().getText());
        packageNames.add(diagramToAdd.getPackageNameText().getText());
        classPackageCombos.add(diagramToAdd.getClassNameText().getText() + ":" + diagramToAdd.getPackageNameText().getText());
    }

    public void addPackage(String packageName) {
        packageNames.add(packageName);
    }

    public void addClassPackageCombo(String name) {
        classPackageCombos.add(name);
    }

    public Pane getRenderingPane() {
        return ((Workspace) app.getWorkspaceComponent()).getCanvas();
    }

    public void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        GridEditController gridEditController = new GridEditController(app);
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked(mouseClicked -> {
            if (workspace.selectionActive) {
                diagram.getRootContainer().getStyleClass().add(SELECTED_DIAGRAM_CONTAINER);
                diagram.getLeftLine().setVisible(true);
                diagram.getRightLine().setVisible(true);

                if (selectedClassDiagram != null) {
                    restoreSelectedProperties(selectedClassDiagram);
                }

                //event handlers for the left line (resizing from the left)
                diagram.getLeftLine().setOnMouseDragged(mouseDraggedEvent -> {
                    if (diagram.getEndPoint() - mouseDraggedEvent.getX() >= 185 && diagram.getEndPoint() - mouseDraggedEvent.getX() <= 450) {
                        diagram.getRootContainer().setPrefWidth((diagram.getEndPoint() - mouseDraggedEvent.getX()));
                        diagram.getRootContainer().setLayoutX(mouseDraggedEvent.getX());
                    }
                });

                diagram.getLeftLine().setOnMouseEntered(mouseEnteredEvent -> {
                    workspace.getScene().getRoot().setCursor(Cursor.W_RESIZE);
                });

                diagram.getLeftLine().setOnMouseExited(mouseEnteredEvent -> {
                    workspace.getScene().getRoot().setCursor(Cursor.DEFAULT);
                });

                //event handlers for the right line (resizing from the right)
                diagram.getRightLine().setOnMouseDragged(mouseDraggedEvent -> {
                    if (mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() >= 185 && mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() <= 450) {
                        diagram.getRootContainer().setPrefWidth(mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX());
                    }
                });

                diagram.getRightLine().setOnMouseEntered(mouseEnteredEvent -> {
                    workspace.getScene().getRoot().setCursor(Cursor.W_RESIZE);
                });

                diagram.getRightLine().setOnMouseExited(mouseEnteredEvent -> {
                    workspace.getScene().getRoot().setCursor(Cursor.DEFAULT);
                });

                selectedClassDiagram = diagram;
                //reflect the selected changes
                workspace.classNameField.setText(diagram.getClassNameText().getText());
                workspace.packageNameField.setText(diagram.getPackageNameText().getText());

                workspace.disableButtons(false);
            }
        });

        //FOR MOVING THE diagram
        diagram.getRootContainer().setOnMouseDragged(rectangleDraggedEvent -> {

            if (selectedClassDiagram != null) {
                if (selectedClassDiagram.equals(diagram) && workspace.selectionActive) {
                    workspace.drawingActive = false;
                    diagram.getRootContainer().setLayoutY(rectangleDraggedEvent.getSceneY() - 50);
                    diagram.getRootContainer().setLayoutX(rectangleDraggedEvent.getSceneX() - 450);

                    double x = diagram.getRootContainer().getLayoutX();
                    double y = diagram.getRootContainer().getLayoutY();

                    Pane canvas = getRenderingPane();

                    //dynamic scrolling 
                    if (x > canvas.getWidth() - 150) {
                        canvas.setMinWidth(canvas.getWidth() + 500);
                    }
                    if (y > canvas.getHeight() - 300) {
                        canvas.setMinHeight(canvas.getHeight() + 500);
                    }
                }
            }
        });
    }

    /**
     * Restores the appearance of the selected diagram after it has been
     * deselected
     *
     * @param selectedClassDiagram
     */
    public void restoreSelectedProperties(ClassDiagramObject selectedClassDiagram) {
        System.out.println("CSS REMOVAL");
        selectedClassDiagram.getRootContainer().getStyleClass().remove(SELECTED_DIAGRAM_CONTAINER);
        selectedClassDiagram.getLeftLine().setVisible(false);
        selectedClassDiagram.getRightLine().setVisible(false);
    }

    public void validateNameOfClass(String oldValue, String newValue) {
        classNames.remove(oldValue);
        

        selectedClassDiagram.getClassNameText().setText(newValue);
        classPackageCombos.remove(oldValue + ":" + selectedClassDiagram.getPackageNameText().getText());

        for (ClassDiagramObject diagram : classesOnCanvas) {
            if (diagram != selectedClassDiagram) {
                int x = selectedClassDiagram.compareTo(diagram);
                if (x == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Class name error");
                    alert.setHeaderText(null);
                    alert.setContentText("Class already exists in this package!");
                    alert.showAndWait();
                }
            }
        }

        classNames.add(newValue);
        classPackageCombos.add(newValue + ":" + selectedClassDiagram.getPackageNameText().getText());
    }

    public void validateNameOfPackage(String oldValue, String newValue) {
        packageNames.remove(oldValue);

        selectedClassDiagram.getPackageNameText().setText(newValue);
        classPackageCombos.remove(selectedClassDiagram.getClassNameText().getText() + ":" + oldValue);

        for (ClassDiagramObject diagram : classesOnCanvas) {
            if (diagram != selectedClassDiagram) {
                int x = selectedClassDiagram.compareTo(diagram);
                if (x == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Package name error");
                    alert.setHeaderText(null);
                    alert.setContentText("Package already exists in this package!");
                    alert.showAndWait();
                }
            }
        }
        packageNames.add(newValue);
        classPackageCombos.add(selectedClassDiagram.getClassNameText().getText() + ":" + newValue);
    }

    public void handleExportCode(Window window) {
        System.out.println("packageNames.size " + packageNames.size());

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export to java code");
        File file = directoryChooser.showDialog(window);

        //get the distinct package names to avoid package with same names
        ArrayList<String> distinctPackages = getDistinctPackages();

        for (String packageName : distinctPackages) {
            ArrayList<ClassDiagramObject> insideCorrectPackage = findByPackageName(packageName);
            //replace all the . with / to prevent issues in file management
            packageName = packageName.replace(".", "/");
            File directory = new File(file.getPath() + "/src/" + packageName);
            directory.mkdirs();
            for (ClassDiagramObject diagramToExtract : insideCorrectPackage) {
                File javaFile = new File(directory, diagramToExtract.getClassNameText().getText() + ".java");

                try {
                    PrintWriter myWriter = new PrintWriter(javaFile.getPath(), "UTF-16");
                    myWriter.write(diagramToExtract.toStringCode());
                    myWriter.close();
                } catch (FileNotFoundException ex) {
                    System.out.println("FILE NOT FOUND");
                } catch (UnsupportedEncodingException ex) {
                    System.out.println("ENCODING NOT FOUND");
                }

            }
        }
    }

    public ArrayList<String> getDistinctPackages() {
        ArrayList<String> uniquePackages = new ArrayList<>();
        for (ClassDiagramObject diagrams : classesOnCanvas) {
            if (!uniquePackages.contains(diagrams.getPackageNameText().getText())) {
                uniquePackages.add(diagrams.getPackageNameText().getText());
            }
        }
        return uniquePackages;
    }

    public ArrayList<ClassDiagramObject> findByPackageName(String packageName) {
        ArrayList<ClassDiagramObject> legitList = new ArrayList<>();

        for (ClassDiagramObject diagrams : classesOnCanvas) {
            if (diagrams.getPackageNameText().getText().equals(packageName)) {
                legitList.add(diagrams);
            }
        }

        return legitList;
    }

    @Override
    public void reset() {
        //remove all the children
        classesOnCanvas.clear();
        ((Workspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
}
