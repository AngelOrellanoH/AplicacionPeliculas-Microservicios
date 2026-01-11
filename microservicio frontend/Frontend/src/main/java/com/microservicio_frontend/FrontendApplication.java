package com.microservicio_frontend;

import com.microservicio_frontend.security.JwtSessionFilter;
import com.microservicio_frontend.security.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class FrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

	@Bean
	public FilterRegistrationBean<JwtSessionFilter> jwtSessionFilter(JwtUtil jwtUtil) {
		FilterRegistrationBean<JwtSessionFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new JwtSessionFilter(jwtUtil));
		registration.addUrlPatterns("/*"); 
		registration.setOrder(1);
		return registration;
	}
}
