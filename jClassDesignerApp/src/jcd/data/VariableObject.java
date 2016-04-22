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
    boolean isFinal;
    String access;
    String value = "";
    
    public static void main(String[] args){
        VariableObject sample = new VariableObject("integerVariable", "int", true, false, "public");
        //System.out.println(sample.toStringCode());
       
    }

    public VariableObject(String name, String type, boolean isStatic, boolean isFinal, String access) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public VariableObject(String name, String type, boolean isStatic, String access, String value) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.access = access;
        this.value = value;
    }

    public VariableObject(String name, String type, boolean isStatic, String access) {
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

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        
        String toReturn = "";

        if (this.access.equals("private")) {
            toReturn += "- ";
        } else {
            toReturn += "+ ";
        }

        if (isStatic) {
            toReturn += "static";
        }

        if (isFinal) {
            toReturn += name.toUpperCase();
        } else {
            toReturn += name + " : ";
        }
        toReturn += type;

        return toReturn;
    }

    public String toStringCode() {
        
        String toReturn = "";

        if (this.access.equals("private")) {
            toReturn += "private ";
        } else {
            toReturn += "public ";
        }

        if (isStatic) {
            toReturn += "static ";
        }
        
        if(isFinal){
            toReturn += "final ";
        }

        toReturn += type + " ";
        if (isFinal) {
            toReturn += name.toUpperCase() + ";";
        } else {
            toReturn += name + ";";
        }

        String[] split = toReturn.split("\\s");
        
        System.out.println("IMPORT1: " + split[split.length-2] + " " + split[split.length-1]);
        
        
        
        return toReturn;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

   
    public boolean getIsFinal() {
        return isFinal;
    }
}
