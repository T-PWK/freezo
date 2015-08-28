package org.freezo.admin.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;

public class WebsiteForm
{
	@Length(min = 1, max = 100)
	private String name;

	@Length(min = 1, max = 255)
	private String description;

	private boolean enabled;
	private final Set<String> hosts = new HashSet<>();

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("name", name).append("description", description)
				.append("enabled", enabled).append("hosts", hosts).toString();
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

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
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
}
