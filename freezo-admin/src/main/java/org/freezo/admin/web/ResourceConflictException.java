package org.freezo.admin.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException
{
	private static final long serialVersionUID = 1367415622541896581L;

	public ResourceConflictException()
	{
	}

	public ResourceConflictException(final String message)
	{
		super(message);
	}
}
