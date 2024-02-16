package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.models.SavingsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SavingsLogRepository extends JpaRepository<SavingsLog, Integer> {
    Optional<List<SavingsLog>> findAllByMemberId(String memberId);
    Optional<List<SavingsLog>> findAllByMemberIdAndSavingsStatus(String memberId, SavingsStatus savingsStatus);
    Optional<List<SavingsLog>> findAllByCooperativeIdAndSavingsStatus(String cooperativeId, SavingsStatus savingsStatus);
    Optional<List<SavingsLog>> findAllByCooperativeId(String cooperativeId);

    @Query("SELECT SUM(s.amountSaved) FROM SavingsLog s WHERE s.cooperativeId = :cooperativeId and s.savingsStatus = com.coopera.cooperaApp.enums.SavingsStatus.SUCCESSFUL")
    BigDecimal calculateTotalCooperativeSavings(@Param("cooperativeId") String cooperativeId);


    @Query("SELECT SUM(s.amountSaved) FROM SavingsLog s WHERE s.memberId = :memberId and s.savingsStatus = com.coopera.cooperaApp.enums.SavingsStatus.SUCCESSFUL")
    BigDecimal calculateTotalMemberSavings(@Param("memberId") String memberId);


}
