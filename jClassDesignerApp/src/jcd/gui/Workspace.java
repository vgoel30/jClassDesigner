/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import static jcd.PropertyType.ADD_CLASS_ICON;
import static jcd.PropertyType.ADD_CLASS_TOOLTIP;
import static jcd.PropertyType.ADD_INTERFACE_ICON;
import static jcd.PropertyType.ADD_INTERFACE_TOOLTIP;
import static jcd.PropertyType.CODE_ICON;
import static jcd.PropertyType.CODE_TOOLTIP;
import static jcd.PropertyType.DECREMENT_ICON;
import static jcd.PropertyType.DECREMENT_TOOLTIP;
import static jcd.PropertyType.GRID_ICON;
import static jcd.PropertyType.GRID_TOOLTIP;
import static jcd.PropertyType.INCREMENT_ICON;
import static jcd.PropertyType.INCREMENT_TOOLTIP;
import static jcd.PropertyType.PHOTO_ICON;
import static jcd.PropertyType.PHOTO_TOOLTIP;
import static jcd.PropertyType.REDO_ICON;
import static jcd.PropertyType.REDO_TOOLTIP;
import static jcd.PropertyType.REMOVE_ICON;
import static jcd.PropertyType.REMOVE_TOOLTIP;
import static jcd.PropertyType.RESIZE_ICON;
import static jcd.PropertyType.RESIZE_TOOLTIP;
import static jcd.PropertyType.SELECTION_TOOL_ICON;
import static jcd.PropertyType.SELECTION_TOOL_TOOLTIP;
import static jcd.PropertyType.SNAP_ICON;
import static jcd.PropertyType.SNAP_TOOLTIP;
import static jcd.PropertyType.UNDO_ICON;
import static jcd.PropertyType.UNDO_TOOLTIP;
import static jcd.PropertyType.ZOOM_IN_ICON;
import static jcd.PropertyType.ZOOM_IN_TOOLTIP;
import static jcd.PropertyType.ZOOM_OUT_ICON;
import static jcd.PropertyType.ZOOM_OUT_TOOLTIP;
import jcd.controller.DiagramController;
import jcd.controller.GridEditController;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import maf.AppTemplate;
import static maf.components.AppStyleArbiter.CHECKBOX;
import static maf.components.AppStyleArbiter.CLASS_FILE_BUTTON;
import static maf.components.AppStyleArbiter.EDIT_TOOLBAR_ROW;
import static maf.components.AppStyleArbiter.RENDERING_CANVAS;
import maf.components.AppWorkspaceComponent;
import maf.ui.AppGUI;

/**
 *
 * @author varungoel
 */
public final class Workspace extends AppWorkspaceComponent {

    static final int BUTTON_TAG_WIDTH = 75;
    static final String CLASS_GRID_CANVAS = "grid_canvas";

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    Scene mainScene;

    //all the rows in the editing toolbar
    ArrayList<HBox> containers = new ArrayList<>();

    ArrayList<Button> buttonsInEditBar = new ArrayList<>();

    ArrayList<Button> toolbarButtons = new ArrayList<>();
    Button selectionButton;
    Button resizeButton;
    Button addClassButton;
    Button addInterfaceButton;
    public Button removeButton;
    Button undoButton;
    Button redoButton;
    Button zoomInButton;
    Button zoomOutButton;
    Button screenshotButton;
    Button codeButton;

    HBox gridButtonContainer;
    Button gridButton;
    CheckBox gridCheckBox;

    HBox snapButtonContainer;
    Button snapButton;
    CheckBox snapCheckBox;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;

    //1st row
    HBox classNameContainer;
    Label classNameLabel;
    public TextField classNameField;

    //2nd row
    HBox packageNameContainer;
    Label packageNameLabel;
    public TextField packageNameField;

    //3rd row
    HBox parentSelectionContainer;
    Label parentNameLabel;
    public ComboBox<String> parentNamePicker;

