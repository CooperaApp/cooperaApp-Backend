package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface LoanEligibility {

    Boolean endorseMember(String memberId, String endorsementRequestId) throws CooperaException;

    String sendEndorsementRequest(LoanRequest loanRequest, String memberId) throws CooperaException;





}
