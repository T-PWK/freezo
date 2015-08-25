package org.freezo.website.service;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.freezo.domain.Website;
import org.freezo.domain.WebsiteRepository;
import org.freezo.web.ResourceNotFoundException;
import org.freezo.website.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Profile("web")
@Component
public class WebsiteListener extends HandlerInterceptorAdapter
{
	private static final Logger LOG = LoggerFactory.getLogger(WebsiteListener.class);

	@Autowired
	private WebsiteRepository repository;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{
		final String url = request.getRequestURL().toString();
		final String host = new URL(url).getHost();
		final Website website = repository.findByHostsIn(host).orElseThrow(
				() -> new ResourceNotFoundException(String.format("No website found for host name: %s", host)));

		request.setAttribute(Constants.WEBSITE_ATTRIBUTE_NAME, website);

		LOG.info("Website: {} detected for URL:[{}]", website, url);

		return true;
	}
}