    //temp 4th row
    //the add interfaces/package row
    HBox interfaceSelectionContainer;
    Button localInterfaceButton;
    Button externalInterfaceButton;
    Button addPackageButton;

//4th row which has the variables increase/decrease control and the table
    VBox fourthRow;
    HBox variablesContainer;
    Label variablesLabel;
    Button variablesIncrementButton;
    Button variablesDecrementButton;
    public TableView<VariableObject> variablesTable;

    //5th row which has the methods increase/decrease control and the table
    VBox fifthRow;
    HBox methodsContainer;
    Label methodsLabel;
    Button methodsIncrementButton;
    Button methodsDecrementButton;
    public TableView<MethodObject> methodsTable;

    //THE AREA WHERE ALL THE STUFF WILL BE RENDERED
    Pane canvas;
    ScrollPane canvasScrollPane;

    //keep track of the current selected button
    Button selected;

    //BOOLEAN TO SEE IF SELECTION IS ACTIVE
    public boolean selectionActive;
    //DRAWING SHAPES
    public boolean drawingActive;
    //SELECTING A SHAPE AND RESIZING IT
    public boolean resizingActive;

    // HERE ARE THE CONTROLLERS
    GridEditController gridEditController;
    DataManager dataManager;
    DiagramController diagramController;

    public Workspace(AppTemplate initApp) throws IOException, Exception {
        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER        
        gui = app.getGUI();
        dataManager = (DataManager) app.getDataComponent();

        layoutGUI();
        setupHandlers();

        mainScene = new Scene(workspace);
    }

    public Pane getCanvas() {
        return canvas;
    }

    public ScrollPane getCanvasScrollPane() {
        return canvasScrollPane;
    }

    public Scene getScene() {
        return mainScene;
    }

    public boolean gridIsActive() {
        return gridCheckBox.isSelected();
    }

    public boolean snapIsActive() {
        return snapCheckBox.isSelected();
    }

    public ComboBox<String> getParentNamePicker() {
        return parentNamePicker;
    }

