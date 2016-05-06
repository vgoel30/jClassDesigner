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
import jcd.connector_lines.AggregateLine;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

/**
 *
 * @author varungoel
 */
public class ExternalDataType extends Diagram{
    String type;
    Text nameText;
    
    //all the classes which use this data type
    public ArrayList<ClassDiagramObject> usedBy = new ArrayList<>();
    //lines emitted
    public ArrayList<AggregateLine> emittedLines = new ArrayList<>();
   
    public ExternalDataType(String nameToSet){
        type = "external_data_type";
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
        System.out.println("EDT: " + canvas.hashCode());
        
    }

    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
    
    

    @Override
    public VBox getRootContainer() {
        return rootContainer;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    //set the style for the diagram
    private void initStyle() {
        nameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        rootContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
    }
}
