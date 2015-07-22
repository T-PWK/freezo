package org.freezo.admin.config;

import org.freezo.admin.service.AdminUserDetailsService;
import org.freezo.admin.service.FailedAuthHandler;
import org.freezo.admin.service.SuccessfulAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Profile("admin")
public class AdminConfiguration
{
	@Bean
	@ConditionalOnProperty(prefix = "freezo.security.authentication.account", name = "updateOnSuccess", havingValue = "true", matchIfMissing = true)
	public ApplicationListener<AuthenticationSuccessEvent> successfulAuthHandler()
	{
		return new SuccessfulAuthHandler();
	}

	@Bean
	@ConditionalOnProperty(prefix = "freezo.security.authentication.account", name = "updateOnFailure", havingValue = "true", matchIfMissing = true)
	public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failedAuthHandler()
	{
		return new FailedAuthHandler();
	}

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
	protected static class ViewsConfiguration extends WebMvcConfigurerAdapter
	{
		@Override
		public void addViewControllers(final ViewControllerRegistry registry)
		{
			registry.addViewController("/admin/login").setViewName("admin/login");
			registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		}
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@Profile("admin")
	protected static class SecurityConfig extends WebSecurityConfigurerAdapter
	{
		@Override
		protected void configure(final HttpSecurity http) throws Exception
		{
			http.antMatcher("/admin/**").authorizeRequests().anyRequest().authenticated()
					.and().formLogin().loginPage("/admin/login").defaultSuccessUrl("/admin", true).permitAll()
					.and().logout().logoutUrl("/admin/logout").permitAll();
		}
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 1)
	public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter
	{
		@Override
		protected void configure(final HttpSecurity http) throws Exception
		{
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasAnyRole("ADMIN", "API");
		}
	}

	@Configuration
	@Profile("admin")
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
