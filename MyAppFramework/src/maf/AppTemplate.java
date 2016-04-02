/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import maf.components.AppComponentsBuilder;
import maf.components.AppDataComponent;
import maf.components.AppFileComponent;
import maf.components.AppWorkspaceComponent;
import static maf.settings.AppPropertyType.APP_CSS;
import static maf.settings.AppPropertyType.APP_PATH_CSS;
import static maf.settings.AppPropertyType.APP_TITLE;
import static maf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_MESSAGE;
import static maf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_TITLE;
import static maf.settings.AppStartupConstants.PATH_DATA;
import static maf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import static maf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;
import static maf.settings.AppStartupConstants.WORKSPACE_PROPERTIES_FILE_NAME;
import maf.ui.AppGUI;
import maf.ui.AppMessageDialogSingleton;
import maf.ui.AppYesNoCancelDialogSingleton;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 *
 * @author varungoel
 */
public abstract class AppTemplate extends Application {

    // THIS CLASS USES A COMPONENT HIERARCHY DESIGN PATTERN, MEANING IT
    // HAS OBJECTS THAT CAN BE SWAPPED OUT FOR OTHER COMPONENTS
    // FIRST THERE IS THE COMPONENT FOR MANAGING CUSTOM APP DATA
    AppDataComponent dataComponent;

    // THEN THE COMPONENT FOR MANAGING CUSTOM FILE I/O
    AppFileComponent fileComponent;

    // AND THEN THE COMPONENT FOR THE GUI WORKSPACE
    AppWorkspaceComponent workspaceComponent;

    // THIS IS THE APP'S FULL JavaFX GUI. NOTE THAT ALL APPS WOULD
    // SHARE A COMMON UI EXCEPT FOR THE CUSTOM WORKSPACE
    AppGUI gui;

    // THIS METHOD MUST BE OVERRIDDEN WHERE THE CUSTOM BUILDER OBJECT
    // WILL PROVIDE THE CUSTOM APP COMPONENTS
    public abstract AppComponentsBuilder makeAppBuilderHook();

    // COMPONENT ACCESSOR METHODS
    public AppDataComponent getDataComponent() {
        return dataComponent;
    }

    public AppFileComponent getFileComponent() {
        return fileComponent;
    }

    public AppWorkspaceComponent getWorkspaceComponent() {
        return workspaceComponent;
    }

    public AppGUI getGUI() {
        return gui;
    }

    /**
     * This is where our Application begins its initialization, it will create
     * the WPM_GUI and initialize all of its components.
     *
     * @param primaryStage This application's window.
     */
    @Override
    public void start(Stage primaryStage) {
        // LET'S START BY INITIALIZING OUR DIALOGS
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        messageDialog.init(primaryStage);
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.init(primaryStage);

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        String saf_properites = SIMPLE_APP_PROPERTIES_FILE_NAME;
        String workspace_properites = WORKSPACE_PROPERTIES_FILE_NAME;

        try {
            // LOAD APP PROPERTIES, BOTH THE BASIC UI STUFF FOR THE FRAMEWORK
            // AND THE CUSTOM UI STUFF FOR THE WORKSPACE
            boolean success = loadProperties(saf_properites)
                    && loadProperties(workspace_properites);

            if (success) {
                String appTitle = props.getProperty(APP_TITLE);

                // GET THE CUSTOM BUILDER, AND USE IT TO INIT THE COMPONENTS
                AppComponentsBuilder builder = makeAppBuilderHook();
                fileComponent = builder.buildFileComponent();
                dataComponent = builder.buildDataComponent();

                // AND NOW THAT THE COMPONENTS HAVE BEEN INSTANTIATED
                // WE CAN INITIALIZE THE GUI
                gui = new AppGUI(primaryStage, appTitle, this);
                workspaceComponent = builder.buildWorkspaceComponent();

                // NOW INIT ALL THE STYLE
                initStylesheet();
                gui.initStyle();
                workspaceComponent.initStyle();
            }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
        } catch (Exception ex) {
            Logger.getLogger(AppTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This function sets up the stylesheet to be used for specifying all style
     * for this application. Note that it does not attach CSS style classes to
     * controls, that must be done separately.
     */
    public void initStylesheet() {
        // SELECT THE STYLESHEET
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String stylesheet = props.getProperty(APP_PATH_CSS);
        stylesheet += props.getProperty(APP_CSS);
        
        URL stylesheetURL = getClass().getResource(stylesheet);
        
        System.out.println("STYLESHEET URL: " + stylesheetURL);
        String stylesheetPath = stylesheetURL.toExternalForm();
        System.out.println("STYLESHEET: " + stylesheet);
        getGUI().getPrimaryScene().getStylesheets().add(stylesheetPath);
        getGUI().getPrimaryScene().getStylesheets().add("http://fonts.googleapis.com/css?family=Gafata");
    }
    
    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @param propertiesFileName The XML file containing properties to be loaded
     * in order to initialize the UI.
     *
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties(String propertiesFileName) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
        } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
            return false;
        }
    }
}
