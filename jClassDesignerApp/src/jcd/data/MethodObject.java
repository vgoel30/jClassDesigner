/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;

/**
 *A custom method object that will be put inside our UML diagram
 * @author varungoel
 */
public class MethodObject {
    String name;
    boolean isStatic;
    boolean isAbstract;
    ArrayList<ArgumentObject> arguments = new ArrayList<>();
    String returnType;
    String access;

    public MethodObject(String name, boolean isStatic, boolean isAbstract, ArrayList<ArgumentObject> arguments, String returnType, String access) {
        this.name = name;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.arguments = arguments;
        this.returnType = returnType;
        this.access = access;
    }
    
    
    
    public void addArgument(ArgumentObject arg){
        arguments.add(arg);
    }

    public String getName() {
        return name;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public ArrayList<ArgumentObject> getArguments() {
        return arguments;
    }

    public String getReturnType() {
        return returnType;
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
        
        return privacy + name + "(" + arguments + "):" + returnType;
    }
}
