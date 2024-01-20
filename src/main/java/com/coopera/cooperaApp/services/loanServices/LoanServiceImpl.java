package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.repositories.LoanRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.LOAN_NOT_FOUND;
import static com.coopera.cooperaApp.utilities.AppUtils.SAVINGS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final ObjectMapper objectMapper;
    private final LoanRepository loanRepository;
    @Override
    public Loan requestLoan(LoanRequest loanRequest, MemberService memberService) throws CooperaException {
        MemberResponse foundMember = memberService.findById(loanRequest.getMemberId());
        Loan loan;
        try {
            loan = objectMapper.readValue(objectMapper.writeValueAsString(loanRequest), Loan.class);
        } catch (JsonProcessingException e) {
            throw new CooperaException(e.getMessage());
        }
        loan.setCooperativeId(foundMember.getCooperativeId());
        return loanRepository.save(loan);
    }

    @Override
    public Loan updateSavingsStatus(String loanId, LoanStatus loanStatus) throws LoanException {
        Loan foundLoan = findById(loanId);
        foundLoan.setLoanStatus(loanStatus);
        if (loanStatus.equals(LoanStatus.ONGOING)){

        }
        return loanRepository.save(foundLoan);
    }

    @Override
    public Loan findById(String loanId) throws LoanException {
        return loanRepository.findById(loanId).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, loanId))
        );
    }

    @Override
    public List<Loan> findByMemberId(String memberId, MemberService memberService) throws CooperaException, LoanException {
        memberService.findById(memberId);
        return loanRepository.findAllByMemberId(memberId).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, memberId))
        );
    }


    @Override
    public List<Loan> findByCooperativeId(String cooperativeId, CooperativeService cooperativeService) throws LoanException {
        cooperativeService.findById(cooperativeId);
        return loanRepository.findAllByCooperativeId(cooperativeId).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, cooperativeService))
        );
    }

    @Override
    public List<Loan> findByMemberIdAndStatus(String memberId, LoanStatus loanStatus, MemberService memberService) throws CooperaException, LoanException {
        memberService.findById(memberId);
        return loanRepository.findAllByMemberIdAndLoanStatus(memberId, loanStatus).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, memberId))
        );
    }

    @Override
    public List<Loan> findByCooperativeIdAndStatus(String cooperativeId, LoanStatus loanStatus, CooperativeService cooperativeService) throws LoanException {
        cooperativeService.findById(cooperativeId);
        return loanRepository.findAllByMemberIdAndLoanStatus(cooperativeId, loanStatus).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, cooperativeId))
        );
    }
}
