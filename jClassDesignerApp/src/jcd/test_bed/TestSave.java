/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import jcd.data.ArgumentObject;
import jcd.data.ClassDiagramObject;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import jcd.file.FileManager;

/**
 *
 * @author varungoel
 */
public class TestSave {

    public static void main(String[] args) throws FileNotFoundException {
        FileManager fileManager = new FileManager();

        //store all the classes
        ArrayList<ClassDiagramObject> classDiagramObjects = new ArrayList<>();

        //hard coding the first class
        ArrayList<VariableObject> variables1 = new ArrayList<>();
        ArrayList<MethodObject> methods1 = new ArrayList<>();

        VariableObject app = new VariableObject("app", "ThreadExample", false, "private");
        VariableObject counter = new VariableObject("counter", "int", false, "private");
        variables1.add(app);
        variables1.add(counter);

        MethodObject call = new MethodObject("call", false, false, new ArrayList<>(), "void", "protected");
        methods1.add(call);

        ClassDiagramObject CounterTask = new ClassDiagramObject("CounterTask", "class", methods1, variables1);
        classDiagramObjects.add(CounterTask);
        //the first class is done. Onto the second one.

        //hard coding the second class (DateTask.java)
        ArrayList<VariableObject> variables2 = new ArrayList<>();
        ArrayList<MethodObject> methods2 = new ArrayList<>();

        VariableObject dateTaskApp = new VariableObject("app", "ThreadExample", false, "private");
        VariableObject now = new VariableObject("now", "Date", false, "private");
        variables2.add(dateTaskApp);
        variables2.add(now);

        MethodObject callDateTask = new MethodObject("call", false, false, new ArrayList<>(), "void", "protected");
        methods2.add(call);

        ClassDiagramObject DateTask = new ClassDiagramObject("DateTask", "class", methods2, variables2);
        classDiagramObjects.add(DateTask);
        //second class is done

        //hard coding the third class (PauseHandler.java)
        ArrayList<VariableObject> variables3 = new ArrayList<>();
        ArrayList<MethodObject> methods3 = new ArrayList<>();

        VariableObject pauseHandlerApp = new VariableObject("app", "ThreadExample", false, "private");
        variables3.add(pauseHandlerApp);

        ArrayList<ArgumentObject> arguments3 = new ArrayList<>();
        arguments3.add(new ArgumentObject("event", "Event"));
        MethodObject handle = new MethodObject("handle", false, false, arguments3, "void", "public");
        methods3.add(handle);

        ClassDiagramObject PauseHandler = new ClassDiagramObject("PauseHandler", "class", methods3, variables3);
        classDiagramObjects.add(PauseHandler);
        //third class is done

        //hard coding the 4th class (StartHandler.java)
        ArrayList<VariableObject> variables4 = new ArrayList<>();
        ArrayList<MethodObject> methods4 = new ArrayList<>();

        VariableObject startHandlerApp = new VariableObject("app", "ThreadExample", false, "private");
        variables4.add(startHandlerApp);

        ArrayList<ArgumentObject> arguments4 = new ArrayList<>();
        arguments4.add(new ArgumentObject("event", "Event"));
        MethodObject handle4 = new MethodObject("handle", false, false, arguments4, "void", "public");
        methods4.add(handle);

        ClassDiagramObject StartHandler = new ClassDiagramObject("StartHandler", "class", methods4, variables4);
        classDiagramObjects.add(StartHandler);
        //fourth class is done

        //hard coding the 5th class 
        ArrayList<VariableObject> variables5 = new ArrayList<>();
        ArrayList<MethodObject> methods5 = new ArrayList<>();

        VariableObject start_text = new VariableObject("START_TEXT", "String", true, "public", "Start");
        variables5.add(start_text);

        VariableObject pause_text = new VariableObject("PAUSE_TEXT", "String", true, "public", "Pause");
        variables5.add(pause_text);

        VariableObject window = new VariableObject("window", "Stage", false, "private");
        variables5.add(window);

        VariableObject appPane = new VariableObject("appPane", "BorderPane", false, "private");
        variables5.add(appPane);

        VariableObject topPane = new VariableObject("topPane", "FlowPane", false, "private");
        variables5.add(topPane);

        VariableObject startButton = new VariableObject("startButton", "Button", false, "private");
        variables5.add(startButton);

        VariableObject pauseButton = new VariableObject("pauseButton", "Button", false, "private");
        variables5.add(pauseButton);

        VariableObject scrollPane = new VariableObject("scrollPane", "ScrollPane", false, "private");
        variables5.add(scrollPane);

        VariableObject textArea = new VariableObject("textArea", "TextArea", false, "private");
        variables5.add(textArea);

        VariableObject dateThread = new VariableObject("dateThread", "Thread", false, "private");
        variables5.add(dateThread);

        VariableObject dateTask = new VariableObject("dateTask", "Task", false, "private");
        variables5.add(dateTask);

        VariableObject counterThread = new VariableObject("counterThread", "Thread", false, "private");
        variables5.add(counterThread);

        VariableObject counterTask = new VariableObject("counterTask", "Task", false, "private");
        variables5.add(counterTask);

        VariableObject work = new VariableObject("work", "boolean", false, "private");
        variables5.add(work);

        ArrayList<ArgumentObject> startArguments = new ArrayList<>();
        ArgumentObject primaryStage = new ArgumentObject("primaryStage", "Stage");
        startArguments.add(primaryStage);
        MethodObject start = new MethodObject("start", false, false, startArguments, "void", "public");
        methods5.add(start);

        MethodObject startWork = new MethodObject("startWork", false, false, new ArrayList<>(), "void", "public");
        methods5.add(startWork);

        MethodObject pauseWork = new MethodObject("pauseWork", false, false, new ArrayList<>(), "void", "public");
        methods5.add(pauseWork);

        MethodObject doWork = new MethodObject("doWork", false, false, new ArrayList<>(), "boolean", "public");
        methods5.add(doWork);

        ArrayList<ArgumentObject> appendTextArguments = new ArrayList<>();
        ArgumentObject textToAppend = new ArgumentObject("textToAppend", "String");
        appendTextArguments.add(primaryStage);
        MethodObject appendText = new MethodObject("appendText", false, false, appendTextArguments, "void", "public");
        methods5.add(appendText);

        ArrayList<ArgumentObject> sleepArguments = new ArrayList<>();
        ArgumentObject timeToSleep = new ArgumentObject("timeToSleep", "int");
        sleepArguments.add(primaryStage);
        MethodObject sleep = new MethodObject("sleep", false, false, sleepArguments, "void", "public");
        methods5.add(sleep);

        MethodObject initLayout = new MethodObject("initLayout", false, false, new ArrayList<>(), "void", "private");
        methods5.add(initLayout);
        
        MethodObject initHandlers = new MethodObject("initHandlers", false, false, new ArrayList<>(), "void", "private");
        methods5.add(initHandlers);
        
        ArrayList<ArgumentObject> initWindowArguments = new ArrayList<>();
        ArgumentObject initPrimaryStage = new ArgumentObject("initPrimaryStage", "Stage");
        initWindowArguments.add(initPrimaryStage);
        MethodObject initWindow = new MethodObject("initWindow", false, false, initWindowArguments, "void", "public");
        methods5.add(initWindow);
        
        MethodObject initThreads = new MethodObject("initThreads", false, false, new ArrayList<>(), "void", "private");
        methods5.add(initThreads);
        
        ArrayList<ArgumentObject> mainMethodArguments = new ArrayList<>();
        ArgumentObject argsi = new ArgumentObject("args", "String[]");
        mainMethodArguments.add(argsi);
        MethodObject mainMethod = new MethodObject("main", true, false, mainMethodArguments, "void", "public");
        methods5.add(mainMethod);

        ClassDiagramObject ThreadExample = new ClassDiagramObject("ThreadExample", "class", methods5, variables5);
        classDiagramObjects.add(ThreadExample);
        //5th class is done

        fileManager.testSaveData(classDiagramObjects, "./work/DesignSaveTest.json");
    }

}
