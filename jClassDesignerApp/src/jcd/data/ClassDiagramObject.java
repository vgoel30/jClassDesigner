/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import jcd.connector_lines.AggregateLine;
import jcd.connector_lines.DependencyLine;
import jcd.connector_lines.InheritanceLine;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINER;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

/**
 *
 * @author varungoel
 */
public class ClassDiagramObject extends Diagram implements Comparable<ClassDiagramObject> {

    public static int counter = 0;

    String name;

    //class or interface
    String diagramType;

    String parent = "";

    boolean isAbstract;

    ArrayList<String> localInterfaces = new ArrayList<>();
    ArrayList<String> externalInterfaces = new ArrayList<>();

    ArrayList<ClassDiagramObject> children = new ArrayList<>();
    //lines that are pointing towards this diagram (this diagram will be a parent)
    public ArrayList<InheritanceLine> linesPointingTowards = new ArrayList<>();
    //all the inheritance lines pointing out of this.
    public ArrayList<InheritanceLine> inheritanceLinesOut = new ArrayList<>();

    //list of all the data types this class has variables of
    public ArrayList<String> externalDataTypesUsed = new ArrayList<>();
    //list of all the aggregate lines originating out of this diagram
    public ArrayList<AggregateLine> aggregateLinesOut = new ArrayList<>();

    //list of all the data types this class 'uses'
    public ArrayList<String> externalUseTypesUsed = new ArrayList<>();
    //list of all the dependency lines originating out of this diagram
    public ArrayList<DependencyLine> dependencyLinesOut = new ArrayList<>();

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

    //the diagram type text
    Text classTypeText;

    //the variables name text
    Text variablesNameText;
    //the methods text
    Text methodsNameText;

    //all the methods
    ArrayList<MethodObject> methods = new ArrayList<>();
    //all the variables
    ArrayList<VariableObject> variables = new ArrayList<>();

    ArrayList<String> javaAPI_Packages = new ArrayList<>();

    public ArrayList<String> getLocalInterfaces() {
        return localInterfaces;
    }

    public ArrayList<String> getExternalInterfaces() {
        return externalInterfaces;
    }

    public ArrayList<ClassDiagramObject> getChildren() {
        return children;
    }

    public ArrayList<MethodObject> getMethods() {
        return methods;
    }

    public void setParentName(String name) {
        parent = name;
    }

    public String getParentName() {
        return parent;
    }

    public ArrayList<String> getJavaAPI_Packages() {
        return javaAPI_Packages;
    }

    public ArrayList<VariableObject> getVariables() {
        return variables;
    }

    public boolean isInterface() {
        return diagramType.equalsIgnoreCase("interface");
    }

    //for drag to resize
    Line rightLine;
    Line leftLine;
    Line bottomLine;
    Line middleLine;

    //helper constructor for testing load and save
    public ClassDiagramObject(String name, String type, ArrayList<MethodObject> methods, ArrayList<VariableObject> variables) {
        rootContainer = new VBox();

        diagramType = type;
        isAbstract = false;

        packageNameText = new Text("");
        packageContainer = new VBox(packageNameText);

        //The first container which has the class name
        classNameText = new Text(name);
        nameContainer = new VBox(classNameText);

        //The second container which has all the variables and stuff
        variablesNameText = new Text("Variables");
        variablesContainer = new VBox();

        //The third container which has all the methods and stuff
        methodsNameText = new Text("Methods");
        methodsContainer = new VBox();

        //putting it all in
        rootContainer.getChildren().add(packageContainer);
        rootContainer.getChildren().add(nameContainer);
        rootContainer.getChildren().add(variablesContainer);
        rootContainer.getChildren().add(methodsContainer);

        rightLine = new Line();

        //root.getChildren().add(rootContainer);
        setStandardDimensions();

        initStyle();

        this.methods = methods;
        this.variables = variables;
    }

