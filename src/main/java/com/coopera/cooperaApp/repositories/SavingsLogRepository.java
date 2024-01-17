package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.SavingsLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsLogRepository extends JpaRepository<SavingsLog, Integer> {
}
