/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maf.components;

/**
 *This interface serves as a family of type that will initialize
 * the style for some set of controls, like the workspace, for example.
 * 
 * @author varungoel
 */
public interface AppStyleArbiter {
    // THESE ARE COMMON STYLE CLASSES WE'LL USE
    public static final String CLASS_BORDERED_PANE = "bordered_pane";
    public static final String CLASS_HEADING_LABEL = "heading_label";
    public static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    public static final String CLASS_PROMPT_LABEL = "prompt_label";
    public static final String CLASS_PROMPT_TEXT_FIELD = "prompt_text_field";
    public static final String CLASS_FILE_BUTTON = "file_button";
    public static final String CLASS_FILE_BUTTON_CONTAINER = "toolbar_container";
    public static final String CLASS_FILE_BUTTON_HOVERED = "file_button_hovered";
    public static final String CLASS_FILE_BUTTON_CONTAINER_HOVERED = "toolbar_container_hovered";
    public static final String CHECKBOX = "checkbox";
    public static final String EDIT_TOOLBAR = "edit_toolbar"; //the toolbar on the right side
    public static final String EDIT_TOOLBAR_ROW = "edit_toolbar_row"; //each row in the toolbar
    public static final String LABEL = "label";
    public static final String CONTAINER_BUTTON = "container_button";
    public static final String RENDERING_CANVAS = "rendering_canvas";
    public static final String TABLES = "table";
    public static final String BUTTON_PRESSED = "pressed";
    public static final String DIAGRAM_CONTAINER = "diagram_container";
    public static final String SELECTED_DIAGRAM_CONTAINER = "selected_diagram_container";
    public static final String DIAGRAM_CONTAINERS = "diagram_containers";
    public static final String DIAGRAM_TEXT = "diagram_text";
            
    
    public void initStyle();
}
