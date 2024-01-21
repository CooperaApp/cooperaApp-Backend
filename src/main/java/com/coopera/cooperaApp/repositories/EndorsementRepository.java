package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.EndorsementRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndorsementRepository extends JpaRepository<EndorsementRequest, String> {
}
