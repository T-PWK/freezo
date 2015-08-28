package org.freezo.website.controller;

import javax.servlet.http.HttpServletRequest;

import org.freezo.core.domain.PostReporitory;
import org.freezo.core.domain.Website;
import org.freezo.website.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("web")
@Controller
public class HomeController
{
	@Autowired
	private PostReporitory repository;

	@Value("${freezo.web.listing.posts:5}")
	private int posts;

	@ModelAttribute("website")
	public Website getUser(final HttpServletRequest request)
	{
		return (Website) request.getAttribute(Constants.WEBSITE_ATTRIBUTE_NAME);
	}

	@RequestMapping("/")
	public String index(final Model model, @ModelAttribute("website") final Website website)
	{
		model.addAttribute("posts", repository.findByWebsite(website, new PageRequest(0, posts)));
		model.addAttribute("baseUrl", "");

		return "index";
	}

	@RequestMapping("/page/{page}")
	public String indexWithPagination(final Model model, @ModelAttribute("website") final Website website,
			@PathVariable("page") final int page)
	{
		model.addAttribute("posts", repository.findByWebsite(website, new PageRequest(page - 1, posts)));
		model.addAttribute("baseUrl", "");

		return "index";
	}

	@RequestMapping("/search/label/{label}")
	public String label(final Model model, @ModelAttribute("website") final Website website,
			@PathVariable("label") final String label)
	{
		model.addAttribute("posts", repository.findByWebsiteAndLabelsIn(website, label, new PageRequest(0, posts)));
		model.addAttribute("baseUrl", String.format("/search/label/%s", label));

		return "index";
	}

	@RequestMapping("/search/label/{label}/page/{page}")
	public String label(final Model model, @ModelAttribute("website") final Website website,
			@PathVariable("label") final String label, @PathVariable("page") final int page)
	{
		model.addAttribute("posts",
				repository.findByWebsiteAndLabelsIn(website, label, new PageRequest(page - 1, posts)));

		model.addAttribute("baseUrl", String.format("/search/label/%s", label));

		return "index";
	}

	@RequestMapping("/{slug}")
	public String page(@ModelAttribute("website") final Website website)
	{
		return "post";
	}

}
