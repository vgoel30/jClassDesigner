/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import jcd.controller.GridEditController;
import jcd.data.DataManager;
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

    ArrayList<Button> toolbarButtons = new ArrayList<>();
    Button selectionButton;
    Button resizeButton;
    Button addClassButton;
    Button addInterfaceButton;
    Button removeButton;
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
    public ComboBox parentNamePicker;

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
    TableView methodsTable;

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

    public Scene getScene() {
        return mainScene;
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
        redoButton = gui.initChildButton(toolBarPane, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
        toolbarButtons.add(redoButton);
        zoomInButton = gui.initChildButton(toolBarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), true);
        toolbarButtons.add(zoomInButton);
        zoomOutButton = gui.initChildButton(toolBarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), true);
        toolbarButtons.add(zoomOutButton);
        screenshotButton = gui.initChildButton(toolBarPane, PHOTO_ICON.toString(), PHOTO_TOOLTIP.toString(), false);
        toolbarButtons.add(screenshotButton);
        codeButton = gui.initChildButton(toolBarPane, CODE_ICON.toString(), CODE_TOOLTIP.toString(), false);
        toolbarButtons.add(codeButton);

        gridButtonContainer = new HBox();
        toolBarPane.getChildren().add(gridButtonContainer);
        gridCheckBox = new CheckBox();
        gridCheckBox.setDisable(true);
        gridButtonContainer.getChildren().add(gridCheckBox);
        gridButton = gui.initChildButton(gridButtonContainer, GRID_ICON.toString(), GRID_TOOLTIP.toString(), true);
        toolbarButtons.add(gridButton);

        snapButtonContainer = new HBox();
        toolBarPane.getChildren().add(snapButtonContainer);
        snapCheckBox = new CheckBox();
        snapCheckBox.setDisable(true);
        snapButtonContainer.getChildren().add(snapCheckBox);
        snapButton = gui.initChildButton(snapButtonContainer, SNAP_ICON.toString(), SNAP_TOOLTIP.toString(), true);
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
        parentSelectionContainer = new HBox(75);
        parentNameLabel = new Label("Parent         ");
        parentNamePicker = new ComboBox();
        parentNamePicker.setEditable(true);
        parentNamePicker.setMaxWidth(210);
        parentSelectionContainer.getChildren().add(parentNameLabel);
        parentSelectionContainer.getChildren().add(parentNamePicker);
        containers.add(parentSelectionContainer);
        editToolbar.getChildren().add(parentSelectionContainer);

        //the 4th row
        fourthRow = new VBox(10);
        variablesContainer = new HBox(78);
        variablesLabel = new Label("Variables: ");
        variablesContainer.getChildren().add(variablesLabel);
        variablesIncrementButton = gui.putButtonInContainer(variablesContainer, INCREMENT_ICON.toString(), INCREMENT_TOOLTIP.toString(), false);
        variablesDecrementButton = gui.putButtonInContainer(variablesContainer, DECREMENT_ICON.toString(), DECREMENT_TOOLTIP.toString(), false);
        fourthRow.getChildren().add(variablesContainer);
        editToolbar.getChildren().add(fourthRow);
        
        variablesTable = new TableView<>();
        //variablesTable.getColumns().addAll(new TableColumn("Name"), new TableColumn("Type"), new TableColumn("Static"), new TableColumn("Access"));
        
        TableColumn<VariableObject, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn<VariableObject,String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        
        TableColumn<VariableObject,Boolean> staticCol = new TableColumn<>("Static");
        staticCol.setCellValueFactory(new PropertyValueFactory("isStatic"));
        
        TableColumn<VariableObject,String> accessCol = new TableColumn<>("Access");
        accessCol.setCellValueFactory(new PropertyValueFactory("access"));
        
        TableColumn<VariableObject,Boolean> finalCol = new TableColumn<>("Final");
        finalCol.setCellValueFactory(new PropertyValueFactory("isFinal"));
       
        //adding all the columns
        variablesTable.getColumns().setAll(nameCol,typeCol,staticCol,accessCol,finalCol);

        ScrollPane variableScroll = new ScrollPane(variablesTable);
        fourthRow.getChildren().add(variableScroll);
        variablesTable.setMinWidth(400);
        
        //Obser
//        ObservableList<VariableObject> value = FXCollections.observableArrayList();
//        value.add(new VariableObject("myMethod", "int", false, false, "private"));
//        
//        variablesTable.setItems(value);

        //the 5th and final row
        fifthRow = new VBox(10);
        methodsContainer = new HBox(78);
        methodsLabel = new Label("Methods:  ");
        methodsContainer.getChildren().add(methodsLabel);
        methodsIncrementButton = gui.putButtonInContainer(methodsContainer, INCREMENT_ICON.toString(), INCREMENT_TOOLTIP.toString(), false);
        methodsDecrementButton = gui.putButtonInContainer(methodsContainer, DECREMENT_ICON.toString(), DECREMENT_TOOLTIP.toString(), false);
        fifthRow.getChildren().add(methodsContainer);
        editToolbar.getChildren().add(fifthRow);
        methodsTable = new TableView();
       methodsTable.getColumns().addAll(new TableColumn("Name"), new TableColumn("Return"), new TableColumn("Static"), new TableColumn("Abstract"), new TableColumn("Access"));
       
       
// methodsTable.g
        ScrollPane methodsScroll = new ScrollPane(methodsTable);
        fifthRow.getChildren().add(methodsScroll);
        variablesTable.setMinWidth(400);

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

        //MAKE THE DIAGRAM CONTROLLER
        // MAKE THE EDIT CONTROLLER
        //when the user wants to add a class
        addClassButton.setOnAction(e -> {
            drawingActive = true;
            selectionActive = false;
            System.out.println("Add class was clicked");
            gridEditController.addDiagram(canvas, "class");
        });

        addInterfaceButton.setOnAction(e -> {
            drawingActive = true;
            selectionActive = false;
            System.out.println("Add interface was clicked");
            gridEditController.addDiagram(canvas, "interface");
        });

        //when the selection button is clicked
        selectionButton.setOnAction(selectionButtonClicked -> {
            drawingActive = false;
            selectionActive = true;
            System.out.println("Selection was clicked");
            mainScene.getRoot().setCursor(Cursor.MOVE);
        });

        variablesIncrementButton.setOnAction(variableIncrementClicked -> {
            drawingActive = false;
            selectionActive = true;
            dataManager.handleVariableIncrement();
        });

        //when the resize button is clicked
        resizeButton.setOnAction(resizeButtonClicked -> {
        });

        codeButton.setOnAction(codeButtonClicked -> {
            dataManager.handleExportCode(gui.getWindow());
        });

        undoButton.setOnAction(undoButtonClicked -> {
            drawingActive = false;
            selectionActive = false;
            dataManager.handleUndo();
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
    }

    @Override
    public void reloadWorkspace() {
        classNameField.setText("");
        packageNameField.setText("");
        disableButtons(true);
        variablesTable.getItems().clear();
        // canvas.getChildren().clear();
        if (selected != null) {
            selected.getStyleClass().remove("pressed");
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
    }

    @Override
    public void initStyle() {
        System.out.println(toolbarButtons.size());
        //stylize the buttons in the toolbar
        for (Button button : toolbarButtons) {
            button.getStyleClass().add(CLASS_FILE_BUTTON);
            button.setOnMouseClicked(e -> {
                if (selected == null) {
                    selected = button;
                    selected.getStyleClass().add(BUTTON_PRESSED);
                } else {
                    selected.getStyleClass().remove("pressed");
                    selected = button;
                    selected.getStyleClass().add("pressed");
                }
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
