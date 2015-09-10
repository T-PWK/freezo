package org.freezo.admin.service;

import org.freezo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class AdminUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserRepository repository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}

}
