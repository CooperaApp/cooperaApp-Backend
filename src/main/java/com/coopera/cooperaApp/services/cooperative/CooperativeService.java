package com.coopera.cooperaApp.services.cooperative;

import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.CooperativeDashboardStatistic;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;

import java.util.Optional;

public interface CooperativeService {

    RegisterCooperativeResponse registerCooperative(RegisterCooperativeRequest request, MemberService memberService) throws CooperaException;
    void deleteAll();
    Cooperative findCooperativeByMail(String mail);
    Object forgotPassword(String email) throws CooperaException;
    String resetPassword(PasswordResetRequest passwordResetRequest) throws CooperaException;
    Optional<Cooperative> findById(String id);
    void save(Cooperative cooperative);

    CooperativeDashboardStatistic getDashboardStatistics(SavingsService savingsService, LoanService loanService);
}


