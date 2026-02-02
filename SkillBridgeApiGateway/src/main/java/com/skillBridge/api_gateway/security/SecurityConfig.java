package com.skillBridge.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtFilter customJwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
   
                .authorizeExchange(auth -> auth
                	    .pathMatchers("/auth/**").permitAll()
                	    .pathMatchers(HttpMethod.POST, "/students/register").permitAll()
                	    .pathMatchers("/admin/**").hasRole("ADMIN")
                	    .pathMatchers(HttpMethod.GET, "/students/*/exists").permitAll()
                	    .pathMatchers("/students/**").hasAnyRole("ADMIN","STUDENT")
                	    .pathMatchers("/projects/**").hasAnyRole("ADMIN","STUDENT")
                	    .pathMatchers("/applications/**").hasAnyRole("ADMIN","STUDENT")
                	    .pathMatchers("/direct-messages/**").hasAnyRole("ADMIN","STUDENT")
                	   
                	    .anyExchange().authenticated()
                	)



                .addFilterAt(customJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
