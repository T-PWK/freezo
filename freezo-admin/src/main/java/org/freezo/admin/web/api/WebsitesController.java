package org.freezo.admin.web.api;

import static org.freezo.admin.web.WebUtil.updatePageable;

import java.util.Date;

import javax.validation.Valid;

import org.freezo.admin.bind.CaseInsentiveEnumEditor;
import org.freezo.admin.domain.WebsiteForm;
import org.freezo.admin.service.ModelMapper;
import org.freezo.admin.web.ResourceConflictException;
import org.freezo.domain.Website;
import org.freezo.domain.Website.Status;
import org.freezo.domain.WebsiteRepository;
import org.freezo.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("admin")
@RestController
@RequestMapping("${freezo.admin.urls.api}/websites")
@Secured("ROLE_ADMIN")
public class WebsitesController
{
	private static final Logger LOG = LoggerFactory.getLogger(WebsitesController.class);

	@Autowired
	private WebsiteRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Value("${freezo.admin.domain.website.items-per-page:20}")
	private int itemsPerPage;

	/**
	 * Registers customer converters for incoming data
	 *
	 * @param binder web data binder
	 */
	@InitBinder
	public void init(final WebDataBinder binder)
	{
		binder.registerCustomEditor(Website.Status.class, new CaseInsentiveEnumEditor<>(Website.Status.class));
	}

	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Page<Website> websites(final Pageable pageable) throws InterruptedException
	{
		final Pageable pageInfo = updatePageable(pageable, itemsPerPage);

		LOG.debug("Websites listing: {}", pageInfo);

		return repository.findAll(pageInfo);
	}

	@RequestMapping(method = RequestMethod.GET, params = "status")
	@Transactional(readOnly = true)
	public Page<Website> websitesByStatus(@RequestParam("status") final Website.Status status,
			@PageableDefault(size = 10) final Pageable pageable) throws InterruptedException
	{
		final Pageable pageInfo = updatePageable(pageable, itemsPerPage);

		LOG.debug("Websites listing filtered by status: {}, {} ", status, pageInfo);

		return repository.findByStatus(status, pageInfo);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Website website(@PathVariable("id") final long id)
	{
		LOG.debug("Requesting website:[{}] details", id);

		return findWebsite(id);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	@Transactional
	public Website update(@PathVariable("id") final long id,
			@RequestBody @Valid final WebsiteForm form, final BindingResult result)
	{
		LOG.debug("Updating website:[{}] with content: {} (errors:{})", id, form, result.hasErrors());

		final Website website = findWebsite(id);

		mapper.update(form, website);
		website.setUpdatedAt(new Date());

		return repository.save(website);
	}

	@RequestMapping(value = "{id}/disable", method = RequestMethod.PATCH)
	@Transactional
	public Website disable(@PathVariable("id") final long id)
	{
		LOG.debug("Disabling website:[{}]", id);

		final Website website = findWebsite(id);

		if (website.getStatus() != Status.ENABLED)
		{
			throw new ResourceConflictException("Invalid status");
		}

		website.setUpdatedAt(new Date());
		website.setStatus(Status.DISABLED);

		repository.save(website);

		return website;
	}

	@RequestMapping(value = "{id}/enable", method = RequestMethod.PATCH)
	@Transactional
	public Website enable(@PathVariable("id") final long id)
	{
		LOG.debug("Enabling website:[{}]", id);

		final Website website = findWebsite(id);

		if (website.getStatus() != Status.DISABLED)
		{
			throw new ResourceConflictException("Invalid status");
		}

		website.setUpdatedAt(new Date());
		website.setStatus(Status.ENABLED);

		repository.save(website);

		return website;
	}

	@RequestMapping(value = "{id}/restore", method = RequestMethod.PATCH)
	public Website restore(@PathVariable("id") final long id)
	{
		LOG.debug("Restoring website:[{}]", id);

		final Website website = findWebsite(id);

		if (website.getStatus() != Status.PENDING_REMOVE)
		{
			throw new ResourceConflictException("Invalid status");
		}

		website.setUpdatedAt(new Date());
		website.setStatus(Status.DISABLED);

		repository.save(website);

		return website;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@Transactional
	public void delete(@PathVariable("id") final long id)
	{
		LOG.debug("Deleting website:[{}]", id);

		final Website website = findWebsite(id);

		website.setUpdatedAt(new Date());
		website.setDeleteRequestedAt(new Date());
		website.setStatus(Status.PENDING_REMOVE);

		repository.save(website);
	}

	/**
	 * Checks if there is a website with the given identifier and returns found instance.
	 *
	 * @param id website identifier
	 * @throws ResourceNotFoundException if there is no website with the given identifier.
	 * @return website entity
	 */
	private Website findWebsite(final long id) throws ResourceNotFoundException
	{
		if (!repository.exists(id))
		{
			throw new ResourceNotFoundException(String.format("Website ID:%s does not exist", id));
		}

		final Website website = repository.findOne(id);

		return website;
	}
}