    public void layoutGUI() {
        FlowPane toolBarPane = gui.getToolbarPane();

        //SETTING UP ALL THE BUTTONS
        selectionButton = gui.initChildButton(toolBarPane, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        toolbarButtons.add(selectionButton);
        resizeButton = gui.initChildButton(toolBarPane, RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), false);
        toolbarButtons.add(resizeButton);
        addClassButton = gui.initChildButton(toolBarPane, ADD_CLASS_ICON.toString(), ADD_CLASS_TOOLTIP.toString(), false);
        toolbarButtons.add(addClassButton);
        addInterfaceButton = gui.initChildButton(toolBarPane, ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), false);
        toolbarButtons.add(addInterfaceButton);
        removeButton = gui.initChildButton(toolBarPane, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
        toolbarButtons.add(removeButton);
        undoButton = gui.initChildButton(toolBarPane, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), false);
        toolbarButtons.add(undoButton);
        redoButton = gui.initChildButton(toolBarPane, REDO_ICON.toString(), REDO_TOOLTIP.toString(), false);
        toolbarButtons.add(redoButton);
        zoomInButton = gui.initChildButton(toolBarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        toolbarButtons.add(zoomInButton);
        zoomOutButton = gui.initChildButton(toolBarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        toolbarButtons.add(zoomOutButton);
        screenshotButton = gui.initChildButton(toolBarPane, PHOTO_ICON.toString(), PHOTO_TOOLTIP.toString(), false);
        toolbarButtons.add(screenshotButton);
        codeButton = gui.initChildButton(toolBarPane, CODE_ICON.toString(), CODE_TOOLTIP.toString(), false);
        toolbarButtons.add(codeButton);

        gridButtonContainer = new HBox();
        toolBarPane.getChildren().add(gridButtonContainer);
        gridCheckBox = new CheckBox();
        gridButtonContainer.getChildren().add(gridCheckBox);
        gridButton = gui.initChildButton(gridButtonContainer, GRID_ICON.toString(), GRID_TOOLTIP.toString(), false);
        toolbarButtons.add(gridButton);

        snapButtonContainer = new HBox();
        toolBarPane.getChildren().add(snapButtonContainer);
        snapCheckBox = new CheckBox();
        snapButtonContainer.getChildren().add(snapCheckBox);
        snapButton = gui.initChildButton(snapButtonContainer, SNAP_ICON.toString(), SNAP_TOOLTIP.toString(), false);
        toolbarButtons.add(snapButton);

        //setting up the editing toolbar
        editToolbar = new VBox();
        //the first row
        classNameContainer = new HBox(40);
        classNameLabel = new Label("Class/Interface ");
        classNameField = new TextField();

        classNameContainer.getChildren().add(classNameLabel);
        classNameContainer.getChildren().add(classNameField);
        containers.add(classNameContainer);
        editToolbar.getChildren().add(classNameContainer);

        //the second row
        packageNameContainer = new HBox(70);
        packageNameLabel = new Label("Package      ");
        packageNameField = new TextField();
        packageNameContainer.getChildren().add(packageNameLabel);
        packageNameContainer.getChildren().add(packageNameField);
        containers.add(packageNameContainer);
        editToolbar.getChildren().add(packageNameContainer);

        //the third row
        parentSelectionContainer = new HBox(70);
        parentNameLabel = new Label("Parent         ");
        parentNamePicker = new ComboBox<>();
        parentNamePicker.setMaxWidth(210);
        parentNamePicker.setEditable(true);
        parentNamePicker.setValue("");
        parentSelectionContainer.getChildren().add(parentNameLabel);
        parentSelectionContainer.getChildren().add(parentNamePicker);
        containers.add(parentSelectionContainer);
        editToolbar.getChildren().add(parentSelectionContainer);

        //the interface thing row
        interfaceSelectionContainer = new HBox();

        localInterfaceButton = new Button("Local Interface");
        localInterfaceButton.setDisable(true);
        interfaceSelectionContainer.getChildren().add(localInterfaceButton);
        buttonsInEditBar.add(localInterfaceButton);

        externalInterfaceButton = new Button("External Interface");
        externalInterfaceButton.setDisable(true);
        interfaceSelectionContainer.getChildren().add(externalInterfaceButton);
        buttonsInEditBar.add(externalInterfaceButton);

        addPackageButton = new Button("Import");
        addPackageButton.setDisable(true);
        interfaceSelectionContainer.getChildren().add(addPackageButton);
        buttonsInEditBar.add(addPackageButton);

        containers.add(interfaceSelectionContainer);
        editToolbar.getChildren().add(interfaceSelectionContainer);

        //the 4th row;
        //the 4th row
        fourthRow = new VBox(10);
        variablesContainer = new HBox(78);
        variablesLabel = new Label("Variables:   ");
        variablesContainer.getChildren().add(variablesLabel);
        variablesIncrementButton = gui.putButtonInContainer(variablesContainer, INCREMENT_ICON.toString(), INCREMENT_TOOLTIP.toString(), false);
        variablesDecrementButton = gui.putButtonInContainer(variablesContainer, DECREMENT_ICON.toString(), DECREMENT_TOOLTIP.toString(), false);
        fourthRow.getChildren().add(variablesContainer);
        editToolbar.getChildren().add(fourthRow);

        variablesTable = new TableView<>();
        //variablesTable.getColumns().addAll(new TableColumn("Name"), new TableColumn("Type"), new TableColumn("Static"), new TableColumn("Access"));

        TableColumn<VariableObject, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn<VariableObject, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));

        TableColumn<VariableObject, Boolean> staticCol = new TableColumn<>("Static");
        staticCol.setCellValueFactory(new PropertyValueFactory("isStatic"));

        TableColumn<VariableObject, String> accessCol = new TableColumn<>("Access");
        accessCol.setCellValueFactory(new PropertyValueFactory("access"));

        TableColumn<VariableObject, Boolean> finalCol = new TableColumn<>("Final");
        finalCol.setCellValueFactory(new PropertyValueFactory("isFinal"));

        //adding all the columns
        variablesTable.getColumns().setAll(nameCol, typeCol, staticCol, accessCol, finalCol);

        ScrollPane variableScroll = new ScrollPane(variablesTable);
        fourthRow.getChildren().add(variableScroll);
        variablesTable.setMinWidth(400);
        variableScroll.setMaxWidth(420);

