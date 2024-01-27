package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String > {
    Optional<Member> findByEmail(String username);
    Page<Member> findAllByCooperativeId(String cooperativeId, Pageable pageable);
    Long countAllByCooperativeId(String cooperativeId);

}
