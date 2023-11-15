package com.coopera.cooperaApp.services.cooperative;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface CooperativeService {
    RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException;
    void deleteAll();}
