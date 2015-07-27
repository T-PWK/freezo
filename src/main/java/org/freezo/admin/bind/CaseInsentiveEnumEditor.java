package org.freezo.admin.bind;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

public class CaseInsentiveEnumEditor<T extends Enum<T>> extends PropertyEditorSupport implements PropertyEditor
{
	private final Class<T> type;

	public CaseInsentiveEnumEditor(final Class<T> type)
	{
		this.type = type;
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException
	{
		setValue(Enum.valueOf(type, text.toUpperCase()));
	}
}
