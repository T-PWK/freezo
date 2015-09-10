package org.freezo.admin.controller.api;

import org.freezo.domain.PostReporitory;
import org.freezo.domain.UserRepository;
import org.freezo.domain.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("admin")
@RestController("adminApiController")
@RequestMapping("${freezo.admin.urls.api}/admin")
@Secured("ROLE_ADMIN")
public class AdminController
{
	@Autowired
	private UserRepository users;

	@Autowired
	private WebsiteRepository websites;

	@Autowired
	private PostReporitory posts;

	@RequestMapping("statistics")
	public Statistics statistics()
	{
		final Statistics stats = new Statistics();
		stats.setUsers(users.count());
		stats.setWebsites(websites.count());
		stats.setPages(posts.count());

		return stats;
	}

	private static class Statistics
	{
		private long users;
		private long pages;
		private long websites;

		@SuppressWarnings("unused")
		public long getUsers()
		{
			return users;
		}

		public void setUsers(final long users)
		{
			this.users = users;
		}

		@SuppressWarnings("unused")
		public long getPages()
		{
			return pages;
		}

		public void setPages(final long pages)
		{
			this.pages = pages;
		}

		@SuppressWarnings("unused")
		public long getWebsites()
		{
			return websites;
		}

		public void setWebsites(final long websites)
		{
			this.websites = websites;
		}
	}
}
