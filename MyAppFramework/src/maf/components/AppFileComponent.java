/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf.components;

import java.io.IOException;

/**
 * This interface provides the structure for file components in our
 * applications. Note that by doing so we make it possible for customly provided
 * descendent classes to have their methods called from this framework
 *
 * @author varungoel
 */
public interface AppFileComponent {

    public void saveData(AppDataComponent data, String filePath) throws IOException;

    public void loadData(AppDataComponent data, String filePath) throws IOException;

    public void exportData(AppDataComponent data, String filePath) throws IOException;

    public void importData(AppDataComponent data, String filePath) throws IOException;
}
