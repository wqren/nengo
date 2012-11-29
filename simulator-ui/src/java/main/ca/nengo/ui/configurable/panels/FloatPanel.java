/*
The contents of this file are subject to the Mozilla Public License Version 1.1
(the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS" basis, WITHOUT
WARRANTY OF ANY KIND, either express or implied. See the License for the specific
language governing rights and limitations under the License.

The Original Code is "FloatPanel.java". Description:
"Input panel for entering floating point numbers

  @author Shu Wu"

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

package ca.nengo.ui.configurable.panels;

import ca.nengo.ui.configurable.properties.PFloat;

/**
 * Input panel for entering floating point numbers
 * 
 * @author Shu Wu
 */
public class FloatPanel extends NumberPanel {

    public FloatPanel(PFloat property) {
		super(property);
	}

	@Override public Float getValue() {
        return Float.parseFloat(getText());
    }
	
	@Override protected TextError checkValue(String textValue) {
        if (textValue == null || textValue.equals("")) {
        	return TextError.ValueNotSet;
        } else if (!textValue.matches("\\s*-??[0-9]*[.]??[0-9]*([eE][-|\\+]??[0-9]+)??\\s*")) {
        	return TextError.InvalidFormat;
        } else if (getDescriptor().isCheckingRange() && !getDescriptor().isInRange(getValue())) {
        	return TextError.OutOfRange;
        } else {
        	return TextError.NoError;
        }
    }

}
