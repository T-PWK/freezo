package org.freezo.admin.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserUpdateRequest
{
	public static enum RequestType
	{
		DISABLE,
		ENABLE,
		LOCK,
		UNLOCK;

		@JsonCreator
		public static RequestType forValue(final String text)
		{
			return RequestType.valueOf(text.toUpperCase());
		}
	}

	private RequestType type;

	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		return builder.append("type", type).toString();
	}

	public RequestType getType()
	{
		return type;
	}

	public void setType(final RequestType type)
	{
		this.type = type;
	}
}
