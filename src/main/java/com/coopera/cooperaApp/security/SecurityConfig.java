package com.coopera.cooperaApp.security;

import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.exceptionHandlers.CustomAuthenticationFailureHandler;
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
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new CooperaAuthenticationFilter(authenticationManager, objectMapper, null, null, jwtUtil);
        CooperaAuthorizationFilter authorizationFilter = new CooperaAuthorizationFilter(jwtUtil);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, CooperaAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandler -> exceptionHandler
                                .authenticationEntryPoint(customAuthenticationFailureHandler::onAuthenticationFailure)
                )
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/cooperative/register", "/api/v1/admin/generateLink", "/api/v1/admin/testing", "/api/v1/member/save", "api/v1/member/register", "api/v1/loans/requestLoan","api/v1/cooperative/forgotPassword", "api/v1/cooperative/resetPassword", "api/v1/member/forgotPassword", "api/v1/member/resetPassword")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .build();
    }


}
