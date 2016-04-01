/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf.components;

/**
 * This interface provides the structure required for a builder object used for
 * initializing all components for this application. This is one means of
 * employing a component hierarchy.
 *
 * @author varungoel
 */
public interface AppComponentsBuilder {

    public AppDataComponent buildDataComponent() throws Exception;

    public AppFileComponent buildFileComponent() throws Exception;

    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception;
}
