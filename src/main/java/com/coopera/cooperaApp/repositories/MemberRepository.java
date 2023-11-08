package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String > {

}
