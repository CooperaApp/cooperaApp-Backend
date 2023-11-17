package com.coopera.cooperaApp.services.cooperative;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface CooperativeService {
    RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException;
    void deleteAll();
    Optional<Cooperative> findByCooperativeById(String id);

    void save(Cooperative cooperative);

}


