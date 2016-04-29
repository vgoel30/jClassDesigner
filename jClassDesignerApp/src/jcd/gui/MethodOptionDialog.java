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

    Button doneButton;

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

    public void init(Stage primaryStage, ClassDiagramObject diagram, TableView methodsTable) {
        DiagramController diagramController = new DiagramController();
        
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        mainPane = new VBox(15);

        nameContainer = new HBox(10);
        nameLabel = new Label("Name");
        nameField = new TextField();
        nameContainer.getChildren().add(nameLabel);
        nameContainer.getChildren().add(nameField);
        mainPane.getChildren().add(nameContainer);

        typeContainer = new HBox(10);
        typeLabel = new Label("Type ");
        typeField = new TextField();
        typeContainer.getChildren().add(typeLabel);
        typeContainer.getChildren().add(typeField);
        mainPane.getChildren().add(typeContainer);

        staticContainer = new HBox(10);
        staticLabel = new Label("Static");
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

        doneButton = new Button("Add Method");
        mainPane.getChildren().add(doneButton);

        EventHandler doneHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            String name = nameField.getText();
            String type = typeField.getText();

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

            MethodObject toAdd = new MethodObject(name, isStatic, isAbstract, arguments, type, access);

            boolean alreadyExists = false;

            //see if the method already exists
//            for (VariableObject variable : diagram.getVariables()) {
//                if (variable.equals(toAdd)) {
//                    alreadyExists = true;
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Variable name error");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Variable already exists in this class!");
//                    alert.showAndWait();
//                    break;
//                }
//            }

            //if the variable doesn't already exist, add it to the list of variables
            if (!alreadyExists) {
                //adds the variable to the list of variables and renders it on the diagram
                
                
                //update the list of variables
                
                
            }
            System.out.println("METHOD TO ADD : " + toAdd);
            diagramController.addMethod(diagram,toAdd);
            diagramController.updateMethodsTable(diagram, methodsTable);
            MethodOptionDialog.this.hide();
        };

        doneButton.setOnAction(doneHandler);

        // MAKE IT LOOK NICE
        mainPane.setPadding(new Insets(10, 20, 20, 20));
        mainPane.setMinHeight(150);
        
        // AND PUT IT IN THE WINDOW
        mainScene = new Scene(mainPane);
        this.setScene(mainScene);
        
        

    }

}
