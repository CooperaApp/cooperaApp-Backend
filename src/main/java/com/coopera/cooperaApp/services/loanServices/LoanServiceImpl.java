package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.DurationType;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.repositories.LoanRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.LOAN_NOT_FOUND;

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
    public Loan updateSavingsStatus(String loanId, LoanStatus loanStatus, CooperativeService cooperativeService) throws LoanException {
        Loan foundLoan = findById(loanId);
        foundLoan.setLoanStatus(loanStatus);
        if (loanStatus.equals(LoanStatus.ONGOING)) {
            processLoanDueDate(foundLoan);
            Cooperative cooperative = cooperativeService.findById(foundLoan.getCooperativeId()).get();
            BigDecimal repaymentAmount = calculateRepaymentAmount(foundLoan, cooperative.getAccountingEntry().getInterestRate());
            foundLoan.setRepaymentAmount(repaymentAmount);
        } else if (loanStatus.equals(LoanStatus.REJECTED)) {
            foundLoan.setDateRejected(LocalDateTime.now());
        }
        return loanRepository.save(foundLoan);
    }

    private static void processLoanDueDate(Loan foundLoan) {
        foundLoan.setDateApproved(LocalDateTime.now());
        if (foundLoan.getLoanDuration().getDurationType().equals(DurationType.DAY)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusDays(foundLoan.getLoanDuration().getDuration());
            foundLoan.setDueDate(dueDate);
        } else if (foundLoan.getLoanDuration().getDurationType().equals(DurationType.MONTH)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusMonths(foundLoan.getLoanDuration().getDuration());
            foundLoan.setDueDate(dueDate);
        } else if (foundLoan.getLoanDuration().getDurationType().equals(DurationType.YEAR)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusYears(foundLoan.getLoanDuration().getDuration());
            foundLoan.setDueDate(dueDate);
        }
    }

    public static BigDecimal calculateRepaymentAmount(Loan loan, Double interestRate) {
        Duration duration = Duration.between(loan.getDateApproved(), loan.getDueDate());
        long durationInMonth = duration.toDays()/30;
        BigDecimal pow = BigDecimal.valueOf(Math.pow(1 + (interestRate / 100), durationInMonth));
        return loan.getAmount().multiply(pow);
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

    @Override
    public BigDecimal calculateTotalDisbursedLoan(String cooperativeId) {
        return loanRepository.calculateTotalDisbursedLoan(cooperativeId);
    }

    @Override
    public BigDecimal calculateTotalRepaidLoan(String cooperativeId) {
        return loanRepository.calculateTotalRepaidLoan(cooperativeId);
    }
}
