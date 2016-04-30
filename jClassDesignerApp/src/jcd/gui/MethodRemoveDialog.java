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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.controller.DiagramController;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.MethodObject;
import jcd.data.MethodObject;

/**
 * Custom dialog box to remove a variable
 *
 * @author varungoel
 */
public class MethodRemoveDialog extends Stage {

    // HERE'S THE SINGLETON
    static MethodRemoveDialog singleton;

    Scene mainScene;

    VBox mainPane;

    HBox variableSelectionContainer;
    Label variableLabel;
    ChoiceBox<MethodObject> methodChoiceBox;

    Button removeVariableButton;

    public MethodRemoveDialog() {

    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static MethodRemoveDialog getSingleton() {
        if (singleton == null) {
            singleton = new MethodRemoveDialog();
        }
        return singleton;
    }

    public void init(Stage primaryStage, ClassDiagramObject diagram, TableView variablesTable) {
        DiagramController diagramController = new DiagramController();

        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        mainPane = new VBox(35);

        variableSelectionContainer = new HBox(15);
        mainPane.getChildren().add(variableSelectionContainer);

        variableLabel = new Label("Methods: ");
        variableSelectionContainer.getChildren().add(variableLabel);

        //create an observable list of all the variables
        ObservableList<MethodObject> methods = FXCollections.observableArrayList();

        for (MethodObject method : diagram.getMethods()) {
            String name = method.getName();
            String type = method.getReturnType();
            boolean isStatic = method.getIsStatic();
            boolean isAbstract = method.getIsAbstract();
            String access = method.getAccess();
            ArrayList<ArgumentObject> arguments = method.getArguments();

            //me.add(new MethodObject(name, type, isStatic, isFinal, access));
            methods.add(new MethodObject(name, isStatic, isAbstract, arguments, type, access));
        }

        methodChoiceBox = new ChoiceBox<>(methods);
        methodChoiceBox.getSelectionModel().selectFirst();

        variableSelectionContainer.getChildren().add(methodChoiceBox);

        removeVariableButton = new Button("Remove");
        mainPane.getChildren().add(removeVariableButton);

        //when the remove button is clicked, manage the removal
        EventHandler removeHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            //get the variable object to remove
            MethodObject toRemove = methodChoiceBox.getValue();

            MethodRemoveDialog.this.hide();

            //removes the variable from the list of variables and shows the change  on the diagram
            diagramController.removeMethod(diagram, toRemove);

            //update the list of variables
            diagramController.updateVariablesTable(diagram, variablesTable);
        };

        removeVariableButton.setOnAction(removeHandler);

        // MAKE IT LOOK NICE
        mainPane.setPadding(new Insets(10, 20, 20, 20));
        mainPane.setMinHeight(150);

        // AND PUT IT IN THE WINDOW
        mainScene = new Scene(mainPane);
        this.setScene(mainScene);
    }
}
