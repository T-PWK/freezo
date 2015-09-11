package org.freezo.admin.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${freezo.admin.urls.app}")
@Profile("admin")
public class AdminController
{
	@RequestMapping("")
	public String cms()
	{
		return "admin/cms";
	}

	@RequestMapping("partial/{name}")
	public String partial(@PathVariable("name") final String name)
	{
		return "admin/partial/" + name;
	}
}
