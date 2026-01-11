package com.api_gateway;

import com.api_gateway.config.CorsFilterConfig;
import com.api_gateway.config.JwtAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilterConfig> corsFilterRegistration(CorsFilterConfig corsFilter) {
		FilterRegistrationBean<CorsFilterConfig> registration = new FilterRegistrationBean<>();
		registration.setFilter(corsFilter);
		registration.addUrlPatterns("/*");
		registration.setOrder(0);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.addUrlPatterns("/api/peliculas/*", "/api/actores/*", "/api/critica/usuario/*", "/api/usuario/*", "/api/critica/*");
		return registration;
	}
}

