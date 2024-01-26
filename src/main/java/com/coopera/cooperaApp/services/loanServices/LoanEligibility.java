package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Endorsement;

import java.util.List;

public interface LoanEligibility {

    String endorseMember(String endorsementRequestId) throws CooperaException;

    String rejectMember(String endorsementRequestId) throws CooperaException;

    Endorsement sendEndorsementRequest(String endorserId) throws CooperaException;

    List<Endorsement> findAllPendingEndorsementRequest(int page, int items) throws CooperaException;

    List<Endorsement> findAllAcceptedEndorsementRequest(int page, int items) throws CooperaException;


    List<Endorsement> findAllRejectedEndorsementRequest(int page, int items) throws CooperaException;




}
