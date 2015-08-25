package org.freezo.web.service;

import java.util.Optional;

import org.freezo.domain.Redirect;
import org.freezo.domain.Website;

public interface RedirectManager
{
	Optional<Redirect> findRedirect(Website website, String url);
}
