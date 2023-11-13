package com.coopera.cooperaApp.services.company;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.response.InitializeCompanyResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface CompanyService {
    InitializeCompanyResponse registerCompany (RegisterCompanyRequest request) throws CooperaException;
}
