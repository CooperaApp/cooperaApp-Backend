package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.EndorsementRequest;

import java.util.List;

public interface LoanEligibility {

    String endorseMember(String endorsementRequestId) throws CooperaException;

    String rejectMember(String endorsementRequestId) throws CooperaException;

    String sendEndorsementRequest(LoanRequest loanRequest, String memberId) throws CooperaException;

    List<EndorsementRequest> findAllPendingEndorsementRequest() throws CooperaException;

    List<EndorsementRequest> findAllAcceptedEndorsementRequest() throws CooperaException;


    List<EndorsementRequest> findAllRejectedEndorsementRequest() throws CooperaException;




}
