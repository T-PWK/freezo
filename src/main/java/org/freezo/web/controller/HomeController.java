package org.freezo.web.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(prefix = "freezo", name = "enableWeb", havingValue = "true")
public class HomeController
{
	@RequestMapping
	public String home()
	{
		return "Welcome to Freezo";
	}

}
