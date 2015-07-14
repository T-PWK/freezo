package org.freezo.admin.config;

import org.freezo.admin.service.AdminUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(prefix = "freezo", name = "enableAdmin", havingValue = "true")
public class AdminConfiguration
{
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService()
	{
		return new AdminUserDetailsService();
	}

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder(
			@Value("${freezo.security.authentication.password.strength:10}") final int strength)
	{
		return new BCryptPasswordEncoder(strength);
	}

	@Configuration
	@ConditionalOnProperty(prefix = "freezo", name = "enableAdmin", havingValue = "true")
	protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter
	{
		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private PasswordEncoder encoder;

		@Override
		public void init(final AuthenticationManagerBuilder auth) throws Exception
		{
			auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
		}
	}
}
