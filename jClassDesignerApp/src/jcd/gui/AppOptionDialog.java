/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import jcd.data.ClassDiagramObject;

/**
 * Custom Dialog box to allow users to add new external packages
 * @author varungoel
 */
public class AppOptionDialog extends Stage {

    // HERE'S THE SINGLETON
    static AppOptionDialog singleton;

    // GUI CONTROLS FOR OUR DIALOG
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button addPackage;
    Button doneButton;
    String selection;

    TextField textField;

    ArrayList<TextField> textFields = new ArrayList<>();

    // CONSTANT CHOICES
    public static final String ADD_PACKAGE = "Add Package";
    //public static final String NO = "No";
    public static final String CANCEL = "Done";

    /**
     * Note that the constructor is private since it follows the singleton
     * design pattern.
     *
     */
    public AppOptionDialog() {
    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static AppOptionDialog getSingleton() {
        if (singleton == null) {
            singleton = new AppOptionDialog();
        }
        return singleton;
    }

    /**
     * This method initializes the singleton for use.
     *
     * @param primaryStage The window above which this dialog will be centered.
     * @param diagram
     */
    public void init(Stage primaryStage, ClassDiagramObject diagram) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label();
        
        

        textField = new TextField();
        textFields.add(textField);

        // YES, NO, AND CANCEL BUTTONS
        addPackage = new Button(ADD_PACKAGE);
        //noButton = new Button(NO);
        doneButton = new Button(CANCEL);

        // NOW ORGANIZE OUR BUTTONS
        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().add(addPackage);
        buttonBox.getChildren().add(doneButton);
        buttonBox.getChildren().add(0,textField);

        EventHandler addPackageHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            TextField newTextField = new TextField();
            textFields.add(newTextField);
            buttonBox.getChildren().add(0,newTextField);
        };
        
        System.out.println("JAVA APIS included: " + diagram.getJavaAPI_Packages().size());
        //this will display all the old packages that have been included for import
        for(String API : diagram.getJavaAPI_Packages()){
            textField = new TextField(API);
            textFields.add(textField);
            buttonBox.getChildren().add(0,textField);
        }
        
        //clear the APIs to add new ones
        diagram.getJavaAPI_Packages().clear();

        EventHandler doneHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            AppOptionDialog.this.hide();

            for (TextField textField : textFields) {
                String packageName = textField.getText();
                //add the API 
                if (packageName != "") {
                    diagram.addAPI(packageName);
                }
            }
        };

        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addPackage.setOnAction(addPackageHandler);
        doneButton.setOnAction(doneHandler);

        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(buttonBox);

        // MAKE IT LOOK NICE
        messagePane.setPadding(new Insets(10, 20, 20, 20));
        messagePane.setSpacing(10);

        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
    }

    /**
     * This method loads a custom message into the label then pops open the
     * dialog.
     *
     * @param title The title to appear in the dialog window bar.
     *
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
        // SET THE DIALOG TITLE BAR TITLE
        setTitle(title);

        // SET THE MESSAGE TO DISPLAY TO THE USER
        messageLabel.setText(message);

        // AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
        // WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
        // DO MORE WORK.
        showAndWait();
    }
}
