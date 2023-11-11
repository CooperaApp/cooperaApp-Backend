package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String > {

    Optional<Member> findByEmail(String username);

}
