///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package jcd.test_bed;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import jcd.data.ClassDiagramObject;
//import jcd.file.FileManager;
//
///**
// *
// * @author varungoel
// */
//public class TestLoad {
//
//    //arraylist fo diagrams to be loaded from the filemanager
//    static ArrayList<ClassDiagramObject> diagrams = new ArrayList<>();
//    static FileManager myManager = new FileManager();
//
//    public static void main(String[] args) throws IOException {
//        populateArray();
//    }
//
//    public static void populateArray() {
//        try {
//            diagrams = myManager.testLoadData("./work/DesignSaveTest.json");
//        } catch (IOException ex) {
//
//        }
//    }
//
//    public static String getData1() {
//        populateArray();
//        System.out.println("DONE WITH IT : " + diagrams.get(0).getVariables().get(0).toString());
//        return diagrams.get(0).getVariables().get(0).toString();
//    }
//
//    public static String getData2() {
//        populateArray();
//        return diagrams.get(0).getDiagramType();
//    }
//
//    public static double getData3() {
//        populateArray();
//        return diagrams.get(0).getX();
//    }
//
//    public static double getData4() {
//        populateArray();
//        return diagrams.get(0).getY();
//    }
//
//    public static double getData5() {
//        populateArray();
//        return diagrams.get(1).getX();
//    }
//
//    public static String getData6() {
//        populateArray();
//        return (diagrams.get(4).getVariables().get(3).toString());
//    }
//
//    public static int getData7() {
//        populateArray();
//        return (diagrams.get(1).getVariables().size());
//    }
//
//    public static int getData8() {
//        populateArray();
//        return diagrams.get(1).getMethods().size();
//    }
//
//    public static String getData9() {
//        populateArray();
//        return (diagrams.get(3).getVariables().get(0).toString());
//    }
//
//    public static String getData10() {
//        populateArray();
//        return diagrams.get(1).getDiagramType();
//    }
//
//}
