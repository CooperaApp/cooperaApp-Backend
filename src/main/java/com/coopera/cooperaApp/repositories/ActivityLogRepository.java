package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityLogRepository  extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findAllByMemberId(String memberId, Pageable pageable);
    Page<ActivityLog> findAllByCooperativeId(String cooperativeId, Pageable pageable);
}
