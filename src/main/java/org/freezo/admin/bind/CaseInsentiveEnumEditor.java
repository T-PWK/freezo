package org.freezo.admin.bind;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * Property editor changes string to enumeration instance via converting string into upper case.
 *
 * @author Freezo
 *
 * @param <T> Enumeration type
 */
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
