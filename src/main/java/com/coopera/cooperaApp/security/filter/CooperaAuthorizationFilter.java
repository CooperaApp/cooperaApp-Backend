package com.coopera.cooperaApp.security.filter;

import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.security.EndPointConstants;
import com.coopera.cooperaApp.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.interfaces.Claim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@AllArgsConstructor
public class CooperaAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isUnAuthorizedPath = EndPointConstants.UNAUTHORIZEDENDPOINTS.contains(request.getServletPath()) &&
                request.getMethod().equals(HttpMethod.POST.name());
        if (isUnAuthorizedPath) filterChain.doFilter(request, response);
        else {
            try {
                authorizeRequest(request, response, filterChain);
            } catch (RuntimeException | CooperaException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void authorizeRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, CooperaException {
        authorize(request);
        filterChain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request) throws CooperaException {
        String authorization = request.getHeader(AUTHORIZATION);
        String tokenPrefix = "Bearer ";
        boolean isValidAuthorizationHeader = false;
        if (authorization != null && authorization.startsWith(tokenPrefix))
            isValidAuthorizationHeader = true;

        if (isValidAuthorizationHeader) {
            String token = authorization.substring(tokenPrefix.length());
            System.out.println(token + " token from filter");
            authorizeToken(token);
        }
    }

    private void authorizeToken(String token) throws CooperaException {
        Map<String, Claim> map = jwtUtil.extractClaimsFromToken(token);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
       // Claim roles = map.get("Roles");
        Claim id = map.get("id");
        System.out.println("this is the ID " + id);

      //  addClaimToUserAuthorities(authorities, roles);
        Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void addClaimToUserAuthorities(List<SimpleGrantedAuthority> authorities, Claim claim) {
        for (int i = 0; i < claim.asMap().size(); i++) {
            String role = (String) claim.asMap().get("role" + (i + 1));
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }
        }
    }
}
