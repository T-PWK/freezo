package org.freezo.admin.controller;

import org.freezo.domain.User;
import org.freezo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
