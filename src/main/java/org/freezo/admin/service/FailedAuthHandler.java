package org.freezo.admin.service;

import java.util.Date;

import org.freezo.domain.Account;
import org.freezo.domain.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FailedAuthHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>
{
	private static final Logger LOG = LoggerFactory.getLogger(FailedAuthHandler.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AccountRepository repository;

	@Value("${freezo.security.authentiation.maxBadCredentialsToLockAccount:-1}")
	private int badCredentialsToLockAccount;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event)
	{
		LOG.info("Updating details of ACCOUNT:[{}] upon bad credentials", event.getAuthentication().getPrincipal());

		repository.findByUsername((String) event.getAuthentication().getPrincipal())
				.ifPresent(account -> updateAccount(account));
	}

	/**
	 * Updates account with failed login details such as: last failed login date and time, users IP address, increases
	 * failed logins counter etc.
	 *
	 * @param account current account
	 * @see #checkIfAccountLocked(Account)
	 */
	private void updateAccount(final Account account)
	{
		account.setLastFailedAuth(new Date());
		account.setLastFailedAuthIp(currentRequestRemoteAddr());
		account.setFailedAuthCounter(account.getFailedAuthCounter() + 1);

		checkIfAccountLocked(account);

		repository.save(account);
	}

	/**
	 * Locks the given account if the maximum number of login failures (bad credentials) has been reached.
	 * <p>
	 * This feature can be disabled by setting {@code freezo.security.authentiation.maxBadCredentialsToLockAccount}
	 * system property to value lower than 1;
	 * <p>
	 * Note that account will be locked after the first failed login attempt is
	 * {@code freezo.security.authentiation.maxBadCredentialsToLockAccount} is set to 1.
	 *
	 * @param account current account
	 * @see UserDetails#isAccountNonLocked()
	 */
	private void checkIfAccountLocked(final Account account)
	{
		if (badCredentialsToLockAccount > 0 && account.getFailedAuthCounter() >= badCredentialsToLockAccount)
		{
			account.setLocked(true);
		}
	}

	private String currentRequestRemoteAddr()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest().getRemoteAddr();
	}

}
