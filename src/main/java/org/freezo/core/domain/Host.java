package org.freezo.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Host
{
	@Id
	private String name;

	@ManyToOne
	private Website website;

	public Host()
	{
	}

	public Host(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String id)
	{
		this.name = id;
	}

	public Website getWebsite()
	{
		return website;
	}

	public void setWebsite(final Website website)
	{
		this.website = website;
	}

}
