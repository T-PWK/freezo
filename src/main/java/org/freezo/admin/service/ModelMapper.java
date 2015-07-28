package org.freezo.admin.service;

public interface ModelMapper
{
	<T> T from(final Object source, final Class<T> destinationType);
}