        //the 5th  row
        fifthRow = new VBox(10);
        methodsContainer = new HBox(78);
        methodsLabel = new Label("Methods:   ");
        methodsContainer.getChildren().add(methodsLabel);
        methodsIncrementButton = gui.putButtonInContainer(methodsContainer, INCREMENT_ICON.toString(), INCREMENT_TOOLTIP.toString(), false);
        methodsDecrementButton = gui.putButtonInContainer(methodsContainer, DECREMENT_ICON.toString(), DECREMENT_TOOLTIP.toString(), false);
        fifthRow.getChildren().add(methodsContainer);
        editToolbar.getChildren().add(fifthRow);
        methodsTable = new TableView();
        //methodsTable.getColumns().addAll(new TableColumn("Name"), new TableColumn("Return"), new TableColumn("Static"), new TableColumn("Abstract"), new TableColumn("Access"));

        TableColumn<MethodObject, String> methodNameCol = new TableColumn<>("Name");
        methodNameCol.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn<MethodObject, String> returnTypeCol = new TableColumn<>("Type");
        returnTypeCol.setCellValueFactory(new PropertyValueFactory("returnType"));

        TableColumn<MethodObject, String> methodAccessCol = new TableColumn<>("Access");
        methodAccessCol.setCellValueFactory(new PropertyValueFactory("access"));

        TableColumn<MethodObject, Boolean> isStaticCol = new TableColumn<>("Static");
        isStaticCol.setCellValueFactory(new PropertyValueFactory("isStatic"));

        TableColumn<MethodObject, Boolean> isAbstractCol = new TableColumn<>("Abstract");
        isAbstractCol.setCellValueFactory(new PropertyValueFactory("isAbstract"));

        TableColumn<MethodObject, String> arg1Col = new TableColumn<>("Arguments");
        arg1Col.setCellValueFactory(new PropertyValueFactory("arguments"));

        //adding all the columns
        methodsTable.getColumns().setAll(methodNameCol, returnTypeCol, methodAccessCol, isStaticCol, isAbstractCol, arg1Col);

        ScrollPane methodsScroll = new ScrollPane(methodsTable);
        fifthRow.getChildren().add(methodsScroll);
        methodsTable.setMinWidth(400);
        methodsScroll.setMaxWidth(420);

        //the final row
        // AND NOW SETUP THE WORKSPACE
        workspace = new BorderPane();

        canvas = new Pane();
        canvasScrollPane = new ScrollPane();

