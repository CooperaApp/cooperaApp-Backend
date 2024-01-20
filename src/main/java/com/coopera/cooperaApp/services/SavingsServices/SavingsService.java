package com.coopera.cooperaApp.services.SavingsServices;

import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.SavingsResponse;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;

import java.math.BigDecimal;
import java.util.List;

public interface SavingsService {

    SavingsResponse saveToCooperative(SaveRequest amountToSave, MemberService memberService) throws CooperaException;

    SavingsLog findById(Integer savingsId) throws CooperaException;

    List<SavingsLog> findByMemberId(String memberId, MemberService memberService) throws CooperaException;

    List<SavingsLog> findByCooperativeId(String cooperativeId, CooperativeService cooperativeService) throws CooperaException;

    SavingsLog updateSavingsStatus(String savingsId, SavingsStatus savingsStatus) throws CooperaException;

    List<SavingsLog> findByMemberIdAndStatus(String memberId, SavingsStatus savingsStatus, MemberService memberService) throws CooperaException;

    List<SavingsLog> findByCooperativeIdAndStatus(String cooperativeId, SavingsStatus savingsStatus, CooperativeService cooperativeService) throws CooperaException;

    BigDecimal calculateTotalCooperativeSavings(String cooperativeId);

}
