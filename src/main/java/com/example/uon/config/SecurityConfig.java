package com.example.uon.config;

import com.example.uon.security.FirebaseAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseAuthorizationFilter firebaseAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("==> Security Filter Chain Configuration");
        System.out.println(auth1);
        if (auth1 != null) {
            System.out.println("Authenticated User: " + auth1.getName()); // Usually UID or username
            System.out.println("Authorities: " + auth1.getAuthorities());
            System.out.println("Principal: " + auth1.getPrincipal());
            System.out.println("Is Authenticated: " + auth1.isAuthenticated());
        } else {
            System.out.println("No authentication found in context.");
        }

        http
                .csrf(csrf -> csrf.disable())
                // .sessionManagement(session ->
                // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // "/api/student/**",
                                "/api/auth/**",
                                "/static/**",
                                "/h2-console/**")
                        .permitAll()
                        .requestMatchers(
                                "/api/tutor/**",
                                "/api/student/**")
                        .authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(firebaseAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}