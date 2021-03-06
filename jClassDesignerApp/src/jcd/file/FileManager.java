/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileInputStream;
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
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.controller.DiagramController;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.DataManager;
import jcd.data.ExternalDataType;
import jcd.data.ExternalParent;
import jcd.data.ExternalUseType;
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
    static final String JSON_EXTERNAL_DATA_TYPES_LIST = "external_data_type_list";
    static final String JSON_EXTERNAL_USE_TYPES_LIST = "external_use_type_list";
    static final String JSON_EXTERNAL_PARENT_TYPES_LIST = "external_parent_type_list";

    static final String JSON_DIAGRAMS_LIST = "diagrams_list";

    //Class or Interface
    static final String DIAGRAM_TYPE = "diagram_type";
    static final String CLASS = "class";
    static final String INTERFACE = "interface";

    static final String PARENT = "parent";

    static final String JSON_EXTERNAL_INTERFACES_IMPLEMENTED = "external_interfaces_implemented";
    static final String JSON_LOCAL_INTERFACES_IMPLEMENTED = "local_interfaces_implemented";
    static final String JSON_IMPORTED_PACKAGES = "imported_packages";

    static final String EXTERNAL_INTERFACE_NAME = "external_interface_name";
    static final String LOCAL_INTERFACE_NAME = "local_interface_name";
    static final String IMPORTED_PACKAGE = "imported_package";
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
    static final String VARIABLE_IS_FINAL = "variable_is_final";
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

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        StringWriter sw = new StringWriter();

        DataManager dataManager = (DataManager) data;

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        fillArrayWithDiagrams(dataManager.classesOnCanvas, arrayBuilder);
        JsonArray diagramsArray = arrayBuilder.build();

        //for all the external data types
        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
        fillArrayWithExternalDataTypes(dataManager.externalDataTypesOnCanvas, arrayBuilder2);
        JsonArray externalDataTypesList = arrayBuilder2.build();

        //for all the external parents on canvas
        JsonArrayBuilder arrayBuilder3 = Json.createArrayBuilder();
        fillArrayWithExternalParentTypes(dataManager.externalParentsOnCanvas, arrayBuilder3);
        JsonArray externalParentTypesList = arrayBuilder3.build();

        //for all the external use types on canvas
        JsonArrayBuilder arrayBuilder4 = Json.createArrayBuilder();
        fillArrayWithExternalUseTypes(dataManager.externalUseTypesOnCanvas, arrayBuilder4);
        JsonArray externalUseTypesList = arrayBuilder4.build();

        int canvasWidth = (int) dataManager.getRenderingPane().getWidth();
        int canvasHeight = (int) dataManager.getRenderingPane().getHeight();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_EXTERNAL_DATA_TYPES_LIST, externalDataTypesList)
                .add(JSON_EXTERNAL_PARENT_TYPES_LIST, externalParentTypesList)
                .add(JSON_EXTERNAL_USE_TYPES_LIST, externalUseTypesList)
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
        int totalClasses = classesOnCanvas.size();
        for (int i = 0; i < totalClasses; i++) {
            ClassDiagramObject diagram = classesOnCanvas.get(i);
            JsonObject diagramObject = makeClassDiagramJsonObject(diagram);
            arrayBuilder.add(diagramObject);
        }
    }

    /**
     * Fills the array with external data types
     *
     * @param externalDataTypesOnCanvas
     * @param arrayBuilder
     */
    private void fillArrayWithExternalDataTypes(ArrayList<ExternalDataType> externalDataTypesOnCanvas, JsonArrayBuilder arrayBuilder) {
        int totalClasses = externalDataTypesOnCanvas.size();
        for (int i = 0; i < totalClasses; i++) {
            ExternalDataType externalDataType = externalDataTypesOnCanvas.get(i);
            JsonObject diagramObject = makeExternalDataTypeJsonObject(externalDataType);
            arrayBuilder.add(diagramObject);
        }
    }

    /**
     * Fills array with external parent types
     *
     * @param externalParentTypesOnCanvas
     * @param arrayBuilder
     */
    private void fillArrayWithExternalParentTypes(ArrayList<ExternalParent> externalParentTypesOnCanvas, JsonArrayBuilder arrayBuilder) {
        int totalClasses = externalParentTypesOnCanvas.size();
        for (int i = 0; i < totalClasses; i++) {
            ExternalParent externalParent = externalParentTypesOnCanvas.get(i);
            JsonObject diagramObject = makeExternalParentTypeJsonObject(externalParent);
            arrayBuilder.add(diagramObject);
        }
    }

    private void fillArrayWithExternalUseTypes(ArrayList<ExternalUseType> externalUseTypesOnCanvas, JsonArrayBuilder arrayBuilder) {
        int totalClasses = externalUseTypesOnCanvas.size();
        for (int i = 0; i < totalClasses; i++) {
            ExternalUseType externalUseType = externalUseTypesOnCanvas.get(i);
            JsonObject diagramObject = makeExternalUseTypeJsonObject(externalUseType);
            arrayBuilder.add(diagramObject);
        }
    }

    /**
     * Makes a JSON object for external data types with their name, x and y
     * coordinates
     *
     * @param diagram
     * @return
     */
    private JsonObject makeExternalDataTypeJsonObject(ExternalDataType diagram) {
        JsonObject jso = Json.createObjectBuilder()
                .add(DIAGRAM_NAME, diagram.getName())
                .add(DIAGRAM_X, diagram.getRootContainer().getLayoutX())
                .add(DIAGRAM_Y, diagram.getRootContainer().getLayoutY())
                .build();

        return jso;
    }

    /**
     * Makes a JSON object for external parent types with their name, x and y
     * coordinates
     *
     * @param diagram
     * @return
     */
    private JsonObject makeExternalParentTypeJsonObject(ExternalParent diagram) {
        JsonObject jso = Json.createObjectBuilder()
                .add(DIAGRAM_NAME, diagram.getName())
                .add(DIAGRAM_X, diagram.getRootContainer().getLayoutX())
                .add(DIAGRAM_Y, diagram.getRootContainer().getLayoutY())
                .build();

        return jso;
    }

    /**
     * Makes a JSON object for external parent types with their name, x and y
     * coordinates
     *
     * @param diagram
     * @return
     */
    private JsonObject makeExternalUseTypeJsonObject(ExternalUseType diagram) {
        JsonObject jso = Json.createObjectBuilder()
                .add(DIAGRAM_NAME, diagram.getName())
                .add(DIAGRAM_X, diagram.getRootContainer().getLayoutX())
                .add(DIAGRAM_Y, diagram.getRootContainer().getLayoutY())
                .build();

        return jso;
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

        if (diagram.getParentName() == null) {
            diagram.setParentName("");
        }

        JsonObject jso = Json.createObjectBuilder().add(DIAGRAM_TYPE, type)
                .add(DIAGRAM_NAME, diagram.getClassNameText().getText())
                .add(PACKAGE_NAME, diagram.getPackageNameText().getText())
                .add(PARENT, diagram.getParentName())
                .add(JSON_EXTERNAL_INTERFACES_IMPLEMENTED, makeExternalImplementedInterfacesJsonArray(diagram))
                .add(JSON_LOCAL_INTERFACES_IMPLEMENTED, makeLocalImplementedInterfacesJsonArray(diagram))
                .add(JSON_IMPORTED_PACKAGES, makeImportedPackagesJsonArray(diagram))
                .add(JSON_DIAGRAM_DIMENSIONS, makeDimensionsJsonArray(diagram))
                .add(JSON_VARIABLES, makeVariablesJsonArray(diagram))
                .add(JSON_METHODS, makeMethodsJsonArray(diagram))
                .build();

        return jso;
    }

    /**
     * Builds and returns an array of all the imported packages
     *
     * @param diagram
     * @return
     */
    private JsonArray makeImportedPackagesJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<String> importedPackages = diagram.getJavaAPI_Packages();

        for (String packageToAdd : importedPackages) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(IMPORTED_PACKAGE, packageToAdd)
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * Builds and returns an array of all the external interfaces implemented in
     * the class
     *
     * @param diagram
     * @return
     */
    private JsonArray makeExternalImplementedInterfacesJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<String> externalInterfaces = diagram.getExternalInterfaces();

        for (String interfaceToAdd : externalInterfaces) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(EXTERNAL_INTERFACE_NAME, interfaceToAdd)
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * Builds and returns an array of all the local interfaces implemented in
     * the class
     *
     * @param diagram
     * @return
     */
    private JsonArray makeLocalImplementedInterfacesJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<String> externalInterfaces = diagram.getLocalInterfaces();

        for (String interfaceToAdd : externalInterfaces) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(LOCAL_INTERFACE_NAME, interfaceToAdd)
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
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
     *
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
                    .add(VARIABLE_IS_STATIC, variable.getIsStatic())
                    .add(VARIABLE_IS_FINAL, variable.getIsFinal())
                    .add(VARIABLE_ACCESS, variable.getAccess())
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * Makes a Json array of all the methods in the class
     *
     * @param diagram
     * @return
     */
    private JsonArray makeMethodsJsonArray(ClassDiagramObject diagram) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        ArrayList<MethodObject> methods = diagram.getMethods();

        for (MethodObject method : methods) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(METHOD_NAME, method.getName())
                    .add(METHOD_IS_STATIC, method.getIsStatic())
                    .add(METHOD_IS_ABSTRACT, method.getIsAbstract())
                    .add(METHOD_ARGUMENTS, makeArgumentsArray(method))
                    .add(METHOD_RETURN_TYPE, method.getReturnType())
                    .add(METHOD_ACCESS, method.getAccess())
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * Makes a Json array of all the arguments in the method
     *
     * @param method
     * @return
     */
    private JsonArray makeArgumentsArray(MethodObject method) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ArrayList<ArgumentObject> arguments = method.getArguments();

        for (ArgumentObject argument : arguments) {
            JsonObject jso = Json.createObjectBuilder()
                    .add(ARGUMENT_NAME, argument.getName())
                    .add(ARGUMENT_TYPE, argument.getType())
                    .build();
            arrayBuilder.add(jso);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        DiagramController diagramController = new DiagramController();
        dataManager.reset();
        System.out.println("LOAD DATA CALLED");
        JsonObject json = loadJSONFile(filePath);

        //LOAD ALL THE EXTERNAL DATA TYPES LIST
        JsonArray jsonExternalDataTypesArray = json.getJsonArray(JSON_EXTERNAL_DATA_TYPES_LIST);
        for (int i = 0; i < jsonExternalDataTypesArray.size(); i++) {
            JsonObject jsonExternalDataType = jsonExternalDataTypesArray.getJsonObject(i);
            ExternalDataType externalDataType = loadExternalDataType(jsonExternalDataType);
            dataManager.attachExternalDataTypeBoxHandlers(externalDataType);
            dataManager.externalDataTypes.add(externalDataType.getName());
            dataManager.externalDataTypesOnCanvas.add(externalDataType);
            //render the thing
            externalDataType.putOnCanvasAfterLoading(dataManager.getRenderingPane());
        }

        //LAOD ALL THE EXTERNAL PARENTS
        JsonArray jsonExternalParentsArray = json.getJsonArray(JSON_EXTERNAL_PARENT_TYPES_LIST);
        for (int i = 0; i < jsonExternalParentsArray.size(); i++) {
            JsonObject jsonExternalParent = jsonExternalParentsArray.getJsonObject(i);
            ExternalParent externalParent = loadExternalParent(jsonExternalParent);
            dataManager.attachExternalParentDiagramHandlers(externalParent);
            dataManager.externalParents.add(externalParent.getName());
            dataManager.externalParentsOnCanvas.add(externalParent);
            //render the thing
            externalParent.putOnCanvasAfterLoading(dataManager.getRenderingPane());
        }

        //LOAD ALL THE EXTERNAL USE TYPES
        JsonArray jsonExternalUseTypesArray = json.getJsonArray(JSON_EXTERNAL_USE_TYPES_LIST);
        for (int i = 0; i < jsonExternalUseTypesArray.size(); i++) {
            JsonObject jsonExternalUseType = jsonExternalUseTypesArray.getJsonObject(i);
            ExternalUseType externalUseType = loadExternalUseType(jsonExternalUseType);
            dataManager.attachExternalUseTypeBoxHandlers(externalUseType);
            dataManager.externalUseTypes.add(externalUseType.getName());
            dataManager.externalUseTypesOnCanvas.add(externalUseType);
            //render the thing
            externalUseType.putOnCanvasAfterLoading(dataManager.getRenderingPane());
        }

        // AND NOW GET ALL THE SHAPES
        JsonArray jsonDiagramsArray = json.getJsonArray(JSON_DIAGRAMS_LIST);
        //LOAD ALL THE SHAPES
        for (int i = 0; i < jsonDiagramsArray.size(); i++) {
            JsonObject jsonDiagram = jsonDiagramsArray.getJsonObject(i);
            ClassDiagramObject classDiagram = loadClassDiagram(jsonDiagram, data);
            dataManager.attachClassDiagramEventHandlers(classDiagram);
            dataManager.classesOnCanvas.add(classDiagram);
            classDiagram.putOnCanvas(dataManager.getRenderingPane());
        }

        //NOW THAT THE DIAGRAMS ARE LOADED, BUILD ALL THE LINES
        for (ClassDiagramObject classDiagramObject : dataManager.classesOnCanvas) {
            //MAKE ALL THE PARENT RELATION LINES
            diagramController.setParentNameForLoadedDiagram(classDiagramObject.getParentName(), dataManager, classDiagramObject);
            //ALL THE EXTERNAL INTERFACE BOXES
            for (String externalInterfaceToAdd : classDiagramObject.getExternalInterfaces()) {
                diagramController.addExternalInterfaceBox(classDiagramObject, externalInterfaceToAdd, dataManager, dataManager.getRenderingPane());
            }
            //ALL THE VARIABLE RELATION LINES
            for (VariableObject variable : classDiagramObject.getVariables()) {
                //render the variable box and line
                diagramController.addVariable(classDiagramObject, variable, dataManager, dataManager.getRenderingPane());
            }
            //ALL THE METHOD RELATIONS 
            for (MethodObject method : classDiagramObject.getMethods()) {
                diagramController.addMethod(classDiagramObject, method, dataManager);
            }
            //ALL THE LOCAL INTERFACE RELATION LINES
            for (String localInterface : classDiagramObject.getLocalInterfaces()) {
                diagramController.manageLocalInterfaceAddition(classDiagramObject, localInterface, dataManager, dataManager.getRenderingPane());
            }
        }

        //setting the canvas dimensions
        int canvasWidth = json.getInt(CANVAS_WIDTH);
        int canvasHeight = json.getInt(CANVAS_HEIGHT);
        dataManager.getRenderingPane().minWidth(canvasWidth);
        dataManager.getRenderingPane().minHeight(canvasHeight);
    }

    public ExternalUseType loadExternalUseType(JsonObject jsonExternalUseType) {
        //get the diagram's name
        String name = jsonExternalUseType.getString(DIAGRAM_NAME);

        ExternalUseType toAdd = new ExternalUseType(name);

        //get the coordinates
        int x = jsonExternalUseType.getInt(DIAGRAM_X);
        int y = jsonExternalUseType.getInt(DIAGRAM_Y);

        //put on the coordinates
        toAdd.getRootContainer().setLayoutX(x);
        toAdd.getRootContainer().setLayoutY(y);

        return toAdd;
    }

    public ExternalDataType loadExternalDataType(JsonObject jsonExternalDataType) {
        //get the diagram's name
        String name = jsonExternalDataType.getString(DIAGRAM_NAME);

        ExternalDataType toAdd = new ExternalDataType(name);

        //get the coordinates
        int x = jsonExternalDataType.getInt(DIAGRAM_X);
        int y = jsonExternalDataType.getInt(DIAGRAM_Y);

        //put on the coordinates
        toAdd.getRootContainer().setLayoutX(x);
        toAdd.getRootContainer().setLayoutY(y);

        return toAdd;
    }

    /**
     * Loads an external parent
     *
     * @param jsonExternalParent
     * @return
     */
    public ExternalParent loadExternalParent(JsonObject jsonExternalParent) {
        //get the diagram's name
        String name = jsonExternalParent.getString(DIAGRAM_NAME);

        ExternalParent toAdd = new ExternalParent(name);

        //get the coordinates
        int x = jsonExternalParent.getInt(DIAGRAM_X);
        int y = jsonExternalParent.getInt(DIAGRAM_Y);

        //put on the coordinates
        toAdd.getRootContainer().setLayoutX(x);
        toAdd.getRootContainer().setLayoutY(y);

        return toAdd;
    }

    /**
     * Loads a class diagram object
     *
     * @param jsonDiagram
     * @param data
     * @return
     */
    public ClassDiagramObject loadClassDiagram(JsonObject jsonDiagram, AppDataComponent data) {
        

        // Pane canvas = dataManager.getRenderingPane();
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
        //setting the parent name
        toAdd.setParentName(jsonDiagram.getString(PARENT));
        //render the diagram 
       

        int rootContainerWidth = dimensionsJsonObject.getInt(ROOT_CONTAINER_WIDTH);
        int rootContainerHeight = dimensionsJsonObject.getInt(ROOT_CONTAINER_HEIGHT);

        toAdd.getRootContainer().setMinSize(rootContainerWidth, rootContainerHeight);

        int packageContainerWidth = dimensionsJsonObject.getInt(PACKAGE_CONTAINER_WIDTH);
        int packageContainerHeight = dimensionsJsonObject.getInt(PACKAGE_CONTAINER_HEIGHT);
        toAdd.getPackageContainer().setMinSize(packageContainerWidth, packageContainerHeight);

        //int variablesContainerWidth = dimensionsJsonObject.getInt(VARIABLES_CONTAINER_WIDTH);
        int variablesContainerHeight = dimensionsJsonObject.getInt(VARIABLES_CONTAINER_HEIGHT);
        toAdd.getVariablesContainer().setMinHeight(variablesContainerHeight);

        //int methodsContainerWidth = dimensionsJsonObject.getInt(METHODS_CONTAINER_WIDTH);
        int methodsContainerHeight = dimensionsJsonObject.getInt(METHODS_CONTAINER_HEIGHT);
        toAdd.getMethodsContainer().setMinHeight(methodsContainerHeight);

        //ALL THE EXTERNAL INTERFACES OF THE CLASS
        JsonArray externalInterfacesArray = jsonDiagram.getJsonArray(JSON_EXTERNAL_INTERFACES_IMPLEMENTED);
        for (int i = 0; i < externalInterfacesArray.size(); i++) {
            JsonObject current = externalInterfacesArray.getJsonObject(i);
            String interfaceName = current.getString(EXTERNAL_INTERFACE_NAME);
            //add to the list of external interfaces
            toAdd.addExternalInterface(interfaceName);

        }

//         //ALL THE LOCAL INTERFACES OF THE CLASS
        JsonArray localInterfacesArray = jsonDiagram.getJsonArray(JSON_LOCAL_INTERFACES_IMPLEMENTED);
        for (int i = 0; i < localInterfacesArray.size(); i++) {
            JsonObject current = localInterfacesArray.getJsonObject(i);
            String interfaceName = current.getString(LOCAL_INTERFACE_NAME);
            //add the external interface to the list of external interfaces
            toAdd.addLocalInterface(interfaceName);
        }

        //ALL THE PACKAGES TO BE IMPORTED
        JsonArray packagesArray = jsonDiagram.getJsonArray(JSON_IMPORTED_PACKAGES);
        for (int i = 0; i < packagesArray.size(); i++) {
            JsonObject current = packagesArray.getJsonObject(i);
            String packageImported = current.getString(IMPORTED_PACKAGE);
            //add the external interface to the list of external interfaces
            toAdd.addAPI(packageImported);
        }

        //ALL THE VARIABLES OF THE CLASS
        JsonArray variablesArray = jsonDiagram.getJsonArray(JSON_VARIABLES);
        for (int i = 0; i < variablesArray.size(); i++) {
            JsonObject current = variablesArray.getJsonObject(i);
            String name = current.getString(VARIABLE_NAME);
            String variableType = current.getString(VARIABLE_TYPE);
            boolean isStatic = current.getBoolean(VARIABLE_IS_STATIC);
            boolean isFinal = current.getBoolean(VARIABLE_IS_FINAL);
            String access = current.getString(VARIABLE_ACCESS);

            VariableObject varToAdd = new VariableObject(name, variableType, isStatic, isFinal, access);
            //add to the list of variables
            toAdd.getVariables().add(varToAdd);
        }

        //ALL THE METHODS OF THE CLASS
        JsonArray methodsArray = jsonDiagram.getJsonArray(JSON_METHODS);
        for (int i = 0; i < methodsArray.size(); i++) {
            JsonObject current = methodsArray.getJsonObject(i);

            String name = current.getString(METHOD_NAME);
            boolean isStatic = current.getBoolean(METHOD_IS_STATIC);
            boolean isAbstract = current.getBoolean(METHOD_IS_ABSTRACT);

            String returnType = current.getString(METHOD_RETURN_TYPE);
            String access = current.getString(METHOD_ACCESS);

            JsonArray methodArguments = current.getJsonArray(METHOD_ARGUMENTS);
            ArrayList<ArgumentObject> arguments = new ArrayList<>();

            // ALL THE ARGUMENTS
            for (int j = 0; j < methodArguments.size(); j++) {
                JsonObject argument = methodArguments.getJsonObject(j);

                String argName = argument.getString(ARGUMENT_NAME);
                String argType = argument.getString(ARGUMENT_TYPE);

                ArgumentObject argumentToAdd = new ArgumentObject(argName, argType);
                arguments.add(argumentToAdd);
            }
            //add the method
            MethodObject methodToAdd = new MethodObject(name, isStatic, isAbstract, arguments, returnType, access);
            toAdd.getMethods().add(methodToAdd);
        }
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
