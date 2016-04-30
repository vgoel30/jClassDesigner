/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.controller.DiagramController;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;

/**
 * Custom dialog box for when the user wants to add a variable
 *
 * @author varungoel
 */
public class MethodOptionDialog extends Stage {

    // HERE'S THE SINGLETON
    static MethodOptionDialog singleton;

    static final String PRIVATE = "private";
    static final String PUBLIC = "public";
    static final String DEFAULT = "default";
    static final String PROTECTED = "protected";

    Scene mainScene;

    //the main pane
    VBox mainPane;

    HBox nameContainer;
    Label nameLabel;
    TextField nameField;

    HBox typeContainer;
    Label typeLabel;
    TextField typeField;

    HBox staticContainer;
    Label staticLabel;
    CheckBox staticCheckBox;

    HBox abstractContainer;
    Label abstractLabel;
    CheckBox abstractCheckBox;

    HBox accessContainer;
    Label accessLabel;
    ChoiceBox<String> accessChoiceBox;

    HBox argumentContainer;

    Label argumentNameLabel;
    Label argumentTypeLabel;

    TextField argNameField;
    TextField argTypeField;

    Button argumentsButton;
    Button doneButton;

    ArrayList<HBox> argumentContainers = new ArrayList<>();
    ArrayList<TextField> argumentNameFields = new ArrayList<>();
    ArrayList<TextField> argumentTypeFields = new ArrayList<>();

    public MethodOptionDialog() {
    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static MethodOptionDialog getSingleton() {
        if (singleton == null) {
            singleton = new MethodOptionDialog();
        }
        return singleton;
    }

    public void init(Stage primaryStage, ClassDiagramObject diagram, TableView methodsTable, DataManager dataManager) {
        DiagramController diagramController = new DiagramController();

        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        mainPane = new VBox(15);

        nameContainer = new HBox(10);
        nameLabel = new Label("Method Name");
        nameField = new TextField();
        nameContainer.getChildren().add(nameLabel);
        nameContainer.getChildren().add(nameField);
        mainPane.getChildren().add(nameContainer);

        typeContainer = new HBox(10);
        typeLabel = new Label("Return Type ");
        typeField = new TextField();
        typeContainer.getChildren().add(typeLabel);
        typeContainer.getChildren().add(typeField);
        mainPane.getChildren().add(typeContainer);

        staticContainer = new HBox(10);
        staticLabel = new Label("Static      ");
        staticCheckBox = new CheckBox();
        staticContainer.getChildren().add(staticLabel);
        staticContainer.getChildren().add(staticCheckBox);
        mainPane.getChildren().add(staticContainer);

        abstractContainer = new HBox(10);
        abstractLabel = new Label("Abstract ");
        abstractCheckBox = new CheckBox();
        abstractContainer.getChildren().add(abstractLabel);
        abstractContainer.getChildren().add(abstractCheckBox);
        mainPane.getChildren().add(abstractContainer);

        accessContainer = new HBox(10);
        accessLabel = new Label("Access");
        accessChoiceBox = new ChoiceBox<>();
        accessChoiceBox.getItems().add(PRIVATE);
        accessChoiceBox.getItems().add(PUBLIC);
        accessChoiceBox.getItems().add(DEFAULT);
        accessChoiceBox.getItems().add(PROTECTED);
        accessChoiceBox.getSelectionModel().selectFirst();

        accessContainer.getChildren().add(accessLabel);
        accessContainer.getChildren().add(accessChoiceBox);
        mainPane.getChildren().add(accessContainer);

        //making the input for arguments
        argumentContainer = new HBox(10);

        argumentNameLabel = new Label("Argument Name");
        argNameField = new TextField();

        argumentContainer.getChildren().add(argumentNameLabel);
        argumentContainer.getChildren().add(argNameField);
        argumentNameFields.add(argNameField);

        argumentTypeLabel = new Label("Argument Type");
        argTypeField = new TextField();
        argumentTypeFields.add(argTypeField);

        argumentContainer.getChildren().add(argumentTypeLabel);
        argumentContainer.getChildren().add(argTypeField);
        argumentContainers.add(argumentContainer);

        mainPane.getChildren().add(argumentContainer);
        //arguments inputs done

        doneButton = new Button("Done");
        mainPane.getChildren().add(doneButton);

        argumentsButton = new Button("Add argument");
        mainPane.getChildren().add(mainPane.getChildren().size() - 1, argumentsButton);

        //when the user has clicked the done button, close this box and send the method's data back to the workspace 
        EventHandler doneHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            String name = nameField.getText();
            String type = typeField.getText();
            if (type.equals("")) {
                type = "void";
            }

            boolean isStatic;
            if (staticCheckBox.isSelected()) {
                isStatic = true;
            } else {
                isStatic = false;
            }

            boolean isAbstract;
            if (abstractCheckBox.isSelected()) {
                isAbstract = true;
            } else {
                isAbstract = false;
            }

            String access = accessChoiceBox.getValue();

            ArrayList<ArgumentObject> arguments = new ArrayList<>();

            //get all the arguments
            for (int i = 0; i < argumentContainers.size(); i++) {
                String argumentNameToAdd = argumentNameFields.get(i).getText();
                String argumentTypeToAdd = argumentTypeFields.get(i).getText();

                if (!argumentNameToAdd.equals("") || !argumentTypeToAdd.equals("")) {
                    ArgumentObject argumentToAdd = new ArgumentObject(argumentNameToAdd, argumentTypeToAdd);
                    arguments.add(argumentToAdd);
                }
            }

            //create the method object to add
            if (!name.equals("")) {
                MethodObject toAdd = new MethodObject(name, isStatic, isAbstract, arguments, type, access);
                System.out.println("METHOD TO ADD : " + toAdd.toStringCode());
                //this will add the method to the class's list of methods and render it
                diagramController.addMethod(diagram, toAdd);
                //this will update the method table
                diagramController.updateMethodsTable(diagram, methodsTable);
                MethodOptionDialog.this.hide();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid method name");
                alert.setHeaderText(null);
                alert.setContentText("Specify proper method name");

                alert.showAndWait();

                MethodOptionDialog.this.hide();
            }
        };

        //if the user wants to add an argument
        EventHandler argumentAdditionHander = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            HBox newArgumentContainer = new HBox(10);

            Label newArgumentNameLabel = new Label("Argument Name");
            TextField newArgNameField = new TextField();

            newArgumentContainer.getChildren().add(newArgumentNameLabel);
            newArgumentContainer.getChildren().add(newArgNameField);

            Label newArgumentTypeLabel = new Label("Argument Type");
            TextField newArgTypeField = new TextField();

            newArgumentContainer.getChildren().add(newArgumentTypeLabel);
            newArgumentContainer.getChildren().add(newArgTypeField);

            mainPane.getChildren().add(mainPane.getChildren().size() - 2, newArgumentContainer);

            //add all the relative information in the lists
            argumentContainers.add(newArgumentContainer);
            argumentNameFields.add(newArgNameField);
            argumentTypeFields.add(newArgTypeField);
        };

        doneButton.setOnAction(doneHandler);
        argumentsButton.setOnAction(argumentAdditionHander);

        // MAKE IT LOOK NICE
        mainPane.setPadding(new Insets(10, 20, 20, 20));
        mainPane.setMinHeight(150);

        // AND PUT IT IN THE WINDOW
        mainScene = new Scene(mainPane);
        this.setScene(mainScene);

    }

}
