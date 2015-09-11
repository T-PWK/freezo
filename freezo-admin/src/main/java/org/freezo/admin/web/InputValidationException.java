package org.freezo.admin.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InputValidationException extends RuntimeException
{
	private static final long serialVersionUID = -8643674983268381338L;

	public InputValidationException()
	{
	}

	public InputValidationException(final String message)
	{
		super(message);
	}

}
