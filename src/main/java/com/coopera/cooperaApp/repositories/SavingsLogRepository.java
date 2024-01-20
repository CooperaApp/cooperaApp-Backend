package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.models.SavingsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingsLogRepository extends JpaRepository<SavingsLog, Integer> {
    Optional<List<SavingsLog>> findAllByMemberId(String memberId);
    Optional<List<SavingsLog>> findAllByMemberIdAndSavingsStatus(String memberId, SavingsStatus savingsStatus);
    Optional<List<SavingsLog>> findAllByCooperativeIdAndSavingsStatus(String cooperativeId, SavingsStatus savingsStatus);
    Optional<List<SavingsLog>> findAllByCooperativeId(String cooperativeId);
}
