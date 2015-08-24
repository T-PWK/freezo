package org.freezo.web.view;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class WebsiteViewResolver implements ViewResolver, Ordered
{
	private static final Logger LOG = LoggerFactory.getLogger(WebsiteViewResolver.class);

	private final ViewResolver delegate;
	private final int order;

	public WebsiteViewResolver(final ViewResolver delegate, final int order)
	{
		this.delegate = delegate;
		this.order = order;
	}

	@Override
	public int getOrder()
	{
		return order;
	}

	@Override
	public View resolveViewName(final String name, final Locale locale) throws Exception
	{
		LOG.info("View: [{}][{}]", name, locale);

		if (name.startsWith("admin"))
		{
			return delegate.resolveViewName(name, locale);
		}

		return delegate.resolveViewName("web/default/" + name, locale);
	}
}
