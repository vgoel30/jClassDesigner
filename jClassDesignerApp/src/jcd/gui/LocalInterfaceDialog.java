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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import jcd.connector_lines.InheritanceLine;
import jcd.controller.DiagramController;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import org.controlsfx.control.CheckComboBox;

/**
 * Custom Dialog box to allow users to add local interfaces
 *
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
     * @param dataManager
     * @param canvas
     */
    public void init(Stage primaryStage, ClassDiagramObject diagram, DataManager dataManager, Pane canvas) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        DiagramController diagramController = new DiagramController();

        mainPane = new VBox(20);

        interfaceNameBox = new HBox(10);
        mainPane.getChildren().add(interfaceNameBox);

        interfaceNamesLabel = new Label("Interfaces");

        ObservableList<String> potentialInterfacesToAdd = FXCollections.observableArrayList();

        //builds the check combo box with all the appropriate interfaces
        for (ClassDiagramObject diagramOnCanvas : dataManager.classesOnCanvas) {
            if (diagramOnCanvas.getDiagramType().equals("interface") && !diagramOnCanvas.equals(diagram)) {
                potentialInterfacesToAdd.add(diagramOnCanvas.getClassNameText().getText());
            }
        }

        localInterfaces = new CheckComboBox<>();
        localInterfaces.getItems().addAll(potentialInterfacesToAdd);

        for (String implementedInterface : diagram.getLocalInterfaces()) {
            localInterfaces.getCheckModel().check(implementedInterface);
        }

        interfaceNameBox.getChildren().add(interfaceNamesLabel);
        interfaceNameBox.getChildren().add(localInterfaces);

        //the event handler for done is clicked
        EventHandler doneHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            diagram.getLocalInterfaces().clear();

            ObservableList<String> interfacesToAdd = localInterfaces.getCheckModel().getCheckedItems();

            for (String interfaceToAdd : interfacesToAdd) {
                //add to the list of local interfaces
                diagram.addLocalInterface(interfaceToAdd);
                //manage the local interface line stuff
                diagramController.manageLocalInterfaceAddition(diagram, interfaceToAdd, dataManager, dataManager.getRenderingPane());

            }
            //this will get rid of any old lines that needn't be there
            ArrayList<InheritanceLine> linesToRemove = new ArrayList<>();
            for (InheritanceLine inheritanceLineOut : diagram.inheritanceLinesOut) {
                if (inheritanceLineOut.getEndDiagram() instanceof ClassDiagramObject) {
                    ClassDiagramObject endDiagram = (ClassDiagramObject) inheritanceLineOut.getEndDiagram();
                    if (!diagram.getLocalInterfaces().contains(endDiagram.getDiagramName())) {

                        if (inheritanceLineOut.standardChildLine != null) {
                            inheritanceLineOut.standardChildLine.removeFromCanvas(dataManager.getRenderingPane());
                        }

                        if (inheritanceLineOut.inheritanceChildLine != null) {
                            inheritanceLineOut.inheritanceChildLine.removeFromCanvas(dataManager.getRenderingPane());
                        }

                        inheritanceLineOut.removeFromCanvas(canvas);
                        endDiagram.getChildren().remove(diagram);
                        endDiagram.linesPointingTowards.remove(inheritanceLineOut);
                        linesToRemove.add(inheritanceLineOut);
                    }
                }
            }
            diagram.inheritanceLinesOut.removeAll(linesToRemove);

            LocalInterfaceDialog.this.hide();
        };

        // YES, NO, AND CANCEL BUTTONS
        doneButton = new Button(DONE);
        doneButton.setOnAction(doneHandler);

        // NOW ORGANIZE OUR BUTTONS
        mainPane.getChildren().add(doneButton);

        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        //doneButton.setOnAction(doneHandler);
        // MAKE IT LOOK NICE
        mainPane.setPadding(new Insets(10, 20, 20, 20));
        mainPane.setSpacing(10);
        mainPane.setMinHeight(75);

        // AND PUT IT IN THE WINDOW
        mainScene = new Scene(mainPane);
        this.setScene(mainScene);
    }

}
