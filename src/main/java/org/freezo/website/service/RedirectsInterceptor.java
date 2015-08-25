package org.freezo.website.service;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromRequest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.freezo.domain.Redirect;
import org.freezo.domain.Website;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Performs redirect or internal forward if there is a redirect item configured for a given request URI.
 * <p/>
 * This component handles three types of redirects:
 * <ul>
 * <li>Internal - request is directed to a different route using {@link javax.servlet.RequestDispatcher}</li>
 * <li>HTTP 301 - <strong>Moved permanently</strong> - system sends 301 response to a client with a new location</li>
 * <li>HTTP 302 - <strong>Moved temporarily</strong> - system sends 302 response to a client with a new path</li>
 * </ul>
 *
 * @author T-PWK
 */
@Profile("web")
@Component
public class RedirectsInterceptor extends HandlerInterceptorAdapter
{
	private static final Logger LOG = LoggerFactory.getLogger(RedirectsInterceptor.class);

	@Autowired
	private RedirectManager manager;

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if there is no valid redirect configured for the current request; otherwise {@code false}.
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{
		final Optional<Redirect> redirect = findRedirectForRequest(request);

		if (!redirect.isPresent())
		{
			return true;
		}

		LOG.info("Redirect found: {}", redirect.get());

		switch (redirect.get().getType())
		{
		case INTERNAL:
			request.getRequestDispatcher(redirect.get().getToUrl()).forward(request, response);
			break;
		case TEMPORARY:
			response.sendRedirect(redirect.get().getToUrl());
			break;
		case PERSISTANT:
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("Location", fromRequest(request).replacePath(redirect.get().getToUrl()).toUriString());
			break;
		default:
		}

		return false;
	}

	/**
	 * Looks up a redirect based on the given request URI
	 *
	 * @param request an HTTP request to get URI from
	 * @return an redirect item found for the given request or {@code null}
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	private Optional<Redirect> findRedirectForRequest(final HttpServletRequest request)
	{
		return manager.findRedirect((Website) request.getAttribute("FREEZO_WEBSITE"), request.getRequestURI());
	}

}
