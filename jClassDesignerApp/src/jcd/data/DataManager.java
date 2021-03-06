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
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import jcd.actions.Action;
import jcd.actions.EditVariable;
import jcd.actions.MoveDiagram;
import jcd.actions.RemoveMethod;
import jcd.actions.RemoveVariable;
import jcd.actions.ResizeLeft;
import jcd.actions.ResizeRight;
import jcd.connector_lines.AggregateLine;
import jcd.connector_lines.ConnectorLine;
import jcd.connector_lines.DependencyLine;
import jcd.connector_lines.InheritanceLine;
import jcd.controller.ActionController;
import jcd.controller.DiagramController;
import jcd.controller.GridEditController;
import jcd.gui.MethodOptionDialog;
import jcd.gui.MethodRemoveDialog;
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
    public static final String RESIZE_LEFT = "resize_left";
    public static final String MOVE_DIAGRAM = "move_diagram";
    public static final String REMOVE_VARIABLE = "remove_variable";
    public static final String REMOVE_METHOD = "remove_method";
    public static final String EDIT_VARIABLE = "edit_variable";

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    //THE CURRENT SELECTED CLASS DIAGRAM
    public Diagram selectedClassDiagram = null;

    //THE CURRENT SELECTED LINE OBJECT
    public ConnectorLine selectedConnectorLine = null;

    //this will keep track of all the classes currently on the canvas
    public ArrayList<ClassDiagramObject> classesOnCanvas = new ArrayList<>();

    //name of all the external parents
    public ArrayList<String> externalParents = new ArrayList<>();
    //this will keep track of all the external parents on the canvas
    public ArrayList<ExternalParent> externalParentsOnCanvas = new ArrayList<>();

    //all the non-primitive data types that the class will use (for the has-a relationship)
    public ArrayList<String> externalDataTypes = new ArrayList<>();
    //all the external data type boxes on canvas
    public ArrayList<ExternalDataType> externalDataTypesOnCanvas = new ArrayList<>();

    //all the non-primitive data types that the class will use (for the uses relationship)
    public ArrayList<String> externalUseTypes = new ArrayList<>();
    //all the external use type boxes on canvas
    public ArrayList<ExternalUseType> externalUseTypesOnCanvas = new ArrayList<>();

    //all the packages to be imported
    public ArrayList<String> packageNames = new ArrayList<>();

    public Stack<Action> undoStack = new Stack<>();
    public Stack<Action> redoStack = new Stack<>();

    ActionController actionController;
    DiagramController diagramController;
    GridEditController gridEditController;

    public ArrayList<ClassDiagramObject> getClassesOnCanvas() {
        return classesOnCanvas;
    }

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     * @throws java.lang.Exception
     */
    public DataManager(AppTemplate initApp) throws Exception {
        // KEEP THE APP FOR LATER
        app = initApp;
        actionController = new ActionController(initApp);
        diagramController = new DiagramController();
        gridEditController = new GridEditController(initApp);
    }

    public void addClassDiagram(ClassDiagramObject diagramToAdd) {
        classesOnCanvas.add(diagramToAdd);
        packageNames.add(diagramToAdd.getPackageNameText().getText());

        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();

        //snaps the new diagram to grid if snap is active
        if (workspace.snapIsActive()) {
            gridEditController.snapToGrid(classesOnCanvas);
        }
    }

    public void addPackage(String packageName) {
        packageNames.add(packageName);
    }

    public Pane getRenderingPane() {
        return ((Workspace) app.getWorkspaceComponent()).getCanvas();
    }

    public ScrollPane getRenderingScrollPane() {
        return ((Workspace) app.getWorkspaceComponent()).getCanvasScrollPane();
    }

    public void attachClassDiagramEventHandlers(ClassDiagramObject diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        MoveDiagram moveDiagramEvent = new MoveDiagram(diagram);

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked((MouseEvent mouseClicked) -> {
            if (workspace.selectionActive) {
                diagram.getRootContainer().getStyleClass().add(SELECTED_DIAGRAM_CONTAINER);
                diagram.getRightLine().setVisible(true);
                diagram.getLeftLine().setVisible(true);
                //diagram.getBottomLine().setVisible(true);
                //diagram.getMiddleLine().setVisible(true);

                if (selectedClassDiagram != null && selectedClassDiagram instanceof ClassDiagramObject) {
                    restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                }

                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                    selectedConnectorLine = null;
                }

                //set the inital position of the mouse event
                moveDiagramEvent.setInitialPosition(diagram.getRootContainer().getLayoutX(), diagram.getRootContainer().getLayoutY());

                //all the event handlers for the resizing stuff
                attachResizingHandlers(diagram, workspace);

                selectedClassDiagram = diagram;
                //reflect the selected changes
                reflectChangesForSelectedDiagram(workspace, (ClassDiagramObject) selectedClassDiagram);

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

                    if (workspace.snapIsActive()) {
                        gridEditController.snapToGrid(classesOnCanvas);
                    }

                    double x = diagram.getRootContainer().getLayoutX();
                    double y = diagram.getRootContainer().getLayoutY();

                    Pane canvas = getRenderingPane();

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

                    if (diagram.children.size() > 0) {
                        for (int i = 0; i < diagram.children.size(); i++) {
                            InheritanceLine myLine = diagram.linesPointingTowards.get(i);
                            ClassDiagramObject child = (ClassDiagramObject) myLine.getStartDiagram();
                            myLine.updateTriangleHead(selectedClassDiagram, child, canvas);
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
     * For external boxes, interfaces,packages etc.
     *
     * @param diagram
     */
    public void attachExternalParentDiagramHandlers(ExternalParent diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked((MouseEvent mouseClicked) -> {
            if (workspace.selectionActive) {
                if (selectedClassDiagram != null) {
                    if (selectedClassDiagram instanceof ClassDiagramObject) {
                        restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                    }
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                    selectedConnectorLine = null;
                }
                selectedClassDiagram = diagram;
                workspace.disableButtons(true);
                workspace.removeButton.setDisable(false);
                reflectChangesForExternalBoxDiagrams(workspace, selectedClassDiagram);

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

                    if (workspace.snapIsActive()) {
                        gridEditController.snapToGrid(classesOnCanvas);
                    }

                    Pane canvas = getRenderingPane();

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

                    if (diagram.children.size() > 0) {
                        for (int i = 0; i < diagram.children.size(); i++) {
                            InheritanceLine myLine = diagram.parentalLines.get(i);
                            ClassDiagramObject child = (ClassDiagramObject) myLine.getStartDiagram();
                            myLine.updateTriangleHead(selectedClassDiagram, child, canvas);
                        }
                    }
                }
            }
        });
    }

    /**
     * Event handlers for the external data type box (has-a relationship)
     *
     * @param diagram
     */
    public void attachExternalDataTypeBoxHandlers(ExternalDataType diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked((MouseEvent mouseClicked) -> {
            if (workspace.selectionActive) {
                if (selectedClassDiagram != null) {
                    if (selectedClassDiagram instanceof ClassDiagramObject) {
                        restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                    }
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                    selectedConnectorLine = null;
                }
                selectedClassDiagram = diagram;
                workspace.disableButtons(true);
                workspace.removeButton.setDisable(false);
                reflectChangesForExternalBoxDiagrams(workspace, selectedClassDiagram);

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
                    if (workspace.snapIsActive()) {
                        gridEditController.snapToGrid(classesOnCanvas);
                    }

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
        });
    }

    /**
     * External use type box's handlers (uses-a relationship)
     *
     * @param diagram
     */
    public void attachExternalUseTypeBoxHandlers(ExternalUseType diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //if the diagram has been clicked
        diagram.getRootContainer().setOnMouseClicked((MouseEvent mouseClicked) -> {
            if (workspace.selectionActive) {
                if (selectedClassDiagram != null) {
                    if (selectedClassDiagram instanceof ClassDiagramObject) {
                        restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                    }
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                    selectedConnectorLine = null;
                }
                selectedClassDiagram = diagram;
                workspace.disableButtons(true);
                workspace.removeButton.setDisable(false);
                reflectChangesForExternalBoxDiagrams(workspace, selectedClassDiagram);

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

                    if (workspace.snapIsActive()) {
                        gridEditController.snapToGrid(classesOnCanvas);
                    }

                    Pane canvas = getRenderingPane();

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
        });
    }

    /**
     * Restores the appearance of the selected diagram after it has been
     * deselected
     *
     * @param selectedClassDiagram
     */
    public void restoreSelectedProperties(ClassDiagramObject selectedClassDiagram) {
        selectedClassDiagram.getRootContainer().getStyleClass().remove(SELECTED_DIAGRAM_CONTAINER);
        selectedClassDiagram.getLeftLine().setVisible(false);
        selectedClassDiagram.getRightLine().setVisible(false);
    }

    public void validateNameOfClass(String oldValue, String newValue) {
        if (selectedClassDiagram instanceof ClassDiagramObject) {
            ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;

            selectedClassObject.getClassNameText().setText(newValue);

            for (ClassDiagramObject diagram : classesOnCanvas) {
                if (diagram != selectedClassObject) {
                    int x = selectedClassObject.compareTo(diagram);
                    if (x == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Class name error");
                        alert.setHeaderText(null);
                        alert.setContentText("Class already exists in this package!");
                        alert.showAndWait();
                    }
                }
            }

        }
    }

    /**
     * Validates the name of the package for a class diagram object
     *
     * @param oldValue
     * @param newValue
     */
    public void validateNameOfPackage(String oldValue, String newValue) {
        packageNames.remove(oldValue);
        if (selectedClassDiagram instanceof ClassDiagramObject) {
            ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;
            if (selectedClassObject != null) {
                selectedClassObject.getPackageNameText().setText(newValue);

                for (ClassDiagramObject diagram : classesOnCanvas) {
                    if (diagram != selectedClassObject) {
                        int x = selectedClassObject.compareTo(diagram);
                        if (x == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Package name error");
                            alert.setHeaderText(null);
                            alert.setContentText("Class already exists in this package!");
                            alert.showAndWait();
                        }
                    }
                }
            }
        }
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
        ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;
        if (selectedClassObject != null) {

            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            VariableOptionDialog newDialog = new VariableOptionDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassObject, workspace.variablesTable, this, workspace.getCanvas());
            newDialog.show();
        }
    }

    /**
     * Adds a method
     */
    public void handleMethodIncrement() {
        ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;
        if (selectedClassObject != null) {

            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            MethodOptionDialog newDialog = new MethodOptionDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassObject, workspace.methodsTable, this);
            newDialog.show();

            diagramController.updateMethodsTable(selectedClassObject, workspace.methodsTable);
        }
    }

    /**
     * If the user wants to delete a variable
     */
    public void handleVariableDecrement() {
        ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;
        if (selectedClassObject != null) {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            VariableRemoveDialog newDialog = new VariableRemoveDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassObject, workspace.variablesTable, this, undoStack);
            redoStack.clear();
            newDialog.show();
        }
    }

    public void handleMethodDecrement() {
        ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;
        if (selectedClassObject != null) {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            MethodRemoveDialog newDialog = new MethodRemoveDialog();
            newDialog.init(app.getGUI().getWindow(), selectedClassObject, workspace.methodsTable, undoStack);
            redoStack.clear();
            newDialog.show();
        }
    }

    public void handleUndo() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        if (selectedClassDiagram instanceof ClassDiagramObject) {
            ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;

            if (selectedClassObject != null) {
                restoreSelectedProperties(selectedClassObject);

            }
        }
        selectedClassDiagram = null;
        ((Workspace) app.getWorkspaceComponent()).disableButtons(true);

        if (undoStack.size() > 0) {
            //if the user wants to resize
            if (undoStack.peek().getActionType().equals(RESIZE_RIGHT)) {
                ResizeRight resizeRightMove = (ResizeRight) undoStack.pop();
                redoStack.push(resizeRightMove);
                ClassDiagramObject diagram = resizeRightMove.getDiagram();
                actionController.handleResizeRightUndo(resizeRightMove.getInitialWidth(), diagram);
            } //if the user wants to remove the diagram
            else if (undoStack.peek().getActionType().equals(MOVE_DIAGRAM)) {
                System.out.println("MOVE DIAGRAM UNDO");
                MoveDiagram moveDiagramAction = (MoveDiagram) undoStack.pop();
                redoStack.push(moveDiagramAction);
                ClassDiagramObject diagram = moveDiagramAction.getDiagram();
                actionController.handleMoveDiagramUndo(moveDiagramAction.getInitialPositionX(), moveDiagramAction.getInitialPositionY(), diagram);
            } else if (undoStack.peek().getActionType().equals(RESIZE_LEFT)) {
                ResizeLeft resizeLeftMove = (ResizeLeft) undoStack.pop();
                redoStack.push(resizeLeftMove);
                ClassDiagramObject diagram = resizeLeftMove.getDiagram();
                actionController.handleResizeLeftUndo(resizeLeftMove.getInitialWidth(), resizeLeftMove.getInitialX(), diagram);
            } //if the user wants to undo the removal of a variable
            else if (undoStack.peek().getActionType().equals(REMOVE_VARIABLE)) {
                RemoveVariable removeVariableMove = (RemoveVariable) undoStack.pop();
                ClassDiagramObject diagram = removeVariableMove.getDiagram();
                redoStack.push(removeVariableMove);
                //adds the variable to the list of variables and renders it on the diagram
                diagramController.addVariable(diagram, removeVariableMove.getRemovedVariable(), this, getRenderingPane());
                //updates the variables table
                diagramController.updateVariablesTable(diagram, workspace.variablesTable);

            } //if the user wants to undo the removal of a method
            else if (undoStack.peek().getActionType().equals(REMOVE_METHOD)) {
                RemoveMethod removeMethodMove = (RemoveMethod) undoStack.pop();
                ClassDiagramObject diagram = removeMethodMove.getDiagram();
                //adds the method
                diagramController.addMethod(diagram, removeMethodMove.getRemovedMethod(), this);
                //updates the method table
                diagramController.updateMethodsTable(diagram, workspace.methodsTable);
            }
            else if (undoStack.peek().getActionType().equals(EDIT_VARIABLE)) {
                EditVariable editVariableMove = (EditVariable) undoStack.pop();
                ClassDiagramObject diagram = editVariableMove.getDiagram();
                
                VariableObject originalVariable = editVariableMove.getOriginalVariable();
                VariableObject editedVariable = editVariableMove.getEditedVariable();
                
                //adds the method
                diagramController.addVariable(diagram, originalVariable, this, getRenderingPane());
                diagramController.removeVariable(diagram, editedVariable, this);
                
                redoStack.push(editVariableMove);
                //updates the variable table
                diagramController.updateVariablesTable(diagram, workspace.variablesTable);
            }
        }

    }

    public void handleRedo() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        if (selectedClassDiagram instanceof ClassDiagramObject) {
            ClassDiagramObject selectedClassObject = (ClassDiagramObject) selectedClassDiagram;

            if (selectedClassObject != null) {
                restoreSelectedProperties(selectedClassObject);

            }
        }
        selectedClassDiagram = null;
        ((Workspace) app.getWorkspaceComponent()).disableButtons(true);

        if (redoStack.size() > 0) {
            if (redoStack.peek().getActionType().equals(RESIZE_RIGHT)) {
                ResizeRight resizeRightMove = (ResizeRight) redoStack.pop();
                undoStack.push(resizeRightMove);
                ClassDiagramObject diagram = resizeRightMove.getDiagram();
                actionController.handleResizeRightRedo(resizeRightMove.getFinalWidth(), diagram);
            } else if (redoStack.peek().getActionType().equals(RESIZE_LEFT)) {
                ResizeLeft resizeLeftMove = (ResizeLeft) redoStack.pop();
                undoStack.push(resizeLeftMove);
                ClassDiagramObject diagram = resizeLeftMove.getDiagram();
                actionController.handleResizeLeftRedo(resizeLeftMove.getFinalWidth(), resizeLeftMove.getFinalX(), diagram);
            } else if (redoStack.peek().getActionType().equals(MOVE_DIAGRAM)) {
                MoveDiagram moveDiagramAction = (MoveDiagram) redoStack.pop();
                undoStack.push(moveDiagramAction);
                ClassDiagramObject diagram = moveDiagramAction.getDiagram();
                actionController.handleMoveDiagramRedo(moveDiagramAction.getFinalPositionX(), moveDiagramAction.getFinalPositionY(), diagram);
            } else if (redoStack.peek().getActionType().equals(REMOVE_VARIABLE)) {
                RemoveVariable removeVariableMove = (RemoveVariable) redoStack.pop();
                undoStack.push(removeVariableMove);
                ClassDiagramObject diagram = removeVariableMove.getDiagram();
                VariableObject toRemove = removeVariableMove.getRemovedVariable();
                diagramController.removeVariable(diagram, toRemove, this);
            }
            else if (redoStack.peek().getActionType().equals(REMOVE_METHOD)) {
                RemoveMethod removeMethodMove = (RemoveMethod) redoStack.pop();
                undoStack.push(removeMethodMove);
                ClassDiagramObject diagram = removeMethodMove.getDiagram();
                MethodObject toRemove = removeMethodMove.getRemovedMethod();
                diagramController.removeMethod(diagram, toRemove);
            }
            else if (redoStack.peek().getActionType().equals(EDIT_VARIABLE)) {
                EditVariable editVariableMove = (EditVariable) redoStack.pop();
                ClassDiagramObject diagram = editVariableMove.getDiagram();
                undoStack.push(editVariableMove);
                VariableObject originalVariable = editVariableMove.getOriginalVariable();
                VariableObject editedVariable = editVariableMove.getEditedVariable();
                
                //adds the method
                diagramController.addVariable(diagram, editedVariable, this, getRenderingPane());
                diagramController.removeVariable(diagram, originalVariable, this);
                
                //updates the variable table
                diagramController.updateVariablesTable(diagram, workspace.variablesTable);
            }

        }
    }

    /**
     * When a shape is selected, we want to show the user it's relevant
     * information
     *
     * @param workspace
     * @param selectedClassDiagram
     */
    private void reflectChangesForSelectedDiagram(Workspace workspace, ClassDiagramObject selectedClassDiagram) {
        workspace.classNameField.setText(selectedClassDiagram.getClassNameText().getText());
        workspace.packageNameField.setText(selectedClassDiagram.getPackageNameText().getText());
        diagramController.updateVariablesTable(selectedClassDiagram, workspace.variablesTable);
        diagramController.updateMethodsTable(selectedClassDiagram, workspace.methodsTable);
        workspace.getParentNamePicker().setValue(selectedClassDiagram.getParentName());
        diagramController.updateParentNamePicker(selectedClassDiagram, workspace.getParentNamePicker(), classesOnCanvas);
    }

    private void reflectChangesForExternalBoxDiagrams(Workspace workspace, Diagram externalBox) {
        workspace.classNameField.setText(selectedClassDiagram.getName());
        workspace.packageNameField.setText("");
        workspace.getParentNamePicker().setValue("");
    }

    /**
     * Handles the removal of a diagram object
     */
    public void handleRemoval() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        //if we want to remove an external parent
        if (selectedClassDiagram instanceof ExternalParent) {
            ExternalParent parentToRemove = (ExternalParent) selectedClassDiagram;
            //iterate over all the lines coming to the diagram
            for (int i = 0; i < parentToRemove.parentalLines.size(); i++) {
                //remove the line from canvas
                parentToRemove.parentalLines.get(i).removeFromCanvas(workspace.getCanvas());

                if (parentToRemove.parentalLines.get(i).inheritanceChildLine != null) {
                    parentToRemove.parentalLines.get(i).inheritanceChildLine.removeFromCanvas(workspace.getCanvas());
                }
                if (parentToRemove.parentalLines.get(i).standardChildLine != null) {
                    parentToRemove.parentalLines.get(i).standardChildLine.removeFromCanvas(workspace.getCanvas());
                }

                //remove the line from the child's lines list
                parentToRemove.children.get(i).inheritanceLinesOut.remove(parentToRemove.parentalLines.get(i));
                //set the parent to null
                parentToRemove.children.get(i).setParentName("");
            }

            for (ClassDiagramObject classDiagramObject : classesOnCanvas) {
                classDiagramObject.getExternalInterfaces().remove(selectedClassDiagram.getName());
            }

            //remove the external parent from the list
            externalParentsOnCanvas.remove(parentToRemove);
            externalParents.remove(parentToRemove.name);
            //remove from canvas
            workspace.getCanvas().getChildren().remove(parentToRemove.getRootContainer());
        } //if the user wants to remove an external use type. It's on the user to make up
        else if (selectedClassDiagram instanceof ExternalUseType) {
            ExternalUseType useTypeToRemove = (ExternalUseType) selectedClassDiagram;

            for (DependencyLine dependencyLine : useTypeToRemove.emittedLines) {
                dependencyLine.removeFromCanvas(workspace.getCanvas());
            }

            useTypeToRemove.removeFromCanvas(workspace.getCanvas());
            //remove from the list of external use types
            externalUseTypes.remove(selectedClassDiagram.name);
            externalUseTypesOnCanvas.remove(useTypeToRemove);
        } else if (selectedClassDiagram instanceof ExternalDataType) {
            ExternalDataType dataTypeToRemove = (ExternalDataType) selectedClassDiagram;

            if (dataTypeToRemove.usedBy.isEmpty()) {
                dataTypeToRemove.removeFromCanvas(workspace.getCanvas());
                //remove from the list of external use types
                externalDataTypes.remove(selectedClassDiagram.name);
                externalDataTypesOnCanvas.remove(dataTypeToRemove);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Removal Error");
                alert.setHeaderText(null);
                alert.setContentText("This data type is being used and can't be removed");

                alert.showAndWait();
            }
        } //if we want to remove a class diagram object
        else if (selectedClassDiagram instanceof ClassDiagramObject) {

            ClassDiagramObject classDiagramObjectToRemove = (ClassDiagramObject) selectedClassDiagram;
            classesOnCanvas.remove(classDiagramObjectToRemove);

            //any classes which use this class as a parent
            for (ClassDiagramObject classOnCanvas : classesOnCanvas) {
                if (classOnCanvas.parent.equals(classDiagramObjectToRemove.toString())) {
                    classOnCanvas.setParentName("");
                }
                //if any class have this class as a child
                classOnCanvas.getChildren().remove(classDiagramObjectToRemove);
            }
            //remove all the lines coming out
            for (InheritanceLine inheritanceLine : classDiagramObjectToRemove.inheritanceLinesOut) {
                inheritanceLine.removeFromCanvas(getRenderingPane());
            }
            //remove all the lines point towards it (inheritance lines)
            for (InheritanceLine inheritanceLine : classDiagramObjectToRemove.linesPointingTowards) {
                inheritanceLine.removeFromCanvas(getRenderingPane());
            }
            //remove all the aggregate lines out
            for (AggregateLine aggregateLine : classDiagramObjectToRemove.aggregateLinesOut) {
                aggregateLine.removeFromCanvas(getRenderingPane());
            }
            //remove all the dependency line
            for (DependencyLine dependencyLine : classDiagramObjectToRemove.dependencyLinesOut) {
                dependencyLine.removeFromCanvas(getRenderingPane());
            }

            //for all the data types which show that they are being used by this class
            for (ExternalDataType externalDataType : externalDataTypesOnCanvas) {
                externalDataType.usedBy.remove(classDiagramObjectToRemove);
            }

            //for all the use types which show that they are being used by this class
            for (ExternalUseType externalUseType : externalUseTypesOnCanvas) {
                externalUseType.usedBy.remove(classDiagramObjectToRemove);
            }

            //removes the diagram from the canvas
            classDiagramObjectToRemove.removeFromCanvas(getRenderingPane());
        }

        selectedClassDiagram = null;
        workspace.disableButtons(true);
    }

    @Override
    public void reset() {
        selectedClassDiagram = null;
        selectedConnectorLine = null;
        //remove all the children
        classesOnCanvas.clear();
        packageNames.clear();
        //remove all the actions from the undo stack
        undoStack.clear();
        redoStack.clear();

        externalParents.clear();
        externalParentsOnCanvas.clear();

        externalDataTypes.clear();
        externalDataTypesOnCanvas.clear();

        ((Workspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void attachConnectorLineHandlers(ConnectorLine connectorLine) {

        //if it is an inheritance line
        if (connectorLine instanceof InheritanceLine) {
            InheritanceLine inheritanceLine = (InheritanceLine) connectorLine;
            inheritanceLine.setOnMouseClicked(e -> {
                ((Workspace) app.getWorkspaceComponent()).disableButtons(true);
                if (selectedClassDiagram != null && selectedClassDiagram instanceof ClassDiagramObject) {
                    restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                }
                selectedClassDiagram = null;
                selectedConnectorLine = inheritanceLine;

                inheritanceLine.setStroke(Color.web("#22A7F0"));

                if (e.getClickCount() == 2) {
                    inheritanceLine.handleDoubleClick(((Workspace) app.getWorkspaceComponent()).getCanvas());
                }

            });
        } else if (connectorLine instanceof AggregateLine) {
            AggregateLine aggregateLine = (AggregateLine) connectorLine;

            aggregateLine.setOnMouseClicked(e -> {
                ((Workspace) app.getWorkspaceComponent()).disableButtons(true);
                ((Workspace) app.getWorkspaceComponent()).removeButton.setDisable(false);
                if (selectedClassDiagram != null && selectedClassDiagram instanceof ClassDiagramObject) {
                    restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                }
                selectedClassDiagram = null;
                selectedConnectorLine = aggregateLine;

                aggregateLine.setStroke(Color.web("#22A7F0"));

                if (e.getClickCount() == 2) {
                    aggregateLine.handleDoubleClick(((Workspace) app.getWorkspaceComponent()).getCanvas());
                }
            });
        } else if (connectorLine instanceof DependencyLine) {
            DependencyLine dependencyLine = (DependencyLine) connectorLine;

            dependencyLine.setOnMouseClicked(e -> {
                ((Workspace) app.getWorkspaceComponent()).disableButtons(true);
                ((Workspace) app.getWorkspaceComponent()).removeButton.setDisable(false);
                if (selectedClassDiagram != null && selectedClassDiagram instanceof ClassDiagramObject) {
                    restoreSelectedProperties((ClassDiagramObject) selectedClassDiagram);
                }
                if (selectedConnectorLine != null) {
                    selectedConnectorLine.setStroke(Color.BLACK);
                }
                selectedClassDiagram = null;
                selectedConnectorLine = dependencyLine;

                dependencyLine.setStroke(Color.web("#22A7F0"));

                if (e.getClickCount() == 2) {
                    dependencyLine.handleDoubleClick(((Workspace) app.getWorkspaceComponent()).getCanvas());
                }
            });
        }
    }

    /**
     * Resizing handlers on the lines for resizing the class diagrams
     *
     * @param diagram
     * @param workspace
     */
    private void attachResizingHandlers(ClassDiagramObject diagram, Workspace workspace) {
        //if the user is resizing to the right, create a new resize right action
        ResizeRight resizeRightMove = new ResizeRight(diagram);

        //if the user is reszing to the left
        ResizeLeft resizeLeftMove = new ResizeLeft(diagram);

        //when the right line is pressed for the resizing, that's the initial width for resizing
        diagram.getRightLine().setOnMousePressed(mouseClickedEvent -> {
            resizeRightMove.setInitialWidth(diagram.getRootContainer().getWidth());

        });

        //when the left line is pressed for the resizing, that's the initial width for resizing
        diagram.getLeftLine().setOnMousePressed(mouseClickedEvent -> {
            resizeLeftMove.setInitialWidth(diagram.getRootContainer().getWidth());
            resizeLeftMove.setInitialX(diagram.getRootContainer().getLayoutX());
        });

        //RIGHT LINE EVENT HANDLERS
        diagram.getRightLine().setOnMouseDragged(mouseDraggedEvent -> {
            if (mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() >= 185 && mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX() <= 450) {
                diagram.getRootContainer().setPrefWidth(mouseDraggedEvent.getX() - diagram.getRootContainer().getLayoutX());
            }
        });

        //when the mouse has been released, we set the final width and final xand push the acion on the undo stack
        diagram.getLeftLine().setOnMouseReleased(mouseDragReleased -> {
            resizeLeftMove.setFinalWidth(diagram.getRootContainer().getWidth());
            resizeLeftMove.setFinalX(diagram.getRootContainer().getLayoutX());
            undoStack.push(resizeLeftMove);
            redoStack.clear();
        });

        //when the mouse has been released, we set the final width and push the acion on the undo stack
        diagram.getRightLine().setOnMouseReleased(mouseDragReleased -> {
            resizeRightMove.setFinalWidth(diagram.getRootContainer().getWidth());
            undoStack.push(resizeRightMove);
            redoStack.clear();
        });

        diagram.getRightLine().setOnMouseEntered(mouseEnteredEvent -> {
            workspace.getScene().getRoot().setCursor(Cursor.W_RESIZE);
        });

        diagram.getRightLine().setOnMouseExited(mouseEnteredEvent -> {
            workspace.getScene().getRoot().setCursor(Cursor.DEFAULT);
        });

        //LEFT LINE
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
    }

}
