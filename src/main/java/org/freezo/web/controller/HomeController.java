package org.freezo.web.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
public class HomeController
{
	@RequestMapping("/")
	public String home()
	{
		return "Welcome to Freezo";
	}

}
