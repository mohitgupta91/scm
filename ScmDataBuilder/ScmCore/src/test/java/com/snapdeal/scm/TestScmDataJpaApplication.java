package com.snapdeal.scm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;

/**
 * ScmDataJpaApplication : Scm Data jpa test conf
 * 
 * @author pranav
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
@ComponentScan({ "com.snapdeal.scm.mongo" })
@PropertySources({ @PropertySource("classpath:application.properties")})
@EnableMongoAuditing
public class TestScmDataJpaApplication {

    @Autowired
    private ScmJmsPropertyMongoRepository repo;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestScmDataJpaApplication.class, args);
    }
    
    @Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

}