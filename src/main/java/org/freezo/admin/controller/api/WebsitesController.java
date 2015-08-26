package org.freezo.admin.controller.api;

import java.util.Date;

import org.freezo.domain.Website;
import org.freezo.domain.WebsiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Profile("admin")
@RestController
@RequestMapping("/api/v1/websites")
@Secured("ROLE_ADMIN")
public class WebsitesController
{
	private static final Logger LOG = LoggerFactory.getLogger(WebsitesController.class);

	@Autowired
	private WebsiteRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	public Page<Website> websites(final Pageable pageable) throws InterruptedException
	{
		return repository.findAll(pageable);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public Website website(@PathVariable("id") final long id) throws InterruptedException
	{
		return repository.findOne(id);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public Website update(@RequestBody final Website website, @PathVariable("id") final long id)
			throws InterruptedException
	{
		LOG.info("Update of website ... : {}", website);

		website.setUpdatedAt(new Date());

		return repository.save(website);
	}

}
