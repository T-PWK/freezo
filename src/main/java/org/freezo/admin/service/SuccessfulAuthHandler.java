package org.freezo.admin.service;

import java.util.Date;

import org.freezo.core.domain.Account;
import org.freezo.core.domain.AccountRepository;
import org.freezo.core.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SuccessfulAuthHandler implements ApplicationListener<AuthenticationSuccessEvent>
{
	@Autowired
	private AccountRepository repository;

	@Override
	@Transactional
	public void onApplicationEvent(final AuthenticationSuccessEvent event)
	{
		final Account account = ((User) event.getAuthentication().getPrincipal()).getAccount();

		account.setFailedAuthCounter(0);
		account.setLastSuccessAuth(new Date());
		account.setLastSuccessAuthIp(currentRequestRemoteAddr());

		repository.save(account);
	}

	private String currentRequestRemoteAddr()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest().getRemoteAddr();
	}
}
