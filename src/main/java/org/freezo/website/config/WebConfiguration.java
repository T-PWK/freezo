package org.freezo.website.config;

import org.freezo.website.service.RedirectsInterceptor;
import org.freezo.website.service.WebsiteListener;
import org.freezo.website.service.WebsiteViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Profile("web")
@Configuration
public class WebConfiguration
{
	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Bean
	public ViewResolver delegatingViewResolver()
	{
		return new WebsiteViewResolver(viewResolver, this.viewResolver.getOrder() - 1);
	}

	@Profile("web")
	@Configuration
	protected static class WebConfig extends WebMvcConfigurerAdapter
	{
		@Autowired
		private RedirectsInterceptor redirectsInterceptor;

		@Autowired
		private WebsiteListener websiteListener;

		@Override
		public void addInterceptors(final InterceptorRegistry registry)
		{
			registry.addInterceptor(websiteListener).excludePathPatterns("/api/**", "/admin/**", "/error");
			registry.addInterceptor(redirectsInterceptor).excludePathPatterns("/api/**", "/admin/**", "/error");
		}
	}

}
