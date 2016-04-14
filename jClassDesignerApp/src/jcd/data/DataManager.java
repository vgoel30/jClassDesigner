/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import jcd.gui.Workspace;
import maf.AppTemplate;
import maf.components.AppDataComponent;

/**
 *
 * @author varungoel
 */
public class DataManager implements AppDataComponent{
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    //this will keep track of all the classes currently on the canvas
    public ArrayList<ClassDiagramObject> classesOnCanvas = new ArrayList<>();
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }

    @Override
    public void reset() {
        ((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
}