        ((BorderPane) workspace).setLeft(editToolbar);
        ((BorderPane) workspace).setCenter(canvasScrollPane);
        canvasScrollPane.setContent(canvas);

    }

    public void setupHandlers() throws Exception {
        // MAKE THE GRID CONTROLLER	
        gridEditController = new GridEditController(app);
        diagramController = new DiagramController();

        //MAKE THE DIAGRAM CONTROLLER
        // MAKE THE EDIT CONTROLLER
        //when the user wants to add a class
        addClassButton.setOnAction(e -> {
            drawingActive = true;
            selectionActive = false;
            gridEditController.addDiagram(canvas, "class");
        });

        addInterfaceButton.setOnAction(e -> {
            drawingActive = true;
            selectionActive = false;
            gridEditController.addDiagram(canvas, "interface");
        });

        //when the selection button is clicked
        selectionButton.setOnAction(selectionButtonClicked -> {
            drawingActive = false;
            selectionActive = true;
            mainScene.getRoot().setCursor(Cursor.MOVE);
        });

        //add the variable
        variablesIncrementButton.setOnAction(variableIncrementClicked -> {
            drawingActive = false;
            dataManager.handleVariableIncrement();
        });

        //delete a variable
        variablesDecrementButton.setOnAction(variableDecrementClicked -> {
            drawingActive = false;
            dataManager.handleVariableDecrement();
        });

        //add a method
        methodsIncrementButton.setOnAction(methodIncrementClicked -> {
            drawingActive = false;
            dataManager.handleMethodIncrement();
        });

        //remove a method
        methodsDecrementButton.setOnAction(methodDecrementClicked -> {
            drawingActive = false;
            dataManager.handleMethodDecrement();
        });

       

        codeButton.setOnAction(codeButtonClicked -> {
            dataManager.handleExportCode(gui.getWindow());
        });

        undoButton.setOnAction(undoButtonClicked -> {
            drawingActive = false;
            selectionActive = false;
            dataManager.handleUndo();
        });

        redoButton.setOnAction(undoButtonClicked -> {
            drawingActive = false;
            selectionActive = false;
            dataManager.handleRedo();
        });

        removeButton.setOnAction(removeButtonClicked -> {
            drawingActive = false;
            dataManager.handleRemoval();
        });

        screenshotButton.setOnAction(screenshotButtonClicked -> {
            if (canvas.getChildren().size() > 0) {
                gridEditController.processSnapshot();
                drawingActive = false;
                selectionActive = false;
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Empty Canvas");
                alert.setHeaderText(null);
                alert.setContentText("Canvas is empty!");

                alert.showAndWait();
            }
        });

        gridCheckBox.setOnAction(e -> {
            if (gridCheckBox.isSelected()) {
                gridEditController.renderGridLines(canvas);
            } else {
                gridEditController.removeGridLines(canvas);
            }
        });

        snapCheckBox.setOnAction(e -> {
            //pass all the classes on canvas as the paramater
            if (snapCheckBox.isSelected()) {
                gridEditController.snapToGrid(dataManager.classesOnCanvas);
            }
        });

        //if the user wants to zoom in
        zoomInButton.setOnAction(e -> {
            if (canvas.getScaleX() >= 1.6 || canvas.getScaleY() >= 1.6) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Zoom In Warning");
                alert.setHeaderText(null);
                alert.setContentText("You have attained maximum zoom level. The only way now is back.");
                alert.show();
            } else {
                gridEditController.zoomIn(canvas);
            }
        });

        //if the user wants to zoom out
        zoomOutButton.setOnAction(e -> {
            if (canvas.getScaleX() <= 1 || canvas.getScaleY() <= 1) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Zoom Out Warning");
                alert.setHeaderText(null);
                alert.setContentText("You have attained minimum zoom level. The only way now is forward.");
                alert.show();
            } else {
                gridEditController.zoomOut(canvas);
            }
        });

        //the event handler for editing variables
        variablesTable.setRowFactory(tv -> {
            TableRow<VariableObject> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    VariableObject selectedVariable = row.getItem();
                    System.out.println(selectedVariable);

                    VariableEditDialog variableEditDialog = new VariableEditDialog();
                    //show the variable edit box
                    variableEditDialog.init(app.getGUI().getWindow(), (ClassDiagramObject) dataManager.selectedClassDiagram,
                            variablesTable, dataManager, canvas, selectedVariable);
                    variableEditDialog.show();
                }
            });
            return row;

        });

        //testing the event handler for text field
        classNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                dataManager.validateNameOfClass(oldValue, newValue);
            }
        });

        //when the enter key is clicked, validate the name of the package
        packageNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                dataManager.validateNameOfPackage(oldValue, newValue);
            }
        });

        parentNamePicker.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (dataManager.selectedClassDiagram instanceof ClassDiagramObject) {
                    diagramController.manageParentNameChange(t, t1, dataManager, (ClassDiagramObject) dataManager.selectedClassDiagram);
                }
            }
        });

        //the user wants to add a package to the class
        addPackageButton.setOnAction(e -> {
            if (dataManager.selectedClassDiagram instanceof ClassDiagramObject) {
                ClassDiagramObject selectedClassObject = (ClassDiagramObject) dataManager.selectedClassDiagram;
                selectedClassObject.getJavaAPI_Packages().remove("");
                AppOptionDialog newDialog = new AppOptionDialog();
                newDialog.init(app.getGUI().getWindow(), selectedClassObject);
                newDialog.show();
            }
        });

        //the user wants to add an external interface to the class
        externalInterfaceButton.setOnAction(e -> {
            if (dataManager.selectedClassDiagram instanceof ClassDiagramObject) {
                ClassDiagramObject selectedClassObject = (ClassDiagramObject) dataManager.selectedClassDiagram;
                selectedClassObject.getExternalInterfaces().remove("");
                ExternalInterfaceDialog newDialog = new ExternalInterfaceDialog();
                newDialog.init(app.getGUI().getWindow(), selectedClassObject, dataManager, canvas);
                newDialog.show();
            }
        });

        //the user wants to add a local interface
        localInterfaceButton.setOnAction(e -> {
            if (dataManager.selectedClassDiagram instanceof ClassDiagramObject) {
                ClassDiagramObject selectedClassObject = (ClassDiagramObject) dataManager.selectedClassDiagram;
                selectedClassObject.getLocalInterfaces().remove("");
                LocalInterfaceDialog newDialog = new LocalInterfaceDialog();
                newDialog.init(app.getGUI().getWindow(), selectedClassObject, dataManager, canvas);
                newDialog.show();
            }
        });

    }

    @Override
    public void reloadWorkspace() {
        ClassDiagramObject.counter = 0;
        classNameField.setText("");
        packageNameField.setText("");
        gridCheckBox.setSelected(false);
        snapCheckBox.setSelected(false);
        disableButtons(true);
        variablesTable.getItems().clear();
        methodsTable.getItems().clear();
        if (selected != null) {
            selected.getStyleClass().remove(BUTTON_PRESSED);
            selected = null;
        }
    }

    /**
     * Disable certain buttons depending on when selection is active or not
     *
     * @param disable
     */
    public void disableButtons(boolean disable) {
        classNameField.setDisable(disable);
        packageNameField.setDisable(disable);
        parentNamePicker.setDisable(disable);
        variablesIncrementButton.setDisable(disable);
        variablesDecrementButton.setDisable(disable);
        methodsIncrementButton.setDisable(disable);
        methodsDecrementButton.setDisable(disable);
        removeButton.setDisable(disable);
        variablesTable.setDisable(disable);
        methodsTable.setDisable(disable);
        localInterfaceButton.setDisable(disable);
        externalInterfaceButton.setDisable(disable);
        addPackageButton.setDisable(disable);
    }

    @Override
    public void initStyle() {

        //stylize the buttons in the toolbar
        for (Button button : toolbarButtons) {
            button.getStyleClass().add(CLASS_FILE_BUTTON);
            button.setOnMouseClicked(e -> {
                if (selected == null) {
                    selected = button;
                    if (selected != gridButton && selected != snapButton) {
                        selected.getStyleClass().add(BUTTON_PRESSED);
                    }

                } else {
                    selected.getStyleClass().remove(BUTTON_PRESSED);
                    selected = button;
                    if (selected != gridButton && selected != snapButton) {
                        selected.getStyleClass().add(BUTTON_PRESSED);
                    }
                }
            });
        }

        for (Button button : buttonsInEditBar) {
            button.getStyleClass().add(EDIT_BAR_BUTTON);

            button.setOnMouseEntered((MouseEvent mouseEnteredEvent) -> {
                button.getStyleClass().remove(EDIT_BAR_BUTTON);
                button.getStyleClass().add(EDIT_BAR_BUTTON_HOVERED);
            });

            button.setOnMouseExited(mouseExitedEvent -> {
                button.getStyleClass().add(EDIT_BAR_BUTTON);
                button.getStyleClass().remove(EDIT_BAR_BUTTON_HOVERED);
            });
        }

        gridCheckBox.getStyleClass().add(CHECKBOX);
        snapCheckBox.getStyleClass().add(CHECKBOX);

        for (HBox container : containers) {
            container.getStyleClass().add(EDIT_TOOLBAR_ROW);
        }
        fourthRow.getStyleClass().add(EDIT_TOOLBAR_ROW);
        fifthRow.getStyleClass().add(EDIT_TOOLBAR_ROW);
        canvas.getStyleClass().add(RENDERING_CANVAS);
        canvasScrollPane.getStyleClass().add(RENDERING_CANVAS);

        methodsTable.getStyleClass().add(TABLES);
        variablesTable.getStyleClass().add(TABLES);

        canvasScrollPane.setFitToHeight(true);
        canvasScrollPane.setFitToWidth(true);
        canvasScrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        canvasScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        disableButtons(true);
    }

}
