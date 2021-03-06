package org.freezo.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties({ "id", "version", "password", "user", "disabled", "locked" })
public class Account implements UserDetails
{
	private static final long serialVersionUID = 572162725149530080L;

	@Id
	private long id;

	@Column(length = 50, unique = true, nullable = false)
	private String username;

	@Column(length = 60)
	private String password;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastSuccessAuth;

	@Column(length = 45)
	private String lastSuccessAuthIp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastFailedAuth;

	@Column(length = 45)
	private String lastFailedAuthIp;

	private int failedAuthCounter;

	@Type(type = "yes_no")
	private boolean disabled;

	@Type(type = "yes_no")
	private boolean locked;

	@Temporal(TemporalType.TIMESTAMP)
	private Date accountExpirationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date credentialsExpirationDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "role")
	@BatchSize(size = 50)
	private final Set<String> authorities = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private User user;

	@Version
	private long version;

	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		return builder.append("id", id).append("username", username).append("version", version).toString();
	}

	public long getId()
	{
		return id;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public Date getLastSuccessAuth()
	{
		return lastSuccessAuth;
	}

	public void setLastSuccessAuth(final Date lastSuccessAuth)
	{
		this.lastSuccessAuth = lastSuccessAuth;
	}

	public String getLastSuccessAuthIp()
	{
		return lastSuccessAuthIp;
	}

	public void setLastSuccessAuthIp(final String lastSuccessAuthIp)
	{
		this.lastSuccessAuthIp = lastSuccessAuthIp;
	}

	public Date getLastFailedAuth()
	{
		return lastFailedAuth;
	}

	public void setLastFailedAuth(final Date lastFailedAuth)
	{
		this.lastFailedAuth = lastFailedAuth;
	}

	public String getLastFailedAuthIp()
	{
		return lastFailedAuthIp;
	}

	public void setLastFailedAuthIp(final String lastFailedAuthIp)
	{
		this.lastFailedAuthIp = lastFailedAuthIp;
	}

	public int getFailedAuthCounter()
	{
		return failedAuthCounter;
	}

	public void setFailedAuthCounter(final int failedAuthCounter)
	{
		this.failedAuthCounter = failedAuthCounter;
	}

	@Override
	public boolean isEnabled()
	{
		return !disabled;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(final boolean disabled)
	{
		this.disabled = disabled;
	}

	@Override
	@JsonProperty("nonLocked")
	public boolean isAccountNonLocked()
	{
		return !locked;
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(final boolean locked)
	{
		this.locked = locked;
	}

	@Override
	@JsonProperty("nonExpired")
	public boolean isAccountNonExpired()
	{
		return accountExpirationDate == null || accountExpirationDate.after(new Date());
	}

	@JsonProperty("expirationDate")
	public Date getAccountExpirationDate()
	{
		return accountExpirationDate;
	}

	public void setAccountExpirationDate(final Date accountExpirationDate)
	{
		this.accountExpirationDate = accountExpirationDate;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return credentialsExpirationDate == null || credentialsExpirationDate.after(new Date());
	}

	public Date getCredentialsExpirationDate()
	{
		return credentialsExpirationDate;
	}

	public void setCredentialsExpirationDate(final Date credentialsExpirationDate)
	{
		this.credentialsExpirationDate = credentialsExpirationDate;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()]));
	}

	@JsonProperty("authorities")
	public Collection<String> getAuthoritiesAsStrings()
	{
		return authorities;
	}

	public void addAuthorities(final String... authorities)
	{
		this.authorities.addAll(Arrays.asList(authorities));
	}

	public void addAuthorities(final GrantedAuthority... authorities)
	{
		this.authorities.addAll(AuthorityUtils.authorityListToSet(Arrays.asList(authorities)));
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	public long getVersion()
	{
		return version;
	}
}
