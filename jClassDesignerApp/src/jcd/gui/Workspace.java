/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.awt.Color;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
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
import static jcd.PropertyType.GRID_ICON;
import static jcd.PropertyType.GRID_TOOLTIP;
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
import maf.AppTemplate;
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
    
    //all the rows in the editing toolbar
    ArrayList<HBox> containers = new ArrayList<>();

    //ArrayList<Button> toolbarButtons = new ArrayList<>();
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
    TextField classNameField;
    
    //2nd row
    HBox packageNameContainer;
    Label packageNameLabel;
    TextField packageNameField;
    
    
    //THE AREA WHERE ALL THE STUFF WILL BE RENDERED
    Pane canvas;

    public Workspace(AppTemplate initApp) throws IOException {
        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER        
        gui = app.getGUI();

        layoutGUI();
//	setupHandlers();
    }

    public void layoutGUI() {
        FlowPane toolBarPane = gui.getToolbarPane();

        //SETTING UP ALL THE BUTTONS
        selectionButton = gui.initChildButton(toolBarPane, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), true);
        resizeButton = gui.initChildButton(toolBarPane, RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), true);
        addClassButton = gui.initChildButton(toolBarPane, ADD_CLASS_ICON.toString(), ADD_CLASS_TOOLTIP.toString(), false);
        addClassButton.getStylesheets().add(CLASS_FILE_BUTTON);
        addInterfaceButton = gui.initChildButton(toolBarPane, ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), false);
        removeButton = gui.initChildButton(toolBarPane, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
        undoButton = gui.initChildButton(toolBarPane, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);
        redoButton = gui.initChildButton(toolBarPane, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
        zoomInButton = gui.initChildButton(toolBarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), true);
        zoomOutButton = gui.initChildButton(toolBarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), true);
        screenshotButton = gui.initChildButton(toolBarPane, PHOTO_ICON.toString(), PHOTO_TOOLTIP.toString(), true);
        codeButton = gui.initChildButton(toolBarPane, CODE_ICON.toString(), CODE_TOOLTIP.toString(), true);

        gridButtonContainer = new HBox();
        toolBarPane.getChildren().add(gridButtonContainer);
        gridCheckBox = new CheckBox();
        gridCheckBox.setDisable(true);
        gridButtonContainer.getChildren().add(gridCheckBox);
        gridButton = gui.initChildButton(gridButtonContainer, GRID_ICON.toString(), GRID_TOOLTIP.toString(), true);

        snapButtonContainer = new HBox();
        toolBarPane.getChildren().add(snapButtonContainer);
        snapCheckBox = new CheckBox();
        snapCheckBox.setDisable(true);
        snapButtonContainer.getChildren().add(snapCheckBox);
        snapButton = gui.initChildButton(snapButtonContainer, SNAP_ICON.toString(), SNAP_TOOLTIP.toString(), true);
        
        //setting up the editing toolbar
        editToolbar = new VBox();
        //the first row
        classNameContainer = new HBox(75);
        classNameLabel = new Label("Class Name");
        classNameField = new TextField();
        classNameContainer.getChildren().add(classNameLabel);
        classNameContainer.getChildren().add(classNameField);
        containers.add(classNameContainer);
        editToolbar.getChildren().add(classNameContainer);
        
        //the second row
        packageNameContainer = new HBox(75);
        packageNameLabel = new Label("Package      ");
        packageNameField = new TextField();
        packageNameContainer.getChildren().add(packageNameLabel);
        packageNameContainer.getChildren().add(packageNameField);
        containers.add(packageNameContainer);
        editToolbar.getChildren().add(packageNameContainer);

        // AND NOW SETUP THE WORKSPACE
        workspace = new BorderPane();
        ((BorderPane) workspace).setRight(editToolbar);
        ((BorderPane) workspace).setCenter(canvas);
        
        
    }

    @Override
    public void reloadWorkspace() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initStyle() {
        selectionButton.getStyleClass().add(CLASS_FILE_BUTTON);
        resizeButton.getStyleClass().add(CLASS_FILE_BUTTON);
        addClassButton.getStyleClass().add(CLASS_FILE_BUTTON);
        addInterfaceButton.getStyleClass().add(CLASS_FILE_BUTTON);
        removeButton.getStyleClass().add(CLASS_FILE_BUTTON);
        undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        zoomInButton.getStyleClass().add(CLASS_FILE_BUTTON);
        zoomOutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        screenshotButton.getStyleClass().add(CLASS_FILE_BUTTON);
        codeButton.getStyleClass().add(CLASS_FILE_BUTTON);
        gridButton.getStyleClass().add(CLASS_FILE_BUTTON);
        snapButton.getStyleClass().add(CLASS_FILE_BUTTON);

        gridCheckBox.getStyleClass().add(CHECKBOX);
        snapCheckBox.getStyleClass().add(CHECKBOX);
        
        //editToolbar.getStyleClass().add(EDIT_TOOLBAR);
//        classNameContainer.getStyleClass().add(EDIT_TOOLBAR_ROW);
//        classNameContainer.setMinHeight(90);
//        
//        classNameLabel.getStyleClass().add(LABEL);
//        editToolbar.setMaxWidth(270);
//        editToolbar.setMinWidth(270);
//        editToolbar.setMaxHeight(1050);
        for(HBox container: containers){
            container.getStyleClass().add(EDIT_TOOLBAR_ROW);
        }
    }

}
