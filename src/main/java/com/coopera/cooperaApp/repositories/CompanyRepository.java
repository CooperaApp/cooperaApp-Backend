package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String > {
}
