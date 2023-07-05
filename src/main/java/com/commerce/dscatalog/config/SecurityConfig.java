package com.commerce.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {
	
	private static String ADMIN = "ADMIN";
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests( (authorize) -> authorize
	           .requestMatchers("/**").permitAll()
	           .requestMatchers("/user/cadastro").hasAuthority(ADMIN)
	           //.anyRequest().authenticated()
	     );

	    return http.build();
	}
}
