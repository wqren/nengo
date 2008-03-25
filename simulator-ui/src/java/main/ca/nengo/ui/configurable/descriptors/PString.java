package ca.nengo.ui.configurable.descriptors;

import ca.nengo.ui.configurable.Property;
import ca.nengo.ui.configurable.PropertyInputPanel;
import ca.nengo.ui.configurable.panels.StringPanel;

/**
 * Config Descriptor for Strings
 * 
 * @author Shu Wu
 */
public class PString extends Property {

	private static final long serialVersionUID = 1L;

	public PString(String name) {
		super(name);
	}

	public PString(String name, String description, String defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	protected PropertyInputPanel createInputPanel() {
		return new StringPanel(this);
	}

	@Override
	public Class<String> getTypeClass() {
		return String.class;
	}

	@Override
	public String getTypeName() {
		return "Text";
	}

}