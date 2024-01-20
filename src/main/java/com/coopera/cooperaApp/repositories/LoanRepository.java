package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, String > {
    Optional<List<Loan>> findAllByMemberId(String memberId);
    Optional<List<Loan>> findAllByCooperativeId(String cooperativeId);
    Optional<List<Loan>> findAllByMemberIdAndLoanStatus(String memberId, LoanStatus loanStatus);
    Optional<List<Loan>> findAllByCooperativeIdAndLoanStatus(String cooperativeId, LoanStatus loanStatus);
}
