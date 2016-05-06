/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jcd.connector_lines.InheritanceLine;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

/**
 *
 * @author varungoel
 */
public class ExternalParent extends Diagram{
    String type;
    String name;
    Text nameText;
    
    public ArrayList<ClassDiagramObject> children = new ArrayList<>();
    public ArrayList<InheritanceLine> parentalLines = new ArrayList<>();
   
    public ExternalParent(String nameToSet){
        type = "external_parent";
        rootContainer = new VBox();
        nameText = new Text("\n" + "       "+nameToSet +"      "+"\n");
        this.name = nameToSet;
        rootContainer.getChildren().add(nameText);
        initStyle();
    }
    
    public void putOnCanvas(Pane canvas){
        rootContainer.setLayoutX(5);
        rootContainer.setLayoutY(5);
        canvas.getChildren().add(rootContainer);
    }
    
    public void putOnCanvas(Pane canvas, double x, double y){
        rootContainer.setLayoutX(x);
        rootContainer.setLayoutY(y);
        canvas.getChildren().add(rootContainer);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    
    

    public VBox getRootContainer() {
        return rootContainer;
    }
    
    public String toString(){
        return name;
    }
    
    //set the style for the diagram
    private void initStyle() {
        nameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        rootContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
    }
}
