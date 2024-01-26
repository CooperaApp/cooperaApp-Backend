package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.enums.EndorsementStatus;
import com.coopera.cooperaApp.models.Endorsement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<Endorsement, String> {
    Page<Endorsement> findEndorsementByEndorserEmailAndEndorsementStatus(String endorserEmail, EndorsementStatus endorsementStatus, Pageable pageable);

}
