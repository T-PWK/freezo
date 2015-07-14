package org.freezo.admin.service;

import java.util.Date;

import org.freezo.domain.Account;
import org.freezo.domain.User;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SuccessfulAuthHandler implements ApplicationListener<AuthenticationSuccessEvent>
{
	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event)
	{
		final Account account = ((User) event.getAuthentication().getPrincipal()).getAccount();

		account.setFailedAuthCounter(0);
		account.setLastSuccessAuth(new Date());
		account.setLastSuccessAuthIp(currentRequestRemoteAddr());
	}

	private String currentRequestRemoteAddr()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest().getRemoteAddr();
	}
}
