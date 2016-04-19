/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINER;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

/**
 *
 * @author varungoel
 */
public class ClassDiagramObject extends Pane {

    //class or interface
    String diagramType;

    //this will hold the three panes and serve as the skeleton for the diagram
    VBox rootContainer;

    VBox packageContainer;

    //the container with the class name
    VBox nameContainer;
    //the container with the variables name
    VBox variablesContainer;
    //the container with the methods name
    VBox methodsContainer;

    Text packageNameText;

    //the class name text
    Text classNameText;
    //the variables name text
    Text variablesNameText;
    //the methods text
    Text methodsNameText;

    Line leftLine;
    Line rightLine;
    Line topLine;
    Line bottomLine;

    public ClassDiagramObject(double x, double y, String type) {
        rootContainer = new VBox();
        
        diagramType = type;

        //set the desired x and y coordinates
        rootContainer.setLayoutX(x);
        rootContainer.setLayoutY(y);

        packageNameText = new Text("");
        packageContainer = new VBox(packageNameText);

        //The first container which has the class name
        classNameText = new Text("Name");
        nameContainer = new VBox(classNameText);

        //The second container which has all the variables and stuff
        variablesNameText = new Text("Variables");
        variablesContainer = new VBox(variablesNameText);

        //The third container which has all the methods and stuff
        methodsNameText = new Text("Methods");
        methodsContainer = new VBox(methodsNameText);

        //putting it all in
        rootContainer.getChildren().add(packageContainer);
        rootContainer.getChildren().add(nameContainer);
        rootContainer.getChildren().add(variablesContainer);
        rootContainer.getChildren().add(methodsContainer);

        leftLine = new Line();
        rightLine = new Line();
        topLine = new Line();
        bottomLine = new Line();

        //root.getChildren().add(rootContainer);
        setStandardDimensions();

        initStyle();
    }

    public void putOnCanvas(Pane root) {
        root.getChildren().add(rootContainer);
        root.getChildren().add(leftLine);
        root.getChildren().add(rightLine);
    }

    public double getEndPoint() {
        return rootContainer.getLayoutX() + rootContainer.getWidth();
    }

    //sets the standard dimensions for the containers inside the boxes
    private void setStandardDimensions() {
        rootContainer.setMinHeight(250);
        rootContainer.setMinWidth(175);
        rootContainer.setMaxWidth(450);

        //setting up the left line
        leftLine.startXProperty().bind(rootContainer.layoutXProperty());
        leftLine.startYProperty().bind(rootContainer.layoutYProperty());

        leftLine.endXProperty().bind(leftLine.startXProperty());
        leftLine.endYProperty().bind(leftLine.startYProperty().add(rootContainer.heightProperty()));

        leftLine.setStroke(Color.YELLOW);
        leftLine.setStrokeWidth(5);

        
        leftLine.setOnMouseDragged(mouseDraggedEvent -> {
            if(this.getEndPoint() - mouseDraggedEvent.getX() >= 185 && this.getEndPoint() - mouseDraggedEvent.getX() <=450){
            rootContainer.setPrefWidth((this.getEndPoint() - mouseDraggedEvent.getX()));
            rootContainer.setLayoutX(mouseDraggedEvent.getX());
            }
        });

        //setting up the right line
        rightLine.startXProperty().bind(rootContainer.layoutXProperty().add(rootContainer.widthProperty()));
        rightLine.startYProperty().bind(rootContainer.layoutYProperty());

        rightLine.endXProperty().bind(rightLine.startXProperty());
        rightLine.endYProperty().bind(rightLine.startYProperty().add(rootContainer.heightProperty()));

        rightLine.setStroke(Color.RED);
        rightLine.setStrokeWidth(5);

        
        rightLine.setOnMouseDragged(mouseDraggedEvent -> {
            if(mouseDraggedEvent.getX() - rootContainer.getLayoutX() >= 185 && mouseDraggedEvent.getX() - rootContainer.getLayoutX() <=450){
            rootContainer.setPrefWidth(mouseDraggedEvent.getX() - rootContainer.getLayoutX());
            }
        });
     

        packageContainer.setMinHeight(20);
        packageContainer.setMinWidth(100);
        packageContainer.setMaxWidth(100);

        nameContainer.setMinHeight(50);
        //binding will allow easier resizing
        nameContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        nameContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        nameContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        variablesContainer.setMinHeight(100);
        variablesContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        variablesContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        variablesContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        methodsContainer.setMinHeight(100);
        methodsContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        methodsContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        methodsContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        //set the wrapping widths
        packageNameText.setWrappingWidth(95);
        classNameText.setWrappingWidth(rootContainer.getMinWidth());
        methodsNameText.setWrappingWidth(rootContainer.getMinWidth());
        variablesNameText.setWrappingWidth(rootContainer.getMinWidth());
    }

    public VBox getRootContainer() {
        return this.rootContainer;
    }

    public VBox getPackageContainer() {
        return this.packageContainer;
    }

    public VBox getMethodsContainer() {
        return this.methodsContainer;
    }

    public VBox getVariablesContainer() {
        return this.variablesContainer;
    }

    public Text getClassNameText() {
        return this.classNameText;
    }

    public void setClassNameText(String packageName) {
        this.classNameText.setText(packageName);
    }

    public Text getPackageNameText() {
        return this.packageNameText;
    }

    public void setPackageNameText(String packageName) {
        this.packageNameText.setText(packageName);
    }

    public void setDiagramType(String type) {
        diagramType = type;
    }

    //set the style for the diagram
    private void initStyle() {
        packageContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        nameContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        variablesContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        methodsContainer.getStyleClass().add(DIAGRAM_CONTAINERS);

        packageNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        classNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        methodsNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        variablesNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);

        rootContainer.getStyleClass().add(DIAGRAM_CONTAINER);

    }
    
    public String getDiagramType(){
        return diagramType;
    }

    public String toString() {
        return diagramType + ": " + this.classNameText.getText();
    }

}
