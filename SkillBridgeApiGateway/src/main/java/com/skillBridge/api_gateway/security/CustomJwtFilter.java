package com.skillBridge.api_gateway.security;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        log.info("Incoming request → {} {}", method, path);

        // ✅ PUBLIC ENDPOINTS (JWT SKIPPED)
        if (
                path.startsWith("/auth/")
                || (path.startsWith("/students/register") && method == HttpMethod.POST)
                || (path.startsWith("/students/login") && method == HttpMethod.POST)
                || (path.matches("/students/.*/exists") && method == HttpMethod.GET)
        ) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // ❌ NO TOKEN
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = jwtUtils.validateToken(token);

            Long studentId = claims.get("studentId", Long.class);
            String role = claims.get("role", String.class);

            if (studentId == null || role == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            studentId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            ServerWebExchange mutatedExchange =
                    exchange.mutate()
                            .request(exchange.getRequest()
                                    .mutate()
                                    .header("X-USER-ID", String.valueOf(studentId))
                                    .header("X-ROLE", role)
                                    .build())
                            .build();

            return chain.filter(mutatedExchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(authentication)
                    );

        } catch (Exception e) {
            log.error("JWT validation failed", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