    public ClassDiagramObject(double x, double y, String type) {
        counter++;
        rootContainer = new VBox();
        variablesContainer = new VBox();
        methodsContainer = new VBox();

        diagramType = type;

        parent = "";

        //set the desired x and y coordinates
        rootContainer.setLayoutX(x);
        rootContainer.setLayoutY(y);

        packageNameText = new Text("Package");
        packageContainer = new VBox(packageNameText);

        //The first container which has the class name
        classTypeText = new Text(type.toUpperCase());
        classNameText = new Text(diagramType + counter);
        nameContainer = new VBox(classTypeText, classNameText);

        //The second container which has all the variables and stuff
        variablesNameText = new Text("Variables");

        //The third container which has all the methods and stuff
        methodsNameText = new Text("Methods");

        //putting it all in
        rootContainer.getChildren().add(packageContainer);
        rootContainer.getChildren().add(nameContainer);
        rootContainer.getChildren().add(variablesContainer);
        rootContainer.getChildren().add(methodsContainer);

        middleLine = new Line();
        bottomLine = new Line();
        rightLine = new Line();
        leftLine = new Line();

        setStandardDimensions();

        initStyle();
    }

    /**
     * Puts the diagram on the canvas with the resizing lines
     *
     * @param canvas
     */
    public void putOnCanvas(Pane canvas) {
        canvas.getChildren().add(rootContainer);
        canvas.getChildren().add(rightLine);
        canvas.getChildren().add(leftLine);
    }

    public void removeFromCanvas(Pane canvas) {
        canvas.getChildren().remove(rootContainer);
        canvas.getChildren().remove(rightLine);
        canvas.getChildren().remove(leftLine);
    }

    public double getEndPoint() {
        return rootContainer.getLayoutX() + rootContainer.getWidth();
    }

    public void makeAbstract() {
        isAbstract = true;
        classTypeText.setText("ABSTRACT " + diagramType.toUpperCase());
    }

    /**
     * Add an import package
     *
     * @param API
     */
    public void addAPI(String API) {
        if (!javaAPI_Packages.contains(API)) {
            javaAPI_Packages.add(API);
        }
    }

    public void addLocalInterface(String interfaceName) {
        if (!localInterfaces.contains(interfaceName)) {
            localInterfaces.add(interfaceName);
        }
    }

    public void addExternalInterface(String interfaceName) {
        if (!externalInterfaces.contains(interfaceName)) {
            externalInterfaces.add(interfaceName);
        }
    }

    //sets the standard dimensions for the containers inside the boxes
    private void setStandardDimensions() {
        rootContainer.setMinHeight(80);
        rootContainer.setMinWidth(175);
        rootContainer.setMaxWidth(500);

        //setting up the left line
        leftLine.startXProperty().bind(rootContainer.layoutXProperty());
        leftLine.startYProperty().bind(rootContainer.layoutYProperty());

        leftLine.endXProperty().bind(leftLine.startXProperty());
        leftLine.endYProperty().bind(leftLine.startYProperty().add(rootContainer.heightProperty()));

        leftLine.setStroke(Color.WHITE);
        leftLine.setStrokeWidth(5);
        leftLine.setVisible(false);

        //setting up the right line
        rightLine.startXProperty().bind(rootContainer.layoutXProperty().add(rootContainer.widthProperty()));
        rightLine.startYProperty().bind(rootContainer.layoutYProperty());

        rightLine.endXProperty().bind(rightLine.startXProperty());
        rightLine.endYProperty().bind(rightLine.startYProperty().add(rootContainer.heightProperty()));

        rightLine.setStroke(Color.WHITE);
        rightLine.setStrokeWidth(5);
        rightLine.setVisible(false);

        packageContainer.setMinHeight(10);
        packageContainer.setMinWidth(100);
        packageContainer.setMaxWidth(100);

        nameContainer.setMinHeight(20);
        packageContainer.setMinHeight(10);
        packageContainer.setMinWidth(100);
        packageContainer.setMaxWidth(100);

        nameContainer.setMinHeight(20);
        //binding will allow easier resizing
        nameContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        nameContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        nameContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        variablesContainer.setMinHeight(20);
        variablesContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        variablesContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        variablesContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        methodsContainer.setMinHeight(20);
        methodsContainer.minWidthProperty().bind(rootContainer.minWidthProperty());
        methodsContainer.maxWidthProperty().bind(rootContainer.maxWidthProperty());
        methodsContainer.prefWidthProperty().bind(rootContainer.prefWidthProperty());

        //set the wrapping widths
        packageNameText.setWrappingWidth(95);
        classNameText.setWrappingWidth(rootContainer.getMinWidth());
        methodsNameText.setWrappingWidth(rootContainer.getMinWidth());
        variablesNameText.setWrappingWidth(rootContainer.getMinWidth());

    }

