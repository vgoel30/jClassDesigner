/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf.ui;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import maf.AppTemplate;
import maf.components.AppStyleArbiter;
import maf.controller.AppFileController;
import static maf.settings.AppPropertyType.APP_LOGO;
import static maf.settings.AppPropertyType.EXIT_ICON;
import static maf.settings.AppPropertyType.EXIT_TOOLTIP;
import static maf.settings.AppPropertyType.LOAD_ICON;
import static maf.settings.AppPropertyType.LOAD_TOOLTIP;
import static maf.settings.AppPropertyType.NEW_ICON;
import static maf.settings.AppPropertyType.NEW_TOOLTIP;
import static maf.settings.AppPropertyType.SAVE_AS_ICON;
import static maf.settings.AppPropertyType.SAVE_AS_TOOLTIP;
import static maf.settings.AppPropertyType.SAVE_ICON;
import static maf.settings.AppPropertyType.SAVE_TOOLTIP;
import static maf.settings.AppStartupConstants.FILE_PROTOCOL;
import static maf.settings.AppStartupConstants.PATH_IMAGES;
import properties_manager.PropertiesManager;

/**
 *
 * @author varungoel
 */
public class AppGUI implements AppStyleArbiter {
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS

    boolean workspaceActive;

    protected AppFileController fileController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI
    protected BorderPane appPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    protected FlowPane fileToolbarPane;
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button saveAsButton;
    protected Button exitButton;

  

    // HERE ARE OUR DIALOGS
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;
    protected String appTitle;

    /**
     * This constructor initializes the file toolbar for use.
     *
     * @param initPrimaryStage The window for this application.
     *
     * @param initAppTitle The title of this application, which will appear in
     * the window bar.
     *
     * @param app The app within this gui is used.
     */
    public AppGUI(Stage initPrimaryStage,
            String initAppTitle,
            AppTemplate app) {
        // SAVE THESE FOR LATER
        primaryStage = initPrimaryStage;
        appTitle = initAppTitle;

        // INIT THE TOOLBAR
        initFileToolbar(app);

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();

        System.out.println("Called");
    }

    public boolean getWorkspaceActive() {
        return workspaceActive;
    }

    /**
     * Accessor method for getting the application pane, within which all user
     * interface controls are ultimately placed.
     *
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() {
        return appPane;
    }

    public FlowPane getToolbarPane() {
        return fileToolbarPane;
    }

    /**
     * Accessor method for getting this application's primary stage's, scene.
     *
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() {
        return primaryScene;
    }

    /**
     * Accessor method for getting this application's window, which is the
     * primary stage within which the full GUI will be placed.
     *
     * @return This application's primary stage (i.e. window).
     */
    public Stage getWindow() {
        return primaryStage;
    }

    /**
     * This method is used to activate/deactivate toolbar buttons when they can
     * and cannot be used so as to provide foolproof design.
     *
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);

        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
        saveAsButton.setDisable(false);
        newButton.setDisable(false);
        loadButton.setDisable(false);
        exitButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /**
     * *************************************************************************
     */
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /**
     * *************************************************************************
     */
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar(AppTemplate app) {
        fileToolbarPane = new FlowPane();

        fileToolbarPane.setHgap(15);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbarPane, NEW_ICON.toString(), NEW_TOOLTIP.toString(), false);
        loadButton = initChildButton(fileToolbarPane, LOAD_ICON.toString(), LOAD_TOOLTIP.toString(), false);
        saveButton = initChildButton(fileToolbarPane, SAVE_ICON.toString(), SAVE_TOOLTIP.toString(), true);
        saveAsButton = initChildButton(fileToolbarPane, SAVE_AS_ICON.toString(), SAVE_AS_TOOLTIP.toString(), true);
        exitButton = initChildButton(fileToolbarPane, EXIT_ICON.toString(), EXIT_TOOLTIP.toString(), false);

        // AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        newButton.setOnAction(e -> {
            workspaceActive = true;
            fileController.handleNewRequest();
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        saveButton.setOnAction(e -> {
            fileController.handleSaveRequest();
        });
        saveAsButton.setOnAction(e -> {
            fileController.handleSaveAsRequest();
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);

        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(fileToolbarPane);
        primaryScene = new Scene(appPane);

        // SET THE APP ICON
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW AND OPEN THE WINDOW
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    /**
     * This is a public helper method for initializing a simple button with an
     * icon and tooltip and placing it into a toolbar.
     *
     * @param toolbar Toolbar pane into which to place this button.
     *
     * @param icon Icon image file name for the button.
     *
     * @param tooltip Tooltip to appear when the user mouses over the button.
     *
     * @param disabled true if the button is to start off disabled, false
     * otherwise.
     *
     * @return A constructed, fully initialized button placed into its
     * appropriate pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);

        // PUT THE BUTTON IN THE TOOLBAR
        HBox buttonContainer = new HBox();
        buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER);
        toolbar.getChildren().add(buttonContainer);
        buttonContainer.getChildren().add(button);

        buttonContainer.setMaxHeight(55);
        buttonContainer.setMinHeight(55);
        buttonContainer.setPrefHeight(55);

        //if the user hovers over a button, change the background. It shall be lit.
        buttonContainer.setOnMouseEntered((MouseEvent mouseEnteredEvent) -> {
            buttonContainer.getStyleClass().remove(CLASS_FILE_BUTTON_CONTAINER);
            buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER_HOVERED);
            buttonContainer.getChildren().get(0).getStyleClass().remove(CLASS_FILE_BUTTON);
            buttonContainer.getChildren().get(0).getStyleClass().add(CLASS_FILE_BUTTON_HOVERED);
        });

        buttonContainer.setOnMouseExited(mouseExitedEvent -> {
            buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER);
            buttonContainer.getStyleClass().remove(CLASS_FILE_BUTTON_CONTAINER_HOVERED);
            buttonContainer.getChildren().get(0).getStyleClass().add(CLASS_FILE_BUTTON);
            buttonContainer.getChildren().get(0).getStyleClass().remove(CLASS_FILE_BUTTON_HOVERED);
        });

        // AND RETURN THE COMPLETED 
        
        return button;
    }

    public Button initChildButton(HBox buttonContainer, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);

        // PUT THE BUTTON IN THE TOOLBAR
        //HBox buttonContainer = new HBox();
        buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER);
        buttonContainer.getChildren().add(button);

        buttonContainer.setMaxHeight(55);
        buttonContainer.setMinHeight(55);
        buttonContainer.setPrefHeight(55);

        //if the user hovers over a button, change the background. It shall be lit.
        buttonContainer.setOnMouseEntered((MouseEvent mouseEnteredEvent) -> {
            buttonContainer.getStyleClass().remove(CLASS_FILE_BUTTON_CONTAINER);
            buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER_HOVERED);
            buttonContainer.getChildren().get(0).getStyleClass().remove(CLASS_FILE_BUTTON);
            buttonContainer.getChildren().get(0).getStyleClass().add(CLASS_FILE_BUTTON_HOVERED);
        });

        buttonContainer.setOnMouseExited(mouseExitedEvent -> {
            buttonContainer.getStyleClass().add(CLASS_FILE_BUTTON_CONTAINER);
            buttonContainer.getStyleClass().remove(CLASS_FILE_BUTTON_CONTAINER_HOVERED);
            buttonContainer.getChildren().get(0).getStyleClass().add(CLASS_FILE_BUTTON);
            buttonContainer.getChildren().get(0).getStyleClass().remove(CLASS_FILE_BUTTON_HOVERED);
        });
        

        return button;
    }

    /**
     * This function specifies the CSS style classes for the controls managed by
     * this framework.
     */
    @Override
    public void initStyle() {
        fileToolbarPane.setMaxHeight(59);
        fileToolbarPane.setMinHeight(59);
        fileToolbarPane.setPrefHeight(59);
        fileToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);

        //ADD THE STYLE CLASSES FOR ALL THE BUTTON
//        for (Button button : fileToolbarButtons) {
//            button.getStyleClass().add(CLASS_FILE_BUTTON);
//        }
        newButton.getStyleClass().add(CLASS_FILE_BUTTON);
        loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveAsButton.getStyleClass().add(CLASS_FILE_BUTTON);
        exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
        
       
    }

    
}
