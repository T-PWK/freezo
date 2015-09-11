package org.freezo.admin.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class WebUtil
{
	private WebUtil()
	{
	}

	public static Pageable updatePageable(final Pageable source, final int size)
	{
		return new PageRequest(source.getPageNumber(), size, source.getSort());
	}
}
