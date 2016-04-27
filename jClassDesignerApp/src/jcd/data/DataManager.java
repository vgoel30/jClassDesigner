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
import java.util.Stack;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import jcd.actions.Action;
import jcd.actions.MoveDiagram;
import jcd.actions.ResizeRight;
import jcd.controller.ActionController;
import jcd.controller.DiagramController;
import jcd.controller.GridEditController;
import jcd.gui.AppOptionDialog;
import jcd.gui.VariableOptionDialog;
import jcd.gui.VariableRemoveDialog;
import jcd.gui.Workspace;
import maf.AppTemplate;
import maf.components.AppDataComponent;
import static maf.components.AppStyleArbiter.SELECTED_DIAGRAM_CONTAINER;

/**
 *
 * @author varungoel
 */
public class DataManager implements AppDataComponent {

    public static final String RESIZE_RIGHT = "resize_right";
    public static final String MOVE_DIAGRAM = "move_diagram";

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    //THE CURRENT SELECTED CLASS DIAGRAM
    public ClassDiagramObject selectedClassDiagram = null;

    public ArrayList<String> classNames = new ArrayList<>();

    //this will keep track of all the classes currently on the canvas
    public ArrayList<ClassDiagramObject> classesOnCanvas = new ArrayList<>();

    public ArrayList<String> packageNames = new ArrayList<>();

    public ArrayList<String> classPackageCombos = new ArrayList<>();

    public Stack<Action> undoStack = new Stack<>();

    ActionController actionController;
    DiagramController diagramController;
    GridEditController gridEditController;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
        // KEEP THE APP FOR LATER
        app = initApp;
        actionController = new ActionController(initApp);
        diagramController = new DiagramController();
        gridEditController = new GridEditController(initApp);
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
    
    public ScrollPane getRenderingScrollPane(){
        return ((Workspace) app.getWorkspaceComponent()).getCanvasScrollPane();
    }

