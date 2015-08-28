package org.freezo.core.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Website
{
	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String description;
	private Status status = Status.ENABLED;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleteRequestedAt;

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
		return getStatus() == Status.ENABLED;
	}

	public boolean isToDelete()
	{
		return getStatus() == Status.PENDING_REMOVE;
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

	public Date getDeleteRequestedAt()
	{
		return deleteRequestedAt;
	}

	public void setDeleteRequestedAt(final Date deleteRequestedAt)
	{
		this.deleteRequestedAt = deleteRequestedAt;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(final Status status)
	{
		this.status = status;
	}

	public static enum Status
	{
		ENABLED,
		DISABLED,
		PENDING_REMOVE
	}

}