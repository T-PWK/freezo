package org.freezo.core.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Redirect
{
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(optional = false)
	private Website website;

	private String fromUrl;
	private String toUrl;
	private Type type;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endAt;

	@Version
	private long version;

	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE);
		return builder.append("id", id).append("type", type)
				.append("from", fromUrl).append("to", toUrl)
				.append("start", startAt).append("end", endAt).toString();
	}

	public long getId()
	{
		return id;
	}

	public long getVersion()
	{
		return version;
	}

	public String getFromUrl()
	{
		return fromUrl;
	}

	public void setFromUrl(final String fromUrl)
	{
		this.fromUrl = fromUrl;
	}

	public String getToUrl()
	{
		return toUrl;
	}

	public void setToUrl(final String toUrl)
	{
		this.toUrl = toUrl;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(final Type type)
	{
		this.type = type;
	}

	public Date getStartAt()
	{
		return startAt;
	}

	public void setStartAt(final Date startAt)
	{
		this.startAt = startAt;
	}

	public Date getEndAt()
	{
		return endAt;
	}

	public void setEndAt(final Date endAt)
	{
		this.endAt = endAt;
	}

	public Website getWebsite()
	{
		return website;
	}

	public void setWebsite(final Website website)
	{
		this.website = website;
	}

	public static enum Type
	{
		PERSISTANT,
		TEMPORARY,
		INTERNAL
	}

}
