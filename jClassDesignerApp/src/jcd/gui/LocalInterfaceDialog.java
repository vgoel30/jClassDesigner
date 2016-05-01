/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jcd.data.ClassDiagramObject;
import org.controlsfx.control.CheckComboBox;

/**
 * Custom Dialog box to allow users to add new external packages
 * @author varungoel
 */
public class LocalInterfaceDialog extends Stage {

    // HERE'S THE SINGLETON
    static LocalInterfaceDialog singleton;

    // GUI CONTROLS FOR OUR DIALOG
    VBox mainPane;
    Scene mainScene;
    
    HBox interfaceNameBox;
    CheckComboBox<String> localInterfaces;
   
    Label interfaceNamesLabel;
    Button doneButton;

   
    // CONSTANT CHOICES
    public static final String DONE = "Done";

    /**
     * Note that the constructor is private since it follows the singleton
     * design pattern.
     *
     */
    public LocalInterfaceDialog() {
    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static LocalInterfaceDialog getSingleton() {
        if (singleton == null) {
            singleton = new LocalInterfaceDialog();
        }
        return singleton;
    }

    /**
     * This method initializes the singleton for use.
     *
     * @param primaryStage The window above which this dialog will be centered.
     * @param diagram
     */
    public void init(Stage primaryStage, ClassDiagramObject diagram, ArrayList<ClassDiagramObject> classesOnCanvas) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        mainPane = new VBox(10);
        
        interfaceNameBox = new HBox(10);
        mainPane.getChildren().add(interfaceNameBox);
        
        interfaceNamesLabel = new Label("Interfaces");
        
        ObservableList<String> potentialInterfacesToAdd = FXCollections.observableArrayList();
        
        for(ClassDiagramObject diagramOnCanvas: classesOnCanvas){
            if(diagramOnCanvas.getDiagramType().equals("interface") && !diagramOnCanvas.equals(diagram)){
                potentialInterfacesToAdd.add(diagramOnCanvas.getClassNameText().getText());
            }
        }
        
        localInterfaces = new CheckComboBox<>();
        localInterfaces.getItems().addAll(potentialInterfacesToAdd);
        
        interfaceNameBox.getChildren().add(interfaceNamesLabel);
        interfaceNameBox.getChildren().add(localInterfaces);

        // YES, NO, AND CANCEL BUTTONS
        doneButton = new Button(DONE);

        // NOW ORGANIZE OUR BUTTONS
       mainPane.getChildren().add(doneButton);

       

        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        //doneButton.setOnAction(doneHandler);


        // MAKE IT LOOK NICE
        mainPane.setPadding(new Insets(10, 20, 20, 20));
        mainPane.setSpacing(10);

        // AND PUT IT IN THE WINDOW
        mainScene = new Scene(mainPane);
        this.setScene(mainScene);
    }

    
}
