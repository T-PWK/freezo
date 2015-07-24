package org.freezo.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "version", "username", "password", "accountNonExpired", "accountNonLocked",
		"credentialsNonExpired", "enabled", "authorities" })
public class User implements UserDetails
{
	private static final long serialVersionUID = -194182420823397720L;

	@Id
	@GeneratedValue
	private long id;

	private String firstName;
	private String lastName;
	private String email;
	private String location;
	private String website;
	private String picture;
	private String bio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final Account account;

	@Version
	private long version;

	public User()
	{
		account = new Account();
		getAccount().setUser(this);
	}

	public long getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(final String location)
	{
		this.location = location;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(final String website)
	{
		this.website = website;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(final String picture)
	{
		this.picture = picture;
	}

	public String getBio()
	{
		return bio;
	}

	public void setBio(final String bio)
	{
		this.bio = bio;
	}

	public Account getAccount()
	{
		return account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return getAccount().getAuthorities();
	}

	@Override
	public String getPassword()
	{
		return getAccount().getPassword();
	}

	@Override
	public String getUsername()
	{
		return getAccount().getUsername();
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return getAccount().isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return getAccount().isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return getAccount().isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled()
	{
		return getAccount().isEnabled();
	}

}
