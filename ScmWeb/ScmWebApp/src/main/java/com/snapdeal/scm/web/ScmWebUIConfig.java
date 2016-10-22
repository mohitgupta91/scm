package com.snapdeal.scm.web;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * SCMWebAppUIConfig : Web Configuration related to UI and resoruces
 * 
 * @author pranav
 *
 */
@Configuration
@EnableWebMvc
@AutoConfigureAfter(value={ConfigurationScmWebApp.class})
public class ScmWebUIConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("home");

		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/access").setViewName("access");
		registry.addViewController("/error").setViewName("error");
		registry.addViewController("/401").setViewName("401");
		registry.addViewController("/403").setViewName("403");
		registry.addViewController("/404").setViewName("404");
		registry.addViewController("/500").setViewName("500");
		super.addViewControllers(registry);
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/");
		resolver.setSuffix(".html");
		return resolver;
	}

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
		"classpath:/META-INF/resources/", "classpath:/resources/",
		"classpath:/static/", "classpath:/public/"};

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}	
}
