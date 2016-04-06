/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author varungoel
 */
public class ClassDiagramObject extends Pane {

    //this will hold the three panes and serve as the skeleton for the diagram
    VBox rootContainer;
    //the container with the class name
    VBox nameContainer;
    //the container with the variables name
    VBox variablesContainer;
    //the container with the methods name
    VBox methodsContainer;

    //the class name text
    public Text classNameText;
    //the variables name text
    Text variablesNameText;
    //the methods text
    Text methodsNameText;

    public ClassDiagramObject(Pane root, double x, double y) {
        rootContainer = new VBox();

        //set the desired x and y coordinates
        rootContainer.setLayoutX(x);
        rootContainer.setLayoutY(y);

        //The first container which has the class name
        classNameText = new Text("Dummy Is To Dummy What dummy is to moron");
        nameContainer = new VBox(classNameText);
        nameContainer.setStyle("-fx-background-color:red");

        //The second container which has all the variables and stuff
        variablesNameText = new Text("Yummy Dummy Is To Dummy What dummy is to moron Dummy Is To Dummy What dummy is to moron");
        variablesContainer = new VBox(variablesNameText);
        variablesContainer.setStyle("-fx-background-color:purple");

        //The third container which has all the methods and stuff
        methodsNameText = new Text("Methods");
        methodsContainer = new VBox(methodsNameText);
        methodsContainer.setStyle("-fx-background-color:pink");

        //putting it all in
        rootContainer.getChildren().add(nameContainer);
        rootContainer.getChildren().add(variablesContainer);
        rootContainer.getChildren().add(methodsContainer);

        root.getChildren().add(rootContainer);
        setStandardDimensions();

    }

    //sets the standard dimensions for the containers inside the boxes
    private void setStandardDimensions() {
        rootContainer.setMinHeight(250);
        rootContainer.setMinWidth(175);
        rootContainer.setMaxWidth(400);

        nameContainer.setMinHeight(50);
//        nameContainer.setMinWidth(17);
//        nameContainer.setMaxWidth(17);
        //binding will allow easier resizing
        nameContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        nameContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        variablesContainer.setMinHeight(100);
//        variablesContainer.setMinWidth(175);
//        variablesContainer.setMaxWidth(200);
        variablesContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        variablesContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        methodsContainer.setMinHeight(100);
//        methodsContainer.setMinWidth(175);
//        methodsContainer.setMaxWidth(200);
        methodsContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        methodsContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        //set the wrapping widths
        classNameText.setWrappingWidth(rootContainer.getMinWidth());
        methodsNameText.setWrappingWidth(rootContainer.getMinWidth());
        variablesNameText.setWrappingWidth(rootContainer.getMinWidth());
    }

}
