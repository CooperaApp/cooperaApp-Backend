package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, String > {
    Optional<List<Loan>> findAllByMemberId(String memberId);
    Optional<List<Loan>> findAllByCooperativeId(String cooperativeId);
    Optional<List<Loan>> findAllByMemberIdAndLoanStatus(String memberId, LoanStatus loanStatus);
    Optional<List<Loan>> findAllByCooperativeIdAndLoanStatus(String cooperativeId, LoanStatus loanStatus);

    @Query("SELECT sum(l.amount) FROM Loan l where l.cooperativeId = :cooperativeId AND l.loanStatus IN (com.coopera.cooperaApp.enums.LoanStatus.ONGOING, com.coopera.cooperaApp.enums.LoanStatus.DUE)")
    BigDecimal calculateTotalDisbursedLoan(@Param("cooperativeId") String cooperativeId);
    @Query("SELECT sum(l.amount) FROM Loan l where l.cooperativeId = :cooperativeId AND l.loanStatus = com.coopera.cooperaApp.enums.LoanStatus.PAID")
    BigDecimal calculateTotalRepaidLoan(String cooperativeId);

}
