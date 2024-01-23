package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<Endorsement, String> {
    List<Endorsement> getEndorsementRequestByEndorserEmail (String endorserEmail);

}
