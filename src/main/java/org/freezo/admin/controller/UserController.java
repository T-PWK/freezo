package org.freezo.admin.controller;

import javax.transaction.Transactional;

import org.freezo.admin.bind.CaseInsentiveEnumEditor;
import org.freezo.domain.Account;
import org.freezo.domain.AccountRepository;
import org.freezo.domain.User;
import org.freezo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/api/v1/users")
@Secured("ROLE_ADMIN")
public class UserController
{
	@Autowired
	private UserRepository repository;

	@Autowired
	private AccountRepository accounts;

	/**
	 * Registers customer converters for incoming data
	 *
	 * @param binder web data binder
	 */
	@InitBinder
	public void init(final WebDataBinder binder)
	{
		binder.registerCustomEditor(UserFilterByType.class, new CaseInsentiveEnumEditor<>(UserFilterByType.class));
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<User> searchUsersByType(@PageableDefault(size = 10) final Pageable pageable,
			@RequestParam(value = "filter", required = false, defaultValue = "ALL") final UserFilterByType type)
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

	@RequestMapping(value = "/{user_id}", method = RequestMethod.PUT)
	@Transactional
	public void updateUser(@PathVariable("user_id") final Long id, @RequestBody final UserUpdateRequest request)
	{
		final Account account = lookupAccount(id);

		System.out.println(request);

		switch (request.getType())
		{
		case LOCK:
			lockAccount(account);
			break;
		case UNLOCK:
			unlockAccount(account);
			break;
		case DISABLE:
			disableAccount(account);
			break;
		case ENABLE:
			enableAccount(account);
			break;
		}
	}

	/**
	 * Enable the given account
	 *
	 * @param account account to be enabled
	 * @throws ResourceConflictException if the given account is enabled already
	 */
	private void enableAccount(final Account account)
	{
		if (!account.isDisabled())
		{
			throw new ResourceConflictException("Account is enabled already");
		}

		account.setDisabled(false);
	}

	/**
	 * Disable the given account
	 *
	 * @param account account to be disabled
	 * @throws ResourceConflictException if the given account is disabled already
	 */
	private void disableAccount(final Account account)
	{
		if (account.isDisabled())
		{
			throw new ResourceConflictException("Account is disabled already");
		}

		account.setDisabled(true);
	}

	/**
	 * Unlock the given account
	 *
	 * @param account account to be unlocked
	 * @throws ResourceConflictException if the given account is unlocked already
	 */
	private void unlockAccount(final Account account)
	{
		if (!account.isLocked())
		{
			throw new ResourceConflictException("Account is unlocked already");
		}

		account.setLocked(false);
	}

	/**
	 * Lock the given account
	 *
	 * @param account account to be locked
	 * @throws ResourceConflictException if the given account is locked already
	 */
	private void lockAccount(final Account account)
	{
		if (account.isLocked())
		{
			throw new ResourceConflictException("Account is locked already");
		}

		account.setLocked(true);
	}

	/**
	 * Looks up account by the given identifier.
	 *
	 * @param id account identifier
	 * @return account entity
	 * @throws ResourceNotFoundException if there is no account with the given identifier
	 */
	private Account lookupAccount(final Long id)
	{
		if (!accounts.exists(id))
		{
			throw new ResourceNotFoundException();
		}
		return accounts.findOne(id);
	}

	public static enum UserFilterByType
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
