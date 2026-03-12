package com.financial.andrew.configs;

import org.financial.common.configs.SecuritySharedConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return SecuritySharedConfig
                .applyCommonSecurity(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/profit-and-lose/**").permitAll()
                        .requestMatchers("/income/**").permitAll()
                        .requestMatchers("/spending/**").permitAll()
                        .requestMatchers("/transactions/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.and())  // Enable CORS
                .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}