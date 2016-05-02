/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static maf.components.AppStyleArbiter.DIAGRAM_CONTAINERS;
import static maf.components.AppStyleArbiter.DIAGRAM_TEXT_FIELD;

/**
 *
 * @author varungoel
 */
public class ExternalParent extends Diagram{
    String type;
    String name;
    VBox rootContainer;
    Text nameText;
    
   
    public ExternalParent(String nameToSet){
        type = "external_parent";
        rootContainer = new VBox();
        nameText = new Text("\n" + "       "+nameToSet +"      "+"\n");
        this.name = nameToSet;
        rootContainer.getChildren().add(nameText);
        initStyle();
    }
    
    public void putOnCanvas(Pane canvas){
        rootContainer.setLayoutX(0);
        rootContainer.setLayoutY(0);
        canvas.getChildren().add(rootContainer);
        
    }

    public VBox getRootContainer() {
        return rootContainer;
    }
    
    
    
    //set the style for the diagram
    private void initStyle() {
        nameText.getStyleClass().add(DIAGRAM_TEXT_FIELD);
        rootContainer.getStyleClass().add(DIAGRAM_CONTAINERS);
    }
}
