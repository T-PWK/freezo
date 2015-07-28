package org.freezo.admin.controller;

import javax.validation.Valid;

import org.freezo.admin.bind.CaseInsentiveEnumEditor;
import org.freezo.admin.domain.UserForm;
import org.freezo.admin.service.ModelMapper;
import org.freezo.domain.User;
import org.freezo.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
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

/**
 * Users API:
 * <ul>
 * <li>GET /api/v1/users - fetches all users. Available query string parameters
 * <ul>
 * <li>filter: <strong>enabled</strong> - fetch enabled users</li>
 * <li>filter: <strong>disabled</strong> - fetch disabled users</li>
 * <li>filter: <strong>locked</strong> - fetch locked users</li>
 * <li>filter: <strong>nonlocked</strong> - fetch non-locked users</li>
 * <li>filter: <strong>expired</strong> - fetch expired users</li>
 * <li>filter: <strong>nonexpired</strong> - fetch non-expired users</li>
 * </ul>
 * </li>
 * <li>GET /api/v1/users/{user_id} - finds a user by an identifier</li>
 * <li>GET /api/v1/users/username/{user_id} - finds a user by a username</li>
 * <li>POST /api/v1/users - creates user</li>
 * </ul>
 *
 * @author tomasz.pawlak
 *
 */
@Profile("admin")
@RestController
@RequestMapping("/api/v1/users")
@Secured("ROLE_ADMIN")
public class UsersController
{
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	/**
	 * Registers customer converters for incoming data
	 *
	 * @param binder
	 *            web data binder
	 */
	@InitBinder
	public void init(final WebDataBinder binder)
	{
		binder.registerCustomEditor(UserFilterByType.class, new CaseInsentiveEnumEditor<>(UserFilterByType.class));
		binder.registerCustomEditor(UpdateAction.class, new CaseInsentiveEnumEditor<>(UpdateAction.class));
	}

	@RequestMapping("/username/{username}")
	@Transactional(readOnly = true)
	public User findByIdentifier(@PathVariable final String username)
	{
		return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException());
	}

	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = true)
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

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public User createUser(@RequestBody @Valid final UserForm form, final BindingResult result)
	{
		verifyInput(form, result);

		final User user = mapper.from(form, User.class);

		user.getAccount().setUsername(form.getUsername());
		user.getAccount().setPassword(encoder.encode(form.getPassword()));
		user.getAccount().addAuthorities(form.getRolesAsString());

		return repository.save(user);
	}

	private void verifyInput(final UserForm form, final BindingResult result)
	{
		if (result.hasErrors())
		{
			throw new InputValidationException();
		}

		if (repository.findByUsername(form.getUsername()).isPresent())
		{
			throw new InputValidationException(
					String.format("Username [%s] already exists", form.getUsername()));
		}
	}

	@RequestMapping(value = "/{user_id}/{action}", method = RequestMethod.PATCH)
	@Transactional
	public void updateUserAvailability(@PathVariable("user_id") final Long id,
			@PathVariable("action") final UpdateAction action, @AuthenticationPrincipal final User currentUser)
	{
		LOG.info("User update :: ACTION:[{}], TARGET:[id:{}], CURRENT:{}", action, id, currentUser);

		final User user = lookupUser(id);

		switch (action)
		{
		case LOCK:
			lockUser(user);
			break;
		case UNLOCK:
			unlockUser(user);
			break;
		case DISABLE:
			disableUser(user);
			break;
		case ENABLE:
			enableUser(user);
			break;
		case DELETE:
			deleteUser(user, currentUser);
			break;
		}
	}

	/**
	 * Delete the given user profile
	 *
	 * @param user
	 *            user profile to be deleted
	 * @param currentUser
	 *            currently logged in user
	 * @throws ResourceConflictException
	 *             if the user to be deleted is current user
	 */
	private void deleteUser(final User user, final User currentUser)
	{

		if (user.getId() == currentUser.getId())
		{
			throw new ResourceConflictException(
					"User can't delete it's own account");
		}

		repository.delete(user);
	}

	/**
	 * Enable the given user profile
	 *
	 * @param user
	 *            user profile to be enabled
	 * @throws ResourceConflictException
	 *             if the given user profile is enabled already
	 */
	private void enableUser(final User user)
	{
		if (!user.getAccount().isDisabled())
		{
			throw new ResourceConflictException("User profile is enabled already");
		}

		user.getAccount().setDisabled(false);
	}

	/**
	 * Disable the given user profile
	 *
	 * @param user
	 *            user profile to be disabled
	 * @throws ResourceConflictException
	 *             if the given user profile is disabled already
	 */
	private void disableUser(final User user)
	{
		if (user.getAccount().isDisabled())
		{
			throw new ResourceConflictException("User profile is disabled already");
		}

		user.getAccount().setDisabled(true);
	}

	/**
	 * Unlock the given user profile
	 *
	 * @param user
	 *            user profile to be unlocked
	 * @throws ResourceConflictException
	 *             if the given user profile is unlocked already
	 */
	private void unlockUser(final User user)
	{
		if (!user.getAccount().isLocked())
		{
			throw new ResourceConflictException("User profile is unlocked already");
		}

		user.getAccount().setLocked(false);
	}

	/**
	 * Lock the given user profile
	 *
	 * @param user
	 *            user profile to be locked
	 * @throws ResourceConflictException
	 *             if the given user profile is locked already
	 */
	private void lockUser(final User user)
	{
		if (user.getAccount().isLocked())
		{
			throw new ResourceConflictException("User profile is locked already");
		}

		user.getAccount().setLocked(true);
	}

	/**
	 * Looks up user profile by the given identifier.
	 *
	 * @param id
	 *            user profile identifier
	 * @return user profile entity
	 * @throws ResourceNotFoundException
	 *             if there is no user profile with the given identifier
	 */
	private User lookupUser(final Long id)
	{
		if (!repository.exists(id))
		{
			throw new ResourceNotFoundException();
		}
		return repository.findOne(id);
	}

	public static enum UpdateAction
	{
		LOCK,
		UNLOCK,
		DISABLE,
		ENABLE,
		DELETE
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
