package org.freezo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "id", "website" })
public class Host
{
	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true)
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

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != getClass()) return false;

		final Host host = (Host) obj;
		return new EqualsBuilder().append(name, host.name).isEquals();
	}

	public long getId()
	{
		return id;
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
