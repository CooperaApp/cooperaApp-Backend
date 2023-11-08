package com.coopera.cooperaApp.security.provider;

import com.mongodb.client.model.Collation;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.coopera.cooperaApp.security.SecurityUtils.BADCREDENTIALSEXCEPTION;

@Component
@AllArgsConstructor
public class CooperaAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticationResult;
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String memberEmail = userDetails.getUsername();
        String memberPassword = userDetails.getPassword();
        Collection<? extends  GrantedAuthority> authorities = userDetails.getAuthorities();

        if (passwordEncoder.matches(password, memberPassword)) {
            authenticationResult = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword, authorities);
            return authenticationResult;
        }
        throw new BadCredentialsException(BADCREDENTIALSEXCEPTION);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.equals(UsernamePasswordAuthenticationToken.class)) return true;
        return false;
    }
}
