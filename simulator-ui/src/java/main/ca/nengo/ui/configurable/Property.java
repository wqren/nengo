/*
The contents of this file are subject to the Mozilla Public License Version 1.1
(the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS" basis, WITHOUT
WARRANTY OF ANY KIND, either express or implied. See the License for the specific
language governing rights and limitations under the License.

The Original Code is "Property.java". Description:
"@author Shu"

The Initial Developer of the Original Code is Bryan Tripp & Centre for Theoretical Neuroscience, University of Waterloo. Copyright (C) 2006-2008. All Rights Reserved.

Alternatively, the contents of this file may be used under the terms of the GNU
Public License license (the GPL License), in which case the provisions of GPL
License are applicable  instead of those above. If you wish to allow use of your
version of this file only under the terms of the GPL License and not to allow
others to use your version of this file under the MPL, indicate your decision
by deleting the provisions above and replace  them with the notice and other
provisions required by the GPL License.  If you do not delete the provisions above,
a recipient may use your version of this file under either the MPL or the GPL License.
 */

package ca.nengo.ui.configurable;

import java.io.Serializable;

import ca.nengo.ui.lib.util.Util;

/**
 * Describes a configuration parameter of a IConfigurable object
 * 
 * @author Shu
 */
public abstract class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object defaultValue = null;
    private String description;
    private boolean isEditable = true;
    private boolean isAdvanced = false;
    private String name;

    /**
     * @param name
     * @param description
     * @param defaultValue
     */
    public Property(String name, String description, Object defaultValue) {
        this.description = description;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    /**
     * @return Name of this property
     */
    public String getName() {
        return name;
    }

    /**
     * @return Default value of this property
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return Description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the input panel
     */
    public final PropertyInputPanel getInputPanel() {
        // Instantiate a new input panel for each call, this is ok.
        Util.Assert(!(!isEditable && (getDefaultValue() == null)),
                "An input panel cannot be disabled and have no default value");

        PropertyInputPanel inputPanel = createInputPanel();

        if (getDefaultValue() != null) {
            inputPanel.setValue(getDefaultValue());
        }
        inputPanel.setEnabled(isEditable);

        return inputPanel;
    }

    /**
     * @return String to be displayed in a tooltip
     */
    public String getTooltip() {
        int width = 250;
        String css = "<style type = \"text/css\">" +
                "body { width: " + width + "px }" +
                "p { margin-top: 12px }" +
                "</style>";
        
        String sType = "Type: " + getTypeName();
        String sBody;
        if (description != null) {
        	sBody = "<p><b>" + description + "</b></p>"
        		+ "<p>" + sType + "</p>";
        } else {
        	sBody = sType;
        }

        String sHTML = "<html><head>" + css + "</head><body>" + sBody + "</body></html>";
        return sHTML;
    }

    /**
     * @return Class type that this parameter's value must be
     */
    public abstract Class<?> getTypeClass();

    /**
     * @return A name given to the Class type of this parameter's value
     */
    public String getTypeName() {
    	return getTypeClass().getCanonicalName();
    }

    /**
     * @return Whether this property can be changed from its default value
     */
    public boolean isEditable() {
        return isEditable;
    }
    
    public boolean isAdvanced() {
    	return isAdvanced;
    }

    /**
     * @param defaultValue
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param bool Whether this property can be changed from its default value
     */
    public void setEditable(boolean bool) {
        isEditable = bool;
    }
    
    public void setAdvanced(boolean bool) {
    	isAdvanced = bool;
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    /**
     * @return UI Input panel which can be used for User Configuration of this
     *         property, null if none exists
     */
    protected abstract PropertyInputPanel createInputPanel();

}
