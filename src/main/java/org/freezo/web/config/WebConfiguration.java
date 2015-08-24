package org.freezo.web.config;

import org.freezo.web.view.WebsiteViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
@Profile("web")
public class WebConfiguration
{
	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Bean
	public ViewResolver delegatingViewResolver()
	{
		return new WebsiteViewResolver(viewResolver, this.viewResolver.getOrder() - 1);
	}

}