    @Override
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

    public Line getRightLine() {
        return this.rightLine;
    }

    public Line getMiddleLine() {
        return middleLine;
    }

    public Line getLeftLine() {
        return this.leftLine;
    }

    public Line getBottomLine() {
        return this.bottomLine;
    }

    public Text getClassNameText() {
        return this.classNameText;
    }

    @Override
    public String getName() {
        return this.classNameText.getText();
    }

    public String getDiagramName() {
        return this.classNameText.getText();
    }

    public void setClassNameText(String className) {
        this.classNameText.setText(className);
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

    public void setX(double X) {
        rootContainer.setLayoutX(X);
    }

    public void setY(double Y) {
        rootContainer.setLayoutY(Y);
    }

    public double getX() {
        return rootContainer.getLayoutX();
    }

    public double getY() {
        return rootContainer.getLayoutY();
    }

    public String getDiagramType() {
        return diagramType;
    }

    //set the style for the diagram
    private void initStyle() {
        packageContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        nameContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        variablesContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
        methodsContainer.getStyleClass().add(DIAGRAM_CONTAINERS);

        packageNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        classTypeText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        classNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        methodsNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        variablesNameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);

        rootContainer.getStyleClass().add(DIAGRAM_CONTAINER);
    }

    public String toStringPlusPlus() {
        return diagramType + ": " + this.classNameText.getText() + " Methods : " + this.methods + " Variables : " + this.variables;
    }

    @Override
    public String toString() {
        return this.classNameText.getText() + ":" + this.packageNameText.getText();
    }

    public String toStringCode() {
        String toReturn = "";

        //Add the import statements at the header
        for (String javaAPI_Name : javaAPI_Packages) {
            if (!javaAPI_Name.equals("")) {
                toReturn += "import " + javaAPI_Name + ";\n";
            }
        }

        toReturn += "\npublic " + getDiagramType() + " " + this.getClassNameText().getText();// + "{\n\n ";

        //adding parent stuff
        if (getParentName() != null && !getParentName().equals("")) {
            String potentialName = getParentName();
            if (potentialName.contains(":")) {
                potentialName = potentialName.split(":")[0];
            }
            toReturn += " extends " + potentialName;
        }

        ArrayList<String> interfacesToAdd = new ArrayList<>();
        if (localInterfaces.size() > 0) {
            interfacesToAdd.addAll(localInterfaces);
        }
        if (externalInterfaces.size() > 0) {
            interfacesToAdd.addAll(externalInterfaces);
        }

//        System.out.println();
        System.out.println("getParentName " + interfacesToAdd.size());

        if (interfacesToAdd.size() == 1) {
            if (getParentName() != null && !getParentName().equals("")) {
                toReturn += ", implements " + interfacesToAdd.get(0) + " {\n\n ";
            } else {
                toReturn += " implements " + interfacesToAdd.get(0) + " {\n\n ";
            }
        } else if (interfacesToAdd.size() > 1) {

            if (getParentName() != null && !getParentName().equals("")) {
                toReturn += ", implements ";
            } else {
                toReturn += " implements ";
            }
            for (int i = 0; i < interfacesToAdd.size() - 1; i++) {
                toReturn += interfacesToAdd.get(i) + ", ";
            }
            toReturn += interfacesToAdd.get(interfacesToAdd.size() - 1) + " {\n\n ";
        } else {
            toReturn += " {\n\n ";
        }

        for (VariableObject variable : variables) {
            toReturn += "\t" + variable.toStringCode() + "\n";
        }
        toReturn += "\n";
        for (MethodObject method : methods) {
            toReturn += "\t" + method.toStringCode() + "\n";
        }

        toReturn += "\n}";

        return toReturn;
    }

    @Override
    public int compareTo(ClassDiagramObject o
    ) {
        int x = this.getPackageNameText().getText().compareTo(o.getPackageNameText().getText());
        if (x != 0) {
            return x;
        }
        return this.getClassNameText().getText().compareTo(o.getClassNameText().getText());
    }

    public boolean equals(ClassDiagramObject o) {
        return this.compareTo(o) == 0;
    }

}
