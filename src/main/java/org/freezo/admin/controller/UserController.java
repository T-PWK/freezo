package org.freezo.admin.controller;

import javax.transaction.Transactional;

import org.freezo.domain.Account;
import org.freezo.domain.AccountRepository;
import org.freezo.domain.User;
import org.freezo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Profile("admin")
public class UserController
{
	@Autowired
	private UserRepository repository;

	@Autowired
	private AccountRepository accounts;

	@RequestMapping(method = RequestMethod.GET)
	public Page<User> usersByType(@PageableDefault(size = 10) final Pageable pageable,
			@RequestParam(value = "filter", required = false, defaultValue = "ALL") final UserType type)
	{
		switch (type)
		{
		case ENABLED:
			return repository.findByAccountDisabled(false, pageable);
		case DISABLED:
			return repository.findByAccountDisabled(true, pageable);
		case LOCKED:
			return repository.findByAccountLocked(true, pageable);
		case NONLOCKED:
			return repository.findByAccountLocked(false, pageable);
		case EXPIRED:
			return repository.findByAccountExpired(pageable);
		case NONEXPIRED:
			return repository.findByAccountNonExpired(pageable);
		case CREDENTIALS_EXPIRED:
			return repository.findByCredentialsExpired(pageable);
		case CREDENTIALS_NONEXPIRED:
			return repository.findByCredentialsNonExpired(pageable);
		default:
			return repository.findAll(pageable);
		}
	}

	@RequestMapping(value = "/{userId}/lock", method = RequestMethod.PUT)
	@Transactional
	public void lockAccount(@PathVariable("userId") final Long id)
	{
		lookupAccount(id).setLocked(true);
	}

	@RequestMapping(value = "/{userId}/unlock", method = RequestMethod.PUT)
	@Transactional
	public void unlockAccount(@PathVariable("userId") final Long id)
	{
		lookupAccount(id).setLocked(false);
	}

	@RequestMapping(value = "/{userId}/enable", method = RequestMethod.PUT)
	@Transactional
	public void enableAccount(@PathVariable("userId") final Long id)
	{
		lookupAccount(id).setDisabled(false);
	}

	@RequestMapping(value = "/{userId}/disable", method = RequestMethod.PUT)
	@Transactional
	public void disableAccount(@PathVariable("userId") final Long id)
	{
		lookupAccount(id).setDisabled(true);
	}

	private Account lookupAccount(final Long id)
	{
		if (!accounts.exists(id)) { throw new ResourceNotFoundException(); }
		return accounts.findOne(id);
	}

	public static enum ActionType
	{
		enable,
		disable,
		lock,
		unlock
	}

	public static enum UserType
	{
		ALL,
		ENABLED,
		DISABLED,
		LOCKED,
		NONLOCKED,
		EXPIRED,
		NONEXPIRED,
		CREDENTIALS_EXPIRED,
		CREDENTIALS_NONEXPIRED
	}
}
