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
 *This will represent a class diagram UML object 
 * @author varungoel
 */
public class ClassDiagram {
    
    //this will hold the three panes and serve as the skeleton for the diagram
    VBox rootContainer;
    //the container with the class name
    VBox nameContainer;
    //the container with the variables name
    VBox variablesContainer;
    //the container with the methods name
    VBox methodsContainer;
    
    //the class name text
    Text classNameText;
    //the variables name text
    Text variablesNameText;
    //the methods text
    Text methodsNameText;
    
    
    public ClassDiagram(){
        rootContainer = new VBox();
    }
    
    
}
