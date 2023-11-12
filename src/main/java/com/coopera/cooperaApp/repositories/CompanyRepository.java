package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String > {
}
