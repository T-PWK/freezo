package org.freezo.admin.domain;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class UserForm
{
	@NotNull
	@Length(max = 50, min = 3)
	private String username;

	@NotNull
	@Length(min = 5, max = 100)
	private String password;

	@NotNull
	@Length(min = 5, max = 100)
	private String confirm;

	@NotNull
	@Length(min = 2, max = 255)
	private String firstName;

	@Length(max = 255)
	private String lastName;

	@Email
	@Length(max = 255)
	private String email;

	@Length(max = 255)
	private String location;

	@Length(max = 255)
	private String website;

	@Length(max = 255)
	private String bio;

	private final Set<Roles> roles = new HashSet<Roles>();

	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this);
		final int passwordLength = StringUtils.length(password);

		return builder.append("username", username)
				.append("password", String.format("* (%d characters long)", passwordLength))
				.append("firstName", firstName).append("lastName", lastName).append("email", email)
				.append("location", location).append("website", website).append("bio", bio)
				.append("roles", roles).toString();
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

	public String[] getRolesAsString()
	{
		return roles.stream().map(role -> role.name()).toArray(String[]::new);
	}

	public Set<Roles> getRoles()
	{
		return roles;
	}

	public void setRoles(final Set<Roles> roles)
	{
		this.roles.clear();
		this.roles.addAll(roles);
	}

	public String getBio()
	{
		return bio;
	}

	public void setBio(final String bio)
	{
		this.bio = bio;
	}

}
