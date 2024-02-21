package com.coopera.cooperaApp.security;

import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.exceptionHandlers.CustomAuthenticationFailureHandler;
import com.coopera.cooperaApp.security.filter.CooperaAuthenticationFilter;
import com.coopera.cooperaApp.security.filter.CooperaAuthorizationFilter;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final MemberService memberService;
    private final CooperativeService cooperativeService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new CooperaAuthenticationFilter(authenticationManager, objectMapper, null, null, jwtUtil, memberService, cooperativeService, null);
        CooperaAuthorizationFilter authorizationFilter = new CooperaAuthorizationFilter(jwtUtil);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(getUrlBasedCorsConfigurationSource()))
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

    private static UrlBasedCorsConfigurationSource getUrlBasedCorsConfigurationSource() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configurationSource.registerCorsConfiguration("/api/**", corsConfig);
        return configurationSource;
    }

}
