package com.skillBridge.api_gateway.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomJwtFilter implements WebFilter {
	private final JwtUtils jwtUtils;
/*
 * filter method - Process the Web request and (optionally) delegate to the next WebFilter through the given WebFilterChain.
 */
	/*
	 *  ServerWebExchange 
	 *  - Contract for an HTTP request-response interaction. Provides access to the HTTP
 * request and response and also exposes additional server-side processing
 * related properties and features such as request attributes.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

	    String authHeader = exchange.getRequest()
	            .getHeaders()
	            .getFirst(HttpHeaders.AUTHORIZATION);

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return chain.filter(exchange);
	    }

	    String token = authHeader.substring(7);

	    try {
	        Claims claims = jwtUtils.validateToken(token);
	        log.info("JWT CLAIMS = {}", claims);
	        Long studentId = claims.get("studentId", Long.class);

	        List<?> rawRoles = claims.get("roles", List.class);

	        List<GrantedAuthority> authorities =
	                rawRoles.stream()
	                        .map(r -> new SimpleGrantedAuthority(r.toString()))
	                        .collect(Collectors.toList());

	        Authentication auth =
	                new UsernamePasswordAuthenticationToken(
	                        claims.getSubject(),
	                        null,
	                        authorities
	                );
	        
	        ServerWebExchange mutatedExchange =
	                exchange.mutate()
	                        .request(
	                                exchange.getRequest()
	                                        .mutate()
	                                        .header("X-User-Id", String.valueOf(studentId))
	                                        .build()
	                        )
	                        .build();

	        return chain.filter(mutatedExchange)
	                .contextWrite(
	                        ReactiveSecurityContextHolder.withAuthentication(auth)
	                );

	    } catch (Exception e) {
	        log.error("JWT filter error", e);
	        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	        return exchange.getResponse().setComplete();
	    }
	}


}
