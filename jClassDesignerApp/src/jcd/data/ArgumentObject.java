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
public class ArgumentObject {
    
    String name;
    String type;
    
    public ArgumentObject(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public String toString() {
        if (!name.equals("")) {
            return name + ":" + type;
        }
        else{
            return "";
        }
    }
    
    public String toStringCode() {
        return type + " " + name;
    }
}
