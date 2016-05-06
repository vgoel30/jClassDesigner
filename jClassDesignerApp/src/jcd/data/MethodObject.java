/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;

/**
 * A custom method object that will be put inside our UML diagram
 *
 * @author varungoel
 */
public class MethodObject{
    static final String PRIVATE = "private";
    static final String PUBLIC = "public";
    static final String DEFAULT = "default";
    static final String PROTECTED = "protected";

    String name;
    boolean isStatic;
    boolean isAbstract;
    ArrayList<ArgumentObject> arguments = new ArrayList<>();
    String returnType;
    String access;

    
    
    public MethodObject(){
        
    }
    
    public MethodObject(String name, boolean isStatic, boolean isAbstract, ArrayList<ArgumentObject> arguments, String returnType, String access) {
        this.name = name;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.arguments = arguments;
        this.returnType = returnType;
        this.access = access;
    }

    public void addArgument(ArgumentObject arg) {
        arguments.add(arg);
    }

    public String getName() {
        return name;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public boolean getIsAbstract() {
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
    
    public boolean equals(MethodObject o){
        if(!this.getName().equals(o.getName()))
            return false;
        if(!this.getReturnType().equals(o.getReturnType()))
            return false;
        return (this.getArguments().size() == o.getArguments().size());
    }

    public String toString() {
        String toReturn = "";
        
        String privacy;
        if (this.access.equals(PRIVATE)) {
            toReturn += "-";
        } else if (this.access.equals(PUBLIC)) {
            toReturn += "+";
        } else if (this.access.equals(PROTECTED)) {
            toReturn += "#";
        }
        
        if(isStatic)
            toReturn += "$";
        
        toReturn += name + "(";
        
        //adding all the arguments
        for (int i = 0; i < arguments.size(); i++) {
            if (i < arguments.size() - 1) {
                toReturn += arguments.get(i).toString() + ", ";
            } else {
                toReturn += arguments.get(i).toString() + "";
            }
        }
        toReturn += "): " + returnType;

        return toReturn;
    }
    
    

    public String toStringCode() {
        String toReturn = "";

        toReturn += access + " ";

        if (isStatic && !isAbstract) {
            toReturn += "static ";
        } else if (!isStatic && isAbstract) {
            toReturn += "abstract ";
        }

        toReturn += returnType + " ";
        toReturn += name + " (";

        //adding all the arguments
        for (int i = 0; i < arguments.size(); i++) {
            if (i < arguments.size() - 1) {
                toReturn += arguments.get(i).toStringCode() + ", ";
            } else {
                toReturn += arguments.get(i).toStringCode() + " ";
            }
        }

        //adding the opening curly brace
        toReturn += " ){\n";
        
        if(returnType.equals("char") || returnType.equals("byte") || returnType.equals("short") || returnType.equals("long") || returnType.equals("int")){
            toReturn += "\t\treturn 0; \n}";
        }
        else if(returnType.equals("double")){
            toReturn += "\t\treturn 0.0; \n}";
        }
        else if(returnType.equals("boolean")){
            toReturn += "\t\treturn false; \n}";
        }
        else if(returnType.equals("void")){
            toReturn += "\n}";
        }
        else{
            toReturn += "\t\treturn null; \n\t\t}";
        }
        
        
        return toReturn;
    }
    
    
}
