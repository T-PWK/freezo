package org.freezo.admin.controller.api;

import org.freezo.core.domain.Host;
import org.freezo.core.domain.HostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Profile("admin")
@RestController
@RequestMapping("${freezo.admin.urls.api}/hosts")
@Secured("ROLE_ADMIN")
public class HostsController
{
	@Autowired
	private HostsRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	public Page<Host> hosts(final Pageable pageable)
	{
		return repository.findAll(pageable);
	}
}
