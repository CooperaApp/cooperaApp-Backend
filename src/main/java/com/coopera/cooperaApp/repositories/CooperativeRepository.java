package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Cooperative;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CooperativeRepository extends MongoRepository<Cooperative, String > {
}