    public void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        MoveDiagram moveDiagramEvent = new MoveDiagram(diagram);

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked((MouseEvent mouseClicked) -> {
            if (workspace.selectionActive) {
                diagram.getRootContainer().getStyleClass().add(SELECTED_DIAGRAM_CONTAINER);
                diagram.getLeftLine().setVisible(true);
                diagram.getRightLine().setVisible(true);

                if (selectedClassDiagram != null) {
                    restoreSelectedProperties(selectedClassDiagram);
                }

                //set the inital position of the mouse event
                moveDiagramEvent.setInitialPosition(diagram.getRootContainer().getLayoutX(), diagram.getRootContainer().getLayoutY());
                

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

                //if the user is resizing to the right, create a new resize right action
                ResizeRight resizeRightMove = new ResizeRight(diagram);

                //when the right line is pressed for the resizing, that's the initial width for resizing
                diagram.getRightLine().setOnMousePressed(mouseClickedEvent -> {
                    resizeRightMove.setInitialWidth(diagram.getRootContainer().getWidth());

                });

                //event handlers for the right line (resizing from the right)
                diagram.getRightLine().setOnMouseDragged(mouseDraggedEvent -> {
                    if (mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() >= 185 && mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() <= 450) {
                        diagram.getRootContainer().setPrefWidth(mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX());
                    }
                });

                //when the mouse has been released, we set the final width and push the acion on the undo stack
                diagram.getRightLine().setOnMouseReleased(mouseDragReleased -> {
                    resizeRightMove.setFinalWidth(diagram.getRootContainer().getWidth());
                    System.out.println(resizeRightMove);
                    undoStack.push(resizeRightMove);
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
                diagramController.updateVariablesTable(selectedClassDiagram, workspace.variablesTable);
                
                
                

                workspace.disableButtons(false);

                //if the user clicked twice on the diagram, ask them to add API classes
                if (mouseClicked.getClickCount() == 2) {
                    System.out.println("Twice");

                    AppOptionDialog newDialog = new AppOptionDialog();
                    newDialog.init(app.getGUI().getWindow(), selectedClassDiagram);
                    newDialog.show();
                }

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
                    ScrollPane scrollPane = getRenderingScrollPane();

                    //dynamic scrolling 
                    if (x > canvas.getWidth() - 150) {
                        canvas.setMinWidth(canvas.getWidth() + 500);
                        canvas.setPrefWidth(canvas.getWidth() + 500);
                        if (workspace.gridIsActive()) {
                            gridEditController.renderGridLines(canvas);
                        }
                    }
                    if (y > canvas.getHeight() - 300) {
                        canvas.setMinHeight(canvas.getHeight() + 500);
                        canvas.setPrefHeight(canvas.getHeight() + 500);
                        if (workspace.gridIsActive()) {
                            gridEditController.renderGridLines(canvas);
                        }
                    }
                }
            }

            //when the mouse is released, push the move diagram action on the stack
            diagram.getRootContainer().setOnMouseReleased(e -> {
                moveDiagramEvent.setFinalPosition(diagram.getRootContainer().getLayoutX(), diagram.getRootContainer().getLayoutX());
                undoStack.push(moveDiagramEvent);
            });
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

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export to java code");
        File file = directoryChooser.showDialog(window);

        //get the distinct package names to avoid package with same names
        ArrayList<String> distinctPackages = getDistinctPackages();

        for (String packageName : distinctPackages) {
            ArrayList<ClassDiagramObject> insideCorrectPackage = findByPackageName(packageName);
            //replace all the . with / to prevent issues in file management and make the children directory
            packageName = packageName.replace(".", "/");
            File directory = new File(file.getPath() + "/src/" + packageName);
            directory.mkdirs();
            for (ClassDiagramObject diagramToExtract : insideCorrectPackage) {
                File javaFile = new File(directory, diagramToExtract.getClassNameText().getText() + ".java");

                try {
                    PrintWriter myWriter = new PrintWriter(javaFile.getPath(), "UTF-8");
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

    /**
     * Looks for classes with a particular package
     *
     * @param packageName
     * @return A list of classes in that package
     */
    public ArrayList<ClassDiagramObject> findByPackageName(String packageName) {
        ArrayList<ClassDiagramObject> legitList = new ArrayList<>();

        for (ClassDiagramObject diagrams : classesOnCanvas) {
            if (diagrams.getPackageNameText().getText().equals(packageName)) {
                legitList.add(diagrams);
            }
        }

        return legitList;
    }

    /**
     * If the user wants to add a variable
     */
    public void handleVariableIncrement() {
        if (selectedClassDiagram != null) {

            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            System.out.println("Variable Increment called");

            VariableOptionDialog newDialog = new VariableOptionDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassDiagram, workspace.variablesTable);
            newDialog.show();

            //diagramController.updateVariablesTable(selectedClassDiagram, workspace.variablesTable);
        }
    }

    /**
     * If the user wants to delete a variable
     */
    public void handleVariableDecrement() {
        if (selectedClassDiagram != null) {

            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            System.out.println("Variable Decrement called");

            VariableRemoveDialog newDialog = new VariableRemoveDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassDiagram, workspace.variablesTable);
            newDialog.show();

            //diagramController.updateVariablesTable(selectedClassDiagram, workspace.variablesTable);
        }
    }

    public void handleUndo() {
        if (selectedClassDiagram != null) {
            restoreSelectedProperties(selectedClassDiagram);
            selectedClassDiagram = null;

            ((Workspace) app.getWorkspaceComponent()).disableButtons(true);
        }

        if (undoStack.size() > 0) {
            //if the user wants to resize
            if (undoStack.peek().getActionType().equals(RESIZE_RIGHT)) {
                ResizeRight resizeRightMove = (ResizeRight) undoStack.pop();
                ClassDiagramObject diagram = resizeRightMove.getDiagram();
                actionController.handleResizeRightUndo(resizeRightMove.getInitialWidth(), diagram);
            } //if the user wants to remove the diagram
            else if (undoStack.peek().getActionType().equals(MOVE_DIAGRAM)) {
                System.out.println("MOVE DIAGRAM UNDO");
                MoveDiagram moveDiagramAction = (MoveDiagram) undoStack.pop();
                ClassDiagramObject diagram = moveDiagramAction.getDiagram();
                actionController.handleMoveDiagramUndo(moveDiagramAction.getInitialPositionX(), moveDiagramAction.getInitialPositionY(), diagram);
            }
        }
    }

    @Override
    public void reset() {
        //remove all the children
        classesOnCanvas.clear();
        //remove all the actions from the undo stack
        undoStack.clear();
        ((Workspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
}
