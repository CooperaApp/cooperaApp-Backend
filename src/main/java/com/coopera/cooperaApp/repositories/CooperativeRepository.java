package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooperativeRepository extends JpaRepository<Cooperative, String > {
}
