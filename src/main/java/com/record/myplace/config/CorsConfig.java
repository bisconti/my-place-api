package com.record.myplace.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration conf = new CorsConfiguration();
		
		conf.setAllowedOrigins(List.of("http://localhost:5173"));
		conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		conf.setAllowedHeaders(List.of("*"));
		conf.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", conf);
		
		return source;
	}
}
