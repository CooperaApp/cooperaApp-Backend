package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String > {
    Optional<Member> findByEmail(String username);
    Long countAllByCooperativeId(String cooperativeId);

}
