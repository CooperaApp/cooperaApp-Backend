package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.SavingsLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface SavingsLogRepository extends CrudRepository<SavingsLog, Integer> {
}
