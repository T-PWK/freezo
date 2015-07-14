package org.freezo.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class User
{
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

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private final Account account;

	@Version
	private long version;

	public User()
	{
		account = new Account();
		account.setUser(this);
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

}
