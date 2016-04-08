/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINER;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

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
        classNameText = new Text("Class Name");
        nameContainer = new VBox(classNameText);

        //The second container which has all the variables and stuff
        variablesNameText = new Text("Variables");
        variablesContainer = new VBox(variablesNameText);
        //variablesContainer.setStyle("-fx-background-color:purple");

        //The third container which has all the methods and stuff
        methodsNameText = new Text("Methods");
        methodsContainer = new VBox(methodsNameText);
        //methodsContainer.setStyle("-fx-background-color:pink");

        //putting it all in
        rootContainer.getChildren().add(nameContainer);
        rootContainer.getChildren().add(variablesContainer);
        rootContainer.getChildren().add(methodsContainer);

        root.getChildren().add(rootContainer);
        setStandardDimensions();

        initStyle();
    }

    //sets the standard dimensions for the containers inside the boxes
    private void setStandardDimensions() {
        rootContainer.setMinHeight(250);
        rootContainer.setMinWidth(175);
        rootContainer.setMaxWidth(400);

        nameContainer.setMinHeight(50);
        //binding will allow easier resizing
        nameContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        nameContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        variablesContainer.setMinHeight(100);
        variablesContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        variablesContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        methodsContainer.setMinHeight(100);
        methodsContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        methodsContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());

        //set the wrapping widths
        classNameText.setWrappingWidth(rootContainer.getMinWidth());
        methodsNameText.setWrappingWidth(rootContainer.getMinWidth());
        variablesNameText.setWrappingWidth(rootContainer.getMinWidth());
    }
    
    public VBox getRootContainer(){
        return this.rootContainer;
    }
    
    public Text getClassNameText(){
        return this.classNameText;
    }

    //set the style for the diagram
    private void initStyle() {
        nameContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        variablesContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        methodsContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        
       classNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
       methodsNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
       variablesNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        
       rootContainer.getStyleClass().add(DIAGRAM_CONTAINER);
        
    }

}
