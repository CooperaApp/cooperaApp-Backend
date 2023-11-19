package com.coopera.cooperaApp.services.company;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.InitializeCompanyResponse;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface CompanyService {

    RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request) throws CooperaException;
    void deleteAll();

}
