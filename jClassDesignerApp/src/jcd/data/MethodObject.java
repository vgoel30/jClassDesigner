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
    ArrayList<String> arguments;
    String returnType;
}
