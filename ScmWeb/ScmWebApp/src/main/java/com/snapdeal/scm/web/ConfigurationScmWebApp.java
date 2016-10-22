package com.snapdeal.scm.web;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.snapdeal.sacs.client.services.SACSClientService;
import com.snapdeal.scm.cache.ICacheLoadService;
import com.snapdeal.scm.web.services.cache.impl.CacheLoadServiceImpl;

@Configuration
@EnableAutoConfiguration
@PropertySources({ @PropertySource("classpath:application.properties"), @PropertySource("classpath:app.properties") })
@ComponentScan({"com.snapdeal.scm","com.snapdeal.base.transport", "com.snapdeal.base.transport.service"})
@EnableScheduling
public class ConfigurationScmWebApp  {

	@Value("${sacs.webservice.base.url}")
	private String sacsWebServiceUrl;

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationScmWebApp.class, args);
	}
	
	@Bean
	public SACSClientService sacsClientService() {
		SACSClientService sacsClientService = new SACSClientService();
		sacsClientService.setWebServiceBaseUrl(sacsWebServiceUrl);
		return sacsClientService;
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
	            ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/403");
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
	            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
	            container.addErrorPages(error401Page, error403Page, error404Page, error500Page);
	        }
	    };
	}
	
	@Bean
    @PostConstruct
    public ICacheLoadService runCacheFirst() {
        return new CacheLoadServiceImpl();
    }
}
