package com.coopera.cooperaApp.security;

import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.security.filter.CooperaAuthenticationFilter;
import com.coopera.cooperaApp.security.filter.CooperaAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtUtil jwtUtil;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new CooperaAuthenticationFilter(authenticationManager, objectMapper, null, null);
        CooperaAuthorizationFilter authorizationFilter = new CooperaAuthorizationFilter(jwtUtil);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()).
                sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, CooperaAuthenticationFilter.class)
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request.requestMatchers("v1/admin/register-cooperative-mock").permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("v1/admin/test-token-mock").hasAnyAuthority(Role.ADMIN.name()))
                .build();
    }




}
