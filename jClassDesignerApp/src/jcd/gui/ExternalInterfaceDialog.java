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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import jcd.connector_lines.InheritanceLine;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.ExternalParent;

/**
 * Custom Dialog box to allow users to add new external packages
 *
 * @author varungoel
 */
public class ExternalInterfaceDialog extends Stage {

    // HERE'S THE SINGLETON
    static ExternalInterfaceDialog singleton;

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
    public static final String ADD_PACKAGE = "Add Interface";
    //public static final String NO = "No";
    public static final String CANCEL = "Done";

    /**
     * Note that the constructor is private since it follows the singleton
     * design pattern.
     *
     */
    public ExternalInterfaceDialog() {
    }

    /**
     * The static accessor method for this singleton.
     *
     * @return The singleton object for this type.
     */
    public static ExternalInterfaceDialog getSingleton() {
        if (singleton == null) {
            singleton = new ExternalInterfaceDialog();
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
     * @return
     */
    public void init(Stage primaryStage, ClassDiagramObject diagram, DataManager dataManager, Pane canvas) {
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

        ArrayList<String> externalInterfacesToAdd = new ArrayList<>();

        // NOW ORGANIZE OUR BUTTONS
        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().add(addPackage);
        buttonBox.getChildren().add(doneButton);
        buttonBox.getChildren().add(0, textField);

        EventHandler addInterfaceHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            TextField newTextField = new TextField();
            textFields.add(newTextField);
            buttonBox.getChildren().add(0, newTextField);
        };

        //this will display all the old interfaces that have been included
        for (String interfaceInList : diagram.getExternalInterfaces()) {
            textField = new TextField(interfaceInList);
            textFields.add(textField);
            //user should not be allowed to edit the previous ones; only see them
            //textField.setDisable(true);
            buttonBox.getChildren().add(0, textField);

        }

        //when the done button is clicked
        EventHandler doneHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            //clear the old interfaces to add new ones
            diagram.getExternalInterfaces().clear();
            for (TextField textField : textFields) {
                String interfaceName = textField.getText();
                //add the API 
                if (!interfaceName.equals("")) {
                    diagram.addExternalInterface(interfaceName);
                    externalInterfacesToAdd.add(interfaceName);
                }
            }

            //renders the interface box
            for (String externalInterfaceToAdd : externalInterfacesToAdd) {
                //if the external parent doesn't exist yet
                if (!dataManager.externalParents.contains(externalInterfaceToAdd)) {
                    //create the external parent object
                    ExternalParent parentToAdd = new ExternalParent(externalInterfaceToAdd);
                    //render it on the canvas
                    parentToAdd.putOnCanvas(canvas);
                    //make the line object
                    InheritanceLine inheritanceLine = new InheritanceLine(parentToAdd, diagram, canvas);
                    //add the selected diagram to the list of children of the parent box
                    parentToAdd.children.add(diagram);
                    //add the parental line
                    parentToAdd.parentalLines.add(inheritanceLine);
                    diagram.inheritanceLinesOut.add(inheritanceLine);
                    dataManager.attachExternalDiagramHandlers(parentToAdd);
                    dataManager.externalParents.add(externalInterfaceToAdd);
                    dataManager.externalParentsOnCanvas.add(parentToAdd);
                } else {
                    for (ExternalParent externalParent : dataManager.externalParentsOnCanvas) {
                        if (externalParent.getName().equals(externalInterfaceToAdd)) {
                            InheritanceLine inheritanceLine = new InheritanceLine(externalParent, diagram, canvas);
                            diagram.linesPointingTowards.add(inheritanceLine);
                            externalParent.children.add(diagram);
                            externalParent.parentalLines.add(inheritanceLine);
                            diagram.inheritanceLinesOut.add(inheritanceLine);
                            break;
                        }
                    }
                }
            }

          

            //this will get rid of any old lines that needn't be there
            ArrayList<InheritanceLine> linesToRemove = new ArrayList<>();

            for (InheritanceLine inheritanceLineOut : diagram.inheritanceLinesOut) {
                System.out.println("LITBC");
                if (inheritanceLineOut.getEndDiagram() instanceof ExternalParent) {
                    ExternalParent endDiagram = (ExternalParent) inheritanceLineOut.getEndDiagram();
                    if (!diagram.getExternalInterfaces().contains(endDiagram.getName())) {
                        //diagram.inheritanceLinesOut.remove(inheritanceLineOut);
                        inheritanceLineOut.removeFromCanvas(canvas);
                        endDiagram.children.remove(diagram);
                        endDiagram.parentalLines.remove(inheritanceLineOut);
                        linesToRemove.add(inheritanceLineOut);
                        if (endDiagram.children.isEmpty()) {
                            canvas.getChildren().remove(endDiagram.getRootContainer());
                            dataManager.externalParentsOnCanvas.remove(endDiagram);
                            dataManager.externalParents.remove(endDiagram.toString());
                        }
                    }
                }
            }
            //remove all the unnecessary lines
            diagram.inheritanceLinesOut.removeAll(linesToRemove);
            ExternalInterfaceDialog.this.hide();
        };

        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addPackage.setOnAction(addInterfaceHandler);
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
