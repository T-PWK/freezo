package org.freezo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;

@Entity
public class Website
{
	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String description;

	@Type(type = "yes_no")
	private boolean enabled;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@ElementCollection
	private final Set<String> hosts = new HashSet<>();

	@Version
	private long version;

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("id", id).append("name", name).append("hosts", hosts).toString();
	}

	public long getId()
	{
		return id;
	}

	public long getVersion()
	{
		return version;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Set<String> getHosts()
	{
		return hosts;
	}

	public void setHosts(final Set<String> hosts)
	{
		this.hosts.clear();
		this.hosts.addAll(hosts);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(final Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(final Date createdAt)
	{
		this.createdAt = createdAt;
	}

}