package com.coopera.cooperaApp.security.provider;

import com.coopera.cooperaApp.security.user.CooperativeDetailsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@AllArgsConstructor
public class CooperaAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CooperativeService cooperativeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticationResult;
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        authenticationResult = authenticateIfAuthIsCooperative(email, password);
        if (authenticationResult == null) {
            authenticationResult = authenticateIfAuthIsMember(email, password);
        }
        return authenticationResult;
    }

    private Authentication authenticateIfAuthIsMember(String principal, String password) {
        Authentication authenticationResult;
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        String memberEmail = userDetails.getUsername();
        String memberPassword = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (passwordEncoder.matches(password, memberPassword)) {
            authenticationResult = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword, authorities);
            return authenticationResult;
        }
        return null;
    }

    private Authentication authenticateIfAuthIsCooperative(String principal, String password) {
        Authentication authenticationResult;
        CooperativeDetailsService cooperativedetails = new CooperativeDetailsService(cooperativeService);
        UserDetails details = cooperativedetails.loadUserByUsername(principal);
        if (!(details == null)) {
            String cooperativeId = details.getUsername();
            String cooperativePassword = details.getPassword();
            if (!cooperativeId.isEmpty()) {
                if (passwordEncoder.matches(password, cooperativePassword)) {
                    authenticationResult = new UsernamePasswordAuthenticationToken(cooperativeId, cooperativePassword);
                    return authenticationResult;
                }
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.equals(UsernamePasswordAuthenticationToken.class)) return true;
        return false;
    }
}
