package org.freezo.website.service;

import java.util.Optional;

import org.freezo.core.domain.Redirect;
import org.freezo.core.domain.Website;

public interface RedirectManager
{
	Optional<Redirect> findRedirect(Website website, String url);
}
