/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.Stack;
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
import jcd.actions.Action;
import jcd.actions.RemoveVariable;
import jcd.controller.DiagramController;
import jcd.data.ClassDiagramObject;
import jcd.data.VariableObject;

/**
 * Custom dialog box to remove a variable
 *
 * @author varungoel
 */
public class VariableRemoveDialog extends Stage {

    // HERE'S THE SINGLETON
    static VariableRemoveDialog singleton;

    Scene mainScene;

    VBox mainPane;

    HBox variableSelectionContainer;
    Label variableLabel;
    ChoiceBox<VariableObject> variableChoiceBox;

    Button removeVariableButton;

    public VariableRemoveDialog() {

    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static VariableRemoveDialog getSingleton() {
        if (singleton == null) {
            singleton = new VariableRemoveDialog();
        }
        return singleton;
    }

    public void init(Stage primaryStage, ClassDiagramObject diagram, TableView variablesTable, Stack<Action> undoStack) {
        DiagramController diagramController = new DiagramController();

        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        mainPane = new VBox(35);

        variableSelectionContainer = new HBox(15);
        mainPane.getChildren().add(variableSelectionContainer);

        variableLabel = new Label("Variables: ");
        variableSelectionContainer.getChildren().add(variableLabel);

        //create an observable list of all the variables
        ObservableList<VariableObject> variables = FXCollections.observableArrayList();

        for (VariableObject variable : diagram.getVariables()) {
            String name = variable.getName();
            String type = variable.getType();
            boolean isStatic = variable.getIsStatic();
            boolean isFinal = variable.getIsFinal();
            String access = variable.getAccess();

            variables.add(new VariableObject(name, type, isStatic, isFinal, access));
        }

        variableChoiceBox = new ChoiceBox<>(variables);
        variableChoiceBox.getSelectionModel().selectFirst();

        variableSelectionContainer.getChildren().add(variableChoiceBox);

        removeVariableButton = new Button("Remove");
        mainPane.getChildren().add(removeVariableButton);

        //when the remove button is clicked, manage the removal
        EventHandler removeHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            //get the variable object to remove
            VariableObject toRemove = variableChoiceBox.getValue();

          RemoveVariable removeVariableMove = new RemoveVariable(diagram);
          removeVariableMove.setRemovedVariable(toRemove);
          undoStack.push(removeVariableMove);

            //removes the variable from the list of variables and shows the change  on the diagram
            diagramController.removeVariable(diagram, toRemove);

            //update the list of variables
            diagramController.updateVariablesTable(diagram, variablesTable);
            
            VariableRemoveDialog.this.hide();
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
