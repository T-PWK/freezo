package org.freezo.admin.web.api;

import java.util.Set;

import org.freezo.domain.Host;
import org.freezo.domain.HostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(method = RequestMethod.GET, params = { "hosts" })
	public Set<Host> findByName(@RequestParam("hosts") final Set<String> hosts)
	{
		return repository.findByNameIn(hosts);
	}
}
