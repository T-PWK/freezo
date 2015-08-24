package org.freezo.web.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("web")
@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home()
	{
		return "index";
	}

	@RequestMapping("/search/label/{label}")
	public String label()
	{
		return "index";
	}

	@RequestMapping("/page/{page}")
	public String pagination()
	{
		return "index";
	}

	@RequestMapping("/{slug}")
	public String page()
	{
		return "index";
	}

}
