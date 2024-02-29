package com.coopera.cooperaApp.configuration.interceptors;

import com.coopera.cooperaApp.dtos.requests.ActivityLogRequest;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.services.activityLogServices.ActivityLogService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.coopera.cooperaApp.utilities.AppUtils.retrieveCooperativeId;

@Component
@RequiredArgsConstructor
public class ApiInterceptor implements HandlerInterceptor {
    private final ActivityLogService activityLogService;
    private final CooperativeService cooperativeService;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getHeader("activityLog"));
        String logsAsString = request.getHeader("activityLog");
        if (logsAsString != null) {
            ActivityLogRequest activityLog = new ObjectMapper().readValue(logsAsString, ActivityLogRequest.class);
            Member member = memberService.findMemberByMail(activityLog.getMemberEmail());
            activityLogService.log(activityLog, member);
        }
        return true;
    }

}
