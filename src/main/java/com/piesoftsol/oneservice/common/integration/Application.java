package com.piesoftsol.oneservice.common.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Class where the spring boot starts.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author Kiran
 */

@Configuration
@SpringBootApplication
public class Application {
	
	/**
	 * Spring boot application start run
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.run(args);
	}

}
