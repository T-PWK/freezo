package org.freezo.admin.controller;

import java.util.UUID;

import org.freezo.domain.User;
import org.freezo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@Profile("admin")
public class CreateUserCtrl
{
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder encoder;

	@RequestMapping(method = RequestMethod.GET, params = { "username", "password" }, value="create")
	public User create(@RequestParam(required = true, value = "username") final String usernaem,
			@RequestParam(required = true, value = "password") final String password)
	{
		final User user = new User();
		user.setEmail(String.format("%s@gmail.com", UUID.randomUUID()));

		user.getAccount().setUsername(usernaem);
		user.getAccount().setPassword(encoder.encode(password));
		user.getAccount().addAuthorities("ROLE_ADMIN");
		user.setFirstName("Dave");
		user.setLastName("Tester");

		return repository.save(user);
	}
}
