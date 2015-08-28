package org.freezo.core.domain;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class Post
{
	@Id
	@GeneratedValue
	private long id;

	private String title;
	private String description;
	private String slug;

	@ManyToOne(optional = false)
	private Website website;

	@ElementCollection
	private final Set<String> labels = new TreeSet<>();

	@Version
	private long version;

	public long getId()
	{
		return id;
	}

	public long getVersion()
	{
		return version;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getSlug()
	{
		return slug;
	}

	public void setSlug(final String slug)
	{
		this.slug = slug;
	}

	public void setLabels(final Set<String> labels)
	{
		this.labels.clear();
		this.labels.addAll(labels);
	}

	public Set<String> getLabels()
	{
		return labels;
	}

	public Website getWebsite()
	{
		return website;
	}

	public void setWebsite(Website website)
	{
		this.website = website;
	}

}
