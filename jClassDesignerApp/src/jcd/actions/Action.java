/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.actions;

/**
 * A custom class that will be the parent to all the possible actions allowed in our app. Requied for the undo and redo
 * functionality.
 * @author varungoel
 */
public class Action {
    
     String actionType;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
}
