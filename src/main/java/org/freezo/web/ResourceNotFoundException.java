package org.freezo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 9847090455482956L;

	public ResourceNotFoundException()
	{
		super();
	}

	public ResourceNotFoundException(final String message)
	{
		super(message);
	}
}
