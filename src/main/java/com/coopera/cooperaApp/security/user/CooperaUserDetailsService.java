package com.coopera.cooperaApp.security.user;

import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.CooperativeRepository;
import com.coopera.cooperaApp.repositories.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
//@Primary
public class CooperaUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(username);
        UserDetails userDetails = new CooperaUserDetails(member.get());
        return userDetails;
    }
}
