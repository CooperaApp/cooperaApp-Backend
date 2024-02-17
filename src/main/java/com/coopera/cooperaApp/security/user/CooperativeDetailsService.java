package com.coopera.cooperaApp.security.user;

import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@AllArgsConstructor
public class CooperativeDetailsService implements UserDetailsService {
    private final CooperativeService cooperativeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Cooperative> cooperative = cooperativeService.findByEmail(username);
        return cooperative.map(CooperativeDetails::new).orElse(null);
    }
}
