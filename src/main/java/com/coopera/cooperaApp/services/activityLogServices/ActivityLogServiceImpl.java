package com.coopera.cooperaApp.services.activityLogServices;

import com.coopera.cooperaApp.dtos.requests.ActivityLogRequest;
import com.coopera.cooperaApp.models.ActivityLog;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.ActivityLogRepository;
import com.coopera.cooperaApp.utilities.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.retrieveCooperativeId;
import static com.coopera.cooperaApp.utilities.AppUtils.retrieveMemberId;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService{
    private final ActivityLogRepository activityLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public ActivityLog log(ActivityLogRequest logRequest, Member member){
        ActivityLog activityLog = modelMapper.map(logRequest, ActivityLog.class);
        activityLog.setMemberId(member.getId());
        activityLog.setCooperativeId(member.getCooperativeId());
        activityLog.setMemberName(member.getFirstName()+" "+member.getLastName());
        return activityLogRepository.save(activityLog);
    }

    @Override
    public List<ActivityLog> findByMemberId(int page, int items) {
        String memberId = retrieveMemberId();
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        Page<ActivityLog> activityLogs = activityLogRepository.findAllByMemberId(memberId, pageable);
        return activityLogs.getContent();
    }

    @Override
    public List<ActivityLog> findByCooperativeId(int page, int items) {
        String cooperativeId = retrieveCooperativeId();
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        Page<ActivityLog> activityLogs = activityLogRepository.findAllByCooperativeId(cooperativeId, pageable);
        return activityLogs.getContent();
    }
}
