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
public class VariableObject implements Comparable<VariableObject> {

    static final String PRIVATE = "private";
    static final String PUBLIC = "public";
    static final String DEFAULT = "default";
    static final String PROTECTED = "protected";

    String name;
    String type;
    boolean isStatic;
    boolean isFinal;
    String access;
    String value = "";

    public static void main(String[] args) {
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

    public VariableObject() {
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

        if (this.access.equals(PRIVATE)) {
            toReturn += "- ";
        } else if (this.access.equals(PUBLIC)) {
            toReturn += "+ ";
        } else if (this.access.equals(PROTECTED)) {
            toReturn += "# ";
        }

        if (isStatic) {
            toReturn += "$";
        }

        if (isFinal) {
            toReturn += name.toUpperCase() + " : ";
        } else {
            toReturn += name + " : ";
        }
        toReturn += type;

        return toReturn;
    }

    public String toStringCode() {
        String toReturn = "";

        if (this.access.equals(PRIVATE) || this.access.equals(PUBLIC) || this.access.equals(PROTECTED)) {
            toReturn += this.access + " ";
        }

        if (isStatic) {
            toReturn += "static ";
        }

        if (isFinal) {
            toReturn += "final ";
        }

        toReturn += type + " ";
        if (isFinal) {
            toReturn += name.toUpperCase() + ";";
        } else {
            toReturn += name + ";";
        }
        return toReturn;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    /**
     * Custom compareTo function to see if two variables are the same
     * @param o
     * @return 
     */
    @Override
    public int compareTo(VariableObject o) {
        int x = this.getName().compareTo(o.getName());
        //if the names are not the same, we can be sure that the methods aren't the same
        if (x != 0) {
            return x;
        }
        //two methods might have the same name but different types
        return this.getType().compareTo(o.getType());
    }
    
    /**
     * Uses the compareTo function to see if the two variables are the same
     * @param o
     * @return 
     */
    public boolean equals(VariableObject o){
        return this.compareTo(o) == 0;
    }
}
