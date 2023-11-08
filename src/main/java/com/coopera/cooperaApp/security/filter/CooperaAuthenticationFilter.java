package com.coopera.cooperaApp.security.filter;

import com.coopera.cooperaApp.dtos.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.coopera.cooperaApp.security.SecurityUtils.BADCREDENTIALSEXCEPTION;

@AllArgsConstructor
@Component
public class CooperaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private String email;
    private String password;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            email = loginRequest.getEmail();
            password = loginRequest.getPassword();
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authenticationResult = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authenticationResult;
        } catch (IOException exception) {
            throw new BadCredentialsException(BADCREDENTIALSEXCEPTION);
        }

    }
}
