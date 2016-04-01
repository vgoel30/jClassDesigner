/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf.components;

/**
 * * This interface provides the structure for data components in
 * our applications. Note that by doing so we make it possible
 * for customly provided descendent classes to have their reset
 * method called from this framework.
 * 
 * @author varungoel
 */
public interface AppDataComponent {
    public void reset();
}
