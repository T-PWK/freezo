package org.freezo.website.service;

import java.util.Optional;

import org.freezo.domain.Redirect;
import org.freezo.domain.RedirectRepository;
import org.freezo.domain.Website;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("web")
@Service
public class JpaRedirectManager implements RedirectManager
{
	@Autowired
	private RedirectRepository repository;

	@Override
	public Optional<Redirect> findRedirect(final Website website, final String url)
	{
		return repository.findActive(website, url).stream().findFirst();
	}

}
