package org.freezo.admin.service;

public class ModelMapperImpl implements ModelMapper
{
	private final org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

	@Override
	public <T> T from(final Object source, final Class<T> destinationType)
	{
		return mapper.map(source, destinationType);
	}

}
