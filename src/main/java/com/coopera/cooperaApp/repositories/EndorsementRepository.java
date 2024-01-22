package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.EndorsementRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<EndorsementRequest, String> {
    List<EndorsementRequest> getEndorsementRequestByEndorserEmail (String endorserEmail);

}
