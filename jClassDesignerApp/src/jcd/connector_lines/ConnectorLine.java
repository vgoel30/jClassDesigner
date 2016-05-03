/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.connector_lines;

import javafx.scene.shape.Line;
import jcd.data.Diagram;

/**
 * This will be the parent class for the connector lines
 * @author varungoel
 */
public class ConnectorLine extends Line{
    Diagram startDiagram;
    Diagram endDiagram;

    public Diagram getStartDiagram() {
        return startDiagram;
    }

    public Diagram getEndDiagram() {
        return endDiagram;
    }
    
    
}
