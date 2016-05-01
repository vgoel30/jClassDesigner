/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

import jcd.data.ClassDiagramObject;
import jcd.data.MethodObject;
import jcd.data.VariableObject;

/**
 *Represents a remove variable move
 * @author varungoel
 */
public class RemoveMethod extends Action{
    
    ClassDiagramObject diagram;
    MethodObject removedMethod;
    
    public RemoveMethod(ClassDiagramObject diagram){
        this.diagram = diagram;
        this.actionType = "remove_method";
    }

    public ClassDiagramObject getDiagram() {
        return diagram;
    }

    public void setDiagram(ClassDiagramObject diagram) {
        this.diagram = diagram;
    }

    public MethodObject getRemovedMethod() {
        return removedMethod;
    }

    public void setRemovedMethod(MethodObject removedMethod) {
        this.removedMethod = removedMethod;
    }

   
    
    
}
