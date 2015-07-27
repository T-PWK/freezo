package org.freezo.admin.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class UserForm
{
	@NotNull
	@Length(max = 50, min = 5)
	private String username;

	@NotNull
	@Length(min = 5, max = 100)
	private String password;

	@NotNull
	@Length(min = 5, max = 100)
	private String confirm;

	@NotNull
	@Length(min = 2, max = 50)
	private String firstName;

	private String lastName;

	@Email
	private String email;

	private String location;
	private String website;

	private final Set<String> roles = new HashSet<String>();


	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("username", username).append("password", password).append("confirm", confirm)
				.append("firstName", firstName).append("lastName", lastName).append("email", email)
				.append("location", location).append("website", website).append("roles", roles);
		return builder.toString();
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getConfirm()
	{
		return confirm;
	}

	public void setConfirm(final String confirm)
	{
		this.confirm = confirm;
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

	public Set<String> getRoles()
	{
		return roles;
	}

	public void setRoles(final Set<String> roles)
	{
		this.roles.clear();
		this.roles.addAll(roles);
	}

}
