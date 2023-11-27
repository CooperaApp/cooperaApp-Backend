package com.coopera.cooperaApp.security.user;

import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
public class CooperativeDetailsService implements UserDetailsService {
    private final CooperativeService cooperativeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Cooperative> cooperative =  cooperativeService.findByCooperativeById(username);
        return cooperative.map(CooperativeDetails::new).orElse(null);
    }
}
