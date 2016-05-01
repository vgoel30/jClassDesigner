/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

import jcd.data.ClassDiagramObject;
import jcd.data.VariableObject;

/**
 *Represents a remove variable move
 * @author varungoel
 */
public class RemoveVariable extends Action{
    
    ClassDiagramObject diagram;
    VariableObject removedVariable;
    
    public RemoveVariable(ClassDiagramObject diagram){
        this.diagram = diagram;
        this.actionType = "remove_variable";
    }

    public ClassDiagramObject getDiagram() {
        return diagram;
    }

    public void setDiagram(ClassDiagramObject diagram) {
        this.diagram = diagram;
    }

    public VariableObject getRemovedVariable() {
        return removedVariable;
    }

    public void setRemovedVariable(VariableObject removedVariable) {
        this.removedVariable = removedVariable;
    }
    
    
}
