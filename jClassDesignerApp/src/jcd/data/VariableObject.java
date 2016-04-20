/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

/**
 *
 * @author varungoel
 */
public class VariableObject {
    String name;
    String type;
    boolean isStatic;
    String access;
    
    public VariableObject(String name, String type, boolean isStatic, String access){
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public String getAccess() {
        return access;
    }
    
    public String toString(){
        String privacy;
        if(this.access.equals("private"))
            privacy = "-";
        else
            privacy = "+";
        
        return privacy + name + " : " + type;
    }
}
