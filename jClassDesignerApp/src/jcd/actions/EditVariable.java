/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

import jcd.data.ClassDiagramObject;
import jcd.data.VariableObject;

/**
 *
 * @author varungoel
 */
public class EditVariable extends Action{
    
    VariableObject originalVariable;
    VariableObject editedVariable;
    
    ClassDiagramObject diagram;
    
    public EditVariable(ClassDiagramObject diagram, VariableObject originalVariable, VariableObject editedVariable){
        this.diagram = diagram;
        this.actionType = "edit_variable";
        
        this.originalVariable = originalVariable;
        this.editedVariable = editedVariable;
    }

    public VariableObject getOriginalVariable() {
        return originalVariable;
    }

    public VariableObject getEditedVariable() {
        return editedVariable;
    }

    public ClassDiagramObject getDiagram() {
        return diagram;
    }
    
    
    
}
