package com.coopera.cooperaApp.services.activityLogServices;

import com.coopera.cooperaApp.dtos.requests.ActivityLogRequest;
import com.coopera.cooperaApp.models.ActivityLog;
import com.coopera.cooperaApp.models.Member;

import java.util.List;

public interface ActivityLogService {
    ActivityLog log(ActivityLogRequest logRequest, Member member);
    List<ActivityLog> findByMemberId(int page, int items);
    List<ActivityLog> findByCooperativeId(int page, int items);
}
