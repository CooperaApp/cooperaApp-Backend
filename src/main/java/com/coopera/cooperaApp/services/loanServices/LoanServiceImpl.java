package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.DurationPeriodType;
import com.coopera.cooperaApp.enums.EndorsementResponse;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.EndorsementRequest;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.LoanRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.coopera.cooperaApp.utilities.AppUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.AssertTrue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final ObjectMapper objectMapper;
    private final LoanRepository loanRepository;

    @Override
    public Loan requestLoan(LoanRequest loanRequest, MemberService memberService) throws CooperaException {
        String memberId = retrieveMemberId();
        MemberResponse foundMemberResponse = memberService.findById(memberId);
        Member foundMember = memberService.findMemberById(memberId);
        boolean isEligible =  checkMemberEligibility(foundMember);
        if (!isEligible) throw new CooperaException(NOT_ELIGIBLE_FOR_LOAN);
        Loan loan;
        try {
            loan = objectMapper.readValue(objectMapper.writeValueAsString(loanRequest), Loan.class);
        } catch (JsonProcessingException e) {
            throw new CooperaException(e.getMessage());
        }
        loan.setCooperativeId(foundMember.getCooperativeId());
        loan.setMemberId(memberId);
        loan.setMemberName(foundMemberResponse.getName());
        return loanRepository.save(loan);
    }


    private boolean checkMemberEligibility(Member member){
        boolean checkIfProperlyEndorsed = false;
        boolean eligibleByAmount = member.getBalance().compareTo(balanceRequiredToEndorse) > 0;
        List<EndorsementRequest> endorsementRequests = member.getEndorsements();
        long numberOfAcceptedEndorsements = endorsementRequests.stream()
                .filter(request -> request.getEndorsementResponse() == EndorsementResponse.ACCEPT)
                .count();
        if (numberOfAcceptedEndorsements >= 2) checkIfProperlyEndorsed = true;
        return checkIfProperlyEndorsed && eligibleByAmount;

    }
    @Override
    public Loan updateSavingsStatus(String loanId, LoanStatus loanStatus, CooperativeService cooperativeService) throws LoanException {
        Loan foundLoan = findById(loanId);
        foundLoan.setLoanStatus(loanStatus);
        if (loanStatus.equals(LoanStatus.ONGOING)) {
            processLoanDueDate(foundLoan);
            Cooperative cooperative = cooperativeService.findById(foundLoan.getCooperativeId()).get();
            Double interestRate = cooperative.getAccountingEntry().getInterestRate();
            if (interestRate == null || interestRate == 0 || interestRate < 0){
                throw new LoanException(INTEREST_CANNOT_BE_CALCULATED);
            }
            BigDecimal repaymentAmount = calculateRepaymentAmount(foundLoan, cooperative.getAccountingEntry().getInterestRate());
            foundLoan.setRepaymentAmount(repaymentAmount);
        } else if (loanStatus.equals(LoanStatus.REJECTED)) {
            foundLoan.setDateRejected(LocalDateTime.now());
        }
        return loanRepository.save(foundLoan);
    }

    private static void processLoanDueDate(Loan foundLoan) {
        foundLoan.setDateApproved(LocalDateTime.now());
        if (foundLoan.getLoanDuration().getDurationPeriodType().equals(DurationPeriodType.DAY)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusDays(foundLoan.getLoanDuration().getPeriod());
            foundLoan.setDueDate(dueDate);
        } else if (foundLoan.getLoanDuration().getDurationPeriodType().equals(DurationPeriodType.MONTH)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusMonths(foundLoan.getLoanDuration().getPeriod());
            foundLoan.setDueDate(dueDate);
        } else if (foundLoan.getLoanDuration().getDurationPeriodType().equals(DurationPeriodType.YEAR)) {
            LocalDateTime dueDate = foundLoan.getDateApproved().plusYears(foundLoan.getLoanDuration().getPeriod());
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
    public List<Loan> findByMemberId(MemberService memberService) throws CooperaException, LoanException {
        String memberId = retrieveMemberId();
        return loanRepository.findAllByMemberId(memberId).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, memberId))
        );
    }


    @Override
    public List<Loan> findByCooperativeId(CooperativeService cooperativeService) throws LoanException {
        String cooperativeId = retrieveCooperativeId();
        cooperativeService.findById(cooperativeId);
        return loanRepository.findAllByCooperativeId(cooperativeId).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, cooperativeService))
        );
    }

    @Override
    public List<Loan> findByMemberIdAndStatus(LoanStatus loanStatus, MemberService memberService) throws CooperaException, LoanException {
        String memberId = retrieveMemberId();
        memberService.findById(memberId);
        return loanRepository.findAllByMemberIdAndLoanStatus(memberId, loanStatus).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, memberId))
        );
    }

    @Override
    public List<Loan> findByCooperativeIdAndStatus(LoanStatus loanStatus, CooperativeService cooperativeService) throws LoanException {
        String cooperativeId = retrieveCooperativeId();
        cooperativeService.findById(cooperativeId);
        return loanRepository.findAllByCooperativeIdAndLoanStatus(cooperativeId, loanStatus).orElseThrow(
                () -> new LoanException(String.format(LOAN_NOT_FOUND, cooperativeId))
        );
    }

    @Override
    public BigDecimal calculateTotalDisbursedLoan(String cooperativeId) {
        BigDecimal totalDisbursedLoan = loanRepository.calculateTotalDisbursedLoan(cooperativeId);
        return totalDisbursedLoan == null ? BigDecimal.ZERO : totalDisbursedLoan;
    }

    @Override
    public BigDecimal calculateTotalRepaidLoan(String cooperativeId) {
        BigDecimal totalRepaidLoan = loanRepository.calculateTotalRepaidLoan(cooperativeId);
        return totalRepaidLoan == null ? BigDecimal.ZERO : totalRepaidLoan;
    }
}
