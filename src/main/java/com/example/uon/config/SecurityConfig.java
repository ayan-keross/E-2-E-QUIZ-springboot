package com.example.uon.config;

import com.example.uon.security.FirebaseAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/api/auth/register", // Allow registration
                "/h2-console/**"      // Allow H2 console access for development
            ).permitAll()
            .antMatchers("/api/tutor/**").hasAuthority("TUTOR")
            .antMatchers("/api/student/**").hasAuthority("STUDENT")
            .anyRequest().authenticated();

        // Add our custom Firebase filter before the standard authentication filter
        http.addFilterBefore(firebaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // This is to allow H2 console frames to be displayed in browser
        http.headers().frameOptions().disable();

        return http.build();
    }
}

