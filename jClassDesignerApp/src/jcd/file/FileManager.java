/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.VBox;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import maf.components.AppDataComponent;
import maf.components.AppFileComponent;

/**
 *
 * @author varungoel
 */
public class FileManager implements AppFileComponent {

    //FOR JSON LOADING
    static final String JSON_DIAGRAMS_LIST = "diagrams_list";

    //Class or Interface
    static final String DIAGRAM_TYPE = "diagram_type";
    static final String CLASS = "class";
    static final String INTERFACE = "interface";
    //The dimensions of the diagram
    static final String JSON_DIAGRAM_DIMENSIONS = "dimensions";

    //The methods of the diagram
    static final String JSON_METHODS = "methods";
    static final String METHOD_NAME = "method_name";
    static final String METHOD_IS_STATIC = "method_is_static";
    static final String METHOD_IS_ABSTRACT = "method_is_abstract";
    static final String METHOD_ARGUMENTS = "method_arguments";
    static final String METHOD_RETURN_TYPE = "method_return_type";
    static final String METHOD_ACCESS = "method_access";
    
    static final String ARGUMENT_NAME = "argument_name";
    static final String ARGUMENT_TYPE = "argument_type";

    //The variables of the diagram
    static final String JSON_VARIABLES = "variables";
    static final String VARIABLE_NAME = "variable_name";
    static final String VARIABLE_TYPE = "variable_type";
    static final String VARIABLE_IS_STATIC = "variable_is_static";
    static final String VARIABLE_ACCESS = "variable_access";
    static final String VARIABLE_VALUE = "variable_value";
    
    static final String PACKAGE_NAME = "package_name";

    static final String DIAGRAM_NAME = "diagram_name";

    //the coordinates of the diagram
    static final String DIAGRAM_X = "x";
    static final String DIAGRAM_Y = "y";

    static final String ROOT_CONTAINER_HEIGHT = "root_container_height";
    static final String ROOT_CONTAINER_WIDTH = "root_container_width";

    static final String PACKAGE_CONTAINER_HEIGHT = "package_container_height";
    static final String PACKAGE_CONTAINER_WIDTH = "package_container_width";

    static final String METHODS_CONTAINER_HEIGHT = "methods_container_height";
    static final String METHODS_CONTAINER_WIDTH = "methods_container_width";

    static final String VARIABLES_CONTAINER_HEIGHT = "variables_container_height";
    static final String VARIABLES_CONTAINER_WIDTH = "variables_container_width";

    static final String CANVAS_WIDTH = "canvas_width";
    static final String CANVAS_HEIGHT = "canvas_height";

    public void testSaveData(ArrayList<ClassDiagramObject> classDiagramObjects, String filePath) throws FileNotFoundException {
        StringWriter sw = new StringWriter();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        fillArrayWithDiagrams(classDiagramObjects, arrayBuilder);

        JsonArray diagramsArray = arrayBuilder.build();

        int canvasWidth = 500;
        int canvasHeight = 500;

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_DIAGRAMS_LIST, diagramsArray)
                .add(CANVAS_WIDTH, canvasWidth)
                .add(CANVAS_HEIGHT, canvasHeight)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        StringWriter sw = new StringWriter();

        DataManager dataManager = (DataManager) data;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        fillArrayWithDiagrams(dataManager.classesOnCanvas, arrayBuilder);
        JsonArray diagramsArray = arrayBuilder.build();

        int canvasWidth = (int) dataManager.getRenderingPane().getWidth();
        int canvasHeight = (int) dataManager.getRenderingPane().getHeight();

        //System.out.println(diagramsArray);
        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_DIAGRAMS_LIST, diagramsArray)
                .add(CANVAS_WIDTH, canvasWidth)
                .add(CANVAS_HEIGHT, canvasHeight)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    /**
     * Helper method that will fill the JSON array with the diagrams present on
     * the canvas for saving them
     *
     * @param classesOnCanvas
     * @param arrayBuilder
     */
    private void fillArrayWithDiagrams(ArrayList<ClassDiagramObject> classesOnCanvas, JsonArrayBuilder arrayBuilder) {
        System.out.println("Total classes on canvas: " + classesOnCanvas.size());
        int totalClasses = classesOnCanvas.size();

        for (int i = 0; i < totalClasses; i++) {
            ClassDiagramObject diagram = classesOnCanvas.get(i);
            JsonObject diagramObject = makeClassDiagramJsonObject(diagram);
            arrayBuilder.add(diagramObject);
        }
    }

    /**
     * Makes a class diagram object in a JSON representation for saving
     *
     * @param diagram
     * @return
     */
    private JsonObject makeClassDiagramJsonObject(ClassDiagramObject diagram) {
        String type;

        if (diagram.getDiagramType().equals(CLASS)) {
            type = "class";
        } else {
            type = "interface";
        }

        JsonObject jso = Json.createObjectBuilder().add(DIAGRAM_TYPE, type)
                .add(DIAGRAM_NAME, diagram.getClassNameText().getText())
                .add(PACKAGE_NAME, diagram.getPackageNameText().getText())
                .add(JSON_DIAGRAM_DIMENSIONS, makeDimensionsJsonArray(diagram))
                .add(JSON_VARIABLES, makeVariablesJsonArray(diagram))
                .add(JSON_METHODS, makeMethodsJsonArray(diagram))
                .build();

        return jso;
    }

    /**
     * Builds and returns the dimension array for a diagram object
     *
     * @param diagram
     * @return
     */
    private JsonArray makeDimensionsJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        VBox rootContainer = diagram.getRootContainer();
        VBox packageContainer = diagram.getPackageContainer();
        VBox methodsContainer = diagram.getMethodsContainer();
        VBox variablesContainer = diagram.getVariablesContainer();

        JsonObject jso = Json.createObjectBuilder()
                .add(DIAGRAM_X, rootContainer.getLayoutX())
                .add(DIAGRAM_Y, rootContainer.getLayoutY())
                .add(ROOT_CONTAINER_HEIGHT, rootContainer.getHeight())
                .add(ROOT_CONTAINER_WIDTH, rootContainer.getWidth())
                .add(PACKAGE_CONTAINER_HEIGHT, packageContainer.getHeight())
                .add(PACKAGE_CONTAINER_WIDTH, packageContainer.getWidth())
                .add(METHODS_CONTAINER_HEIGHT, methodsContainer.getHeight())
                .add(METHODS_CONTAINER_WIDTH, methodsContainer.getWidth())
                .add(VARIABLES_CONTAINER_HEIGHT, variablesContainer.getHeight())
                .add(VARIABLES_CONTAINER_WIDTH, variablesContainer.getWidth())
                .build();

        arrayBuilder.add(jso);
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * Builds the JSON array of all the variables of the class diagram
     * @param diagram
     * @return 
     */
    private JsonArray makeVariablesJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<VariableObject> variables = diagram.getVariables();

        for (VariableObject variable : variables) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(VARIABLE_NAME, variable.getName())
                    .add(VARIABLE_TYPE, variable.getType())
                    .add(VARIABLE_IS_STATIC,variable.getIsStatic())
                    .add(VARIABLE_ACCESS,variable.getAccess())
                    .add(VARIABLE_VALUE,variable.getValue())
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        //System.out.println("VARIABLES ARRAY " + jA);
        return jA;
    }
    
    /**
     * Makes a Json array of all the methods in the class
     * @param diagram
     * @return 
     */
    private JsonArray makeMethodsJsonArray(ClassDiagramObject diagram){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<MethodObject> methods = diagram.getMethods();
        
        for (MethodObject method : methods) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(METHOD_NAME, method.getName())
                    .add(METHOD_IS_STATIC, method.getIsStatic())
                    .add(METHOD_IS_ABSTRACT, method.getIsAbstract())
                    .add(METHOD_ARGUMENTS,makeArgumentsArray(method))
                    .add(METHOD_RETURN_TYPE, method.getReturnType())
                    .add(METHOD_ACCESS, method.getAccess())
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        System.out.println("METHODS ARRAY " + jA);
        return jA;
    }
    
    /**
     * Makes a Json array of all the arguments in the method
     * @param method
     * @return 
     */
    private JsonArray makeArgumentsArray(MethodObject method){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ArrayList<ArgumentObject> arguments = method.getArguments();
        
        for(ArgumentObject argument: arguments){
            JsonObject jso = Json.createObjectBuilder()
                    .add(ARGUMENT_NAME,argument.getName())
                    .add(ARGUMENT_TYPE,argument.getType())
                    .build();
             arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        //System.out.println("ARGUMENTS ARRAY: " + jA);
        return jA;
    }
    
    public void testLoadData(String filePath) throws IOException {
        System.out.println("TEST LOAD DATA CALLED");
        JsonObject json = loadJSONFile(filePath);

        // AND NOW LOAD ALL THE SHAPES
        JsonArray jsonDiagramsArray = json.getJsonArray(JSON_DIAGRAMS_LIST);
        for (int i = 0; i < jsonDiagramsArray.size(); i++) {
            JsonObject jsonDiagram = jsonDiagramsArray.getJsonObject(i);
            ClassDiagramObject classDiagram = loadClassDiagram(jsonDiagram);
            System.out.println(classDiagram.toStringPlusPlus());
        }
        
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        dataManager.reset();
        System.out.println("LOAD DATA CALLED");
        JsonObject json = loadJSONFile(filePath);

        // AND NOW LOAD ALL THE SHAPES
        JsonArray jsonDiagramsArray = json.getJsonArray(JSON_DIAGRAMS_LIST);
        for (int i = 0; i < jsonDiagramsArray.size(); i++) {
            JsonObject jsonDiagram = jsonDiagramsArray.getJsonObject(i);
            ClassDiagramObject classDiagram = loadClassDiagram(jsonDiagram);
            dataManager.attachClassDiagramEventHandlers(classDiagram);
            dataManager.classesOnCanvas.add(classDiagram);
            classDiagram.putOnCanvas(dataManager.getRenderingPane());
        }
        int canvasWidth = json.getInt(CANVAS_WIDTH);
        int canvasHeight = json.getInt(CANVAS_HEIGHT);
        dataManager.getRenderingPane().minWidth(canvasWidth);
        dataManager.getRenderingPane().minHeight(canvasHeight);
    }

    public ClassDiagramObject loadClassDiagram(JsonObject jsonDiagram) {

        JsonArray dimensionsArray = jsonDiagram.getJsonArray(JSON_DIAGRAM_DIMENSIONS);
        JsonObject dimensionsJsonObject = dimensionsArray.getJsonObject(0);

        //get the x and y 
        int x = dimensionsJsonObject.getInt(DIAGRAM_X);
        int y = dimensionsJsonObject.getInt(DIAGRAM_Y);

        

        //the type of the diagram (interface/class)
        String type = jsonDiagram.getString(DIAGRAM_TYPE);

        ClassDiagramObject toAdd = new ClassDiagramObject(x, y, type);

        //setting the class and package names
        toAdd.setPackageNameText(jsonDiagram.getString(PACKAGE_NAME));
        toAdd.setClassNameText(jsonDiagram.getString(DIAGRAM_NAME));

        int rootContainerWidth = dimensionsJsonObject.getInt(ROOT_CONTAINER_WIDTH);
        int rootContainerHeight = dimensionsJsonObject.getInt(ROOT_CONTAINER_HEIGHT);

        toAdd.getRootContainer().setPrefSize(rootContainerWidth, rootContainerHeight);

        int packageContainerWidth = dimensionsJsonObject.getInt(PACKAGE_CONTAINER_WIDTH);
        int packageContainerHeight = dimensionsJsonObject.getInt(PACKAGE_CONTAINER_HEIGHT);
        toAdd.getPackageContainer().setPrefSize(packageContainerWidth, packageContainerHeight);

        int methodsContainerWidth = dimensionsJsonObject.getInt(METHODS_CONTAINER_WIDTH);
        int methodsContainerHeight = dimensionsJsonObject.getInt(METHODS_CONTAINER_HEIGHT);
        toAdd.getPackageContainer().setPrefSize(methodsContainerWidth, methodsContainerHeight);

        int variablesContainerWidth = dimensionsJsonObject.getInt(VARIABLES_CONTAINER_WIDTH);
        int variablesContainerHeight = dimensionsJsonObject.getInt(VARIABLES_CONTAINER_HEIGHT);
        toAdd.getPackageContainer().setPrefSize(variablesContainerWidth, variablesContainerHeight);
        
        //ALL THE VARIABLES OF THE METHOD
        JsonArray variablesArray = jsonDiagram.getJsonArray(JSON_VARIABLES);
        for(int i = 0; i < variablesArray.size(); i++){
            JsonObject current = variablesArray.getJsonObject(i);
            String name = current.getString(VARIABLE_NAME);
            String variableType = current.getString(VARIABLE_TYPE);
            boolean isStatic = current.getBoolean(VARIABLE_IS_STATIC);
            String access = current.getString(VARIABLE_ACCESS);
            String value = current.getString(VARIABLE_VALUE);
            
            VariableObject varToAdd = new VariableObject(name, variableType, isStatic, access, value);
            toAdd.getVariables().add(varToAdd);
        }
        
        JsonArray methodsArray = jsonDiagram.getJsonArray(JSON_METHODS);
        
       
        return toAdd;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        //hrow new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
