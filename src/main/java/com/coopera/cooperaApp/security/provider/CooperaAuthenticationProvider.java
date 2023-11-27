package com.coopera.cooperaApp.security.provider;

import com.coopera.cooperaApp.security.user.CooperaUserDetails;
import com.coopera.cooperaApp.security.user.CooperaUserDetailsService;
import com.coopera.cooperaApp.security.user.CooperativeDetailsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.mongodb.client.model.Collation;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
    private final CooperativeService cooperativeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticationResult;
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        CooperativeDetailsService cooperativedetails = new CooperativeDetailsService(cooperativeService);
        System.out.println(email);
      var details =   cooperativedetails.loadUserByUsername(email);
        System.out.println(details.getUsername());
      String cooperativeId = details.getUsername();
      String cooperativePassword = details.getPassword();

        String memberEmail = userDetails.getUsername();
        String memberPassword = userDetails.getPassword();
        Collection<? extends  GrantedAuthority> authorities = userDetails.getAuthorities();

        if (password.equals("String")) {
            authenticationResult = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword, authorities);

            return authenticationResult;
        }
        else {
            authenticationResult = new UsernamePasswordAuthenticationToken(cooperativeId, cooperativePassword);
            System.out.println(authenticationResult);
            return authenticationResult;

        }


    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.equals(UsernamePasswordAuthenticationToken.class)) return true;
        return false;
    }
}
