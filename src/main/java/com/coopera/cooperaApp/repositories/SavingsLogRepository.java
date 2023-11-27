package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.SavingsLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SavingsLogRepository extends MongoRepository<SavingsLog, String> {
}
