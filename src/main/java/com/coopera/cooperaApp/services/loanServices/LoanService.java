package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;

import java.util.List;

public interface LoanService {
    Loan requestLoan(LoanRequest loanRequest, MemberService memberService) throws CooperaException;

    Loan updateSavingsStatus(String loanId, LoanStatus loanStatus) throws LoanException;

    Loan findById(String loanId) throws LoanException;

    List<Loan> findByMemberId(String memberId, MemberService memberService) throws CooperaException, LoanException;

    List<Loan> findByCooperativeId(String cooperativeId, CooperativeService cooperativeService) throws LoanException;

    List<Loan> findByMemberIdAndStatus(String memberId, LoanStatus loanStatus, MemberService memberService) throws CooperaException, LoanException;

    List<Loan> findByCooperativeIdAndStatus(String cooperativeId, LoanStatus loanStatus, CooperativeService cooperativeService) throws LoanException;
}
