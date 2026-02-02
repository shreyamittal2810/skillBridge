//package com.skillBridge.sms_service.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//    public SecurityFilterChain filterChain(
//            HttpSecurity http,
//            JwtUtils jwtUtils) throws Exception {
//
//        return http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(request ->
//				request.anyRequest().permitAll());	
//                .addFilterBefore(
//                        new JwtAuthenticationFilter(jwtUtils),
//                        UsernamePasswordAuthenticationFilter.class
//                )
//                .build();
//    }
//}


package com.skillBridge.sms_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration // To declare a java config class (equivalent to bean config xml file)
@EnableWebSecurity // to enable spring security
@EnableMethodSecurity // optional to add method level authorization rules
@Slf4j
public class SecurityConfig {

	

	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        log.info("Student service security initialized");

	        http
	            .csrf(csrf -> csrf.disable())
	            .sessionManagement(session ->
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            )
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().permitAll()
	            );

	        return http.build();
	    }

	// Configure AuthManager as spring bean
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
