package com.coopera.cooperaApp.services.SavingsServices;

import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.repositories.SavingsLogRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsServiceImpl implements SavingsService {
    private final SavingsLogRepository savingsLogRepository;
    private final MemberService memberService;

    @Override
    public SavingsLog saveToCooperative(SaveRequest saveRequest, MemberService memberService) throws CooperaException {
        if (!checkIfAmountToSaveIsValid(saveRequest.getAmountToSave())) throw new CooperaException("Invalid Amount");
        BigDecimal amount = new BigDecimal(saveRequest.getAmountToSave());
        String memberId = retrieveMemberId();
        MemberResponse foundMember = memberService.findById(memberId);
        SavingsLog newSavingsLog = SavingsLog.builder()
                .amountSaved(amount)
                .timeSaved(LocalDateTime.now())
                .memberName(foundMember.getName())
                .memberId(foundMember.getId())
                .memberEmail(foundMember.getEmail())
                .cooperativeId(foundMember.getCooperativeId())
                .savingsStatus(SavingsStatus.PENDING)
                .build();
        return savingsLogRepository.save(newSavingsLog);
    }

    @Override
    public SavingsLog findById(Integer savingsId) throws CooperaException {
        return savingsLogRepository.findById(savingsId).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, savingsId))
        );
    }

    @Override
    public List<SavingsLog> findByMemberId(MemberService memberService) throws CooperaException {
        String memberId = retrieveMemberId();
        memberService.findById(memberId);
        return savingsLogRepository.findAllByMemberId(memberId).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, memberId))
        );
    }

    @Override
    public List<SavingsLog> findByCooperativeId(CooperativeService cooperativeService) throws CooperaException {
        String cooperativeId = retrieveCooperativeId();
        cooperativeService.findById(cooperativeId);
        return savingsLogRepository.findAllByCooperativeId(cooperativeId).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, cooperativeService))
        );
    }

    @Override
    public SavingsLog updateSavingsStatus(String savingsId, SavingsStatus savingsStatus) throws CooperaException {
        SavingsLog foundSavingsLog = findById(Integer.valueOf(savingsId));
        foundSavingsLog.setSavingsStatus(savingsStatus);
        return savingsLogRepository.save(foundSavingsLog);
    }

    @Override
    public List<SavingsLog> findByMemberIdAndStatus(SavingsStatus savingsStatus, MemberService memberService) throws CooperaException {
        String memberId = retrieveMemberId();
        memberService.findById(memberId);
        return savingsLogRepository.findAllByMemberIdAndSavingsStatus(memberId, savingsStatus).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, memberId))
        );
    }


    @Override
    public List<SavingsLog> findByCooperativeIdAndStatus(SavingsStatus savingsStatus, CooperativeService cooperativeService) throws CooperaException {
        String cooperativeId = retrieveCooperativeId();
        cooperativeService.findById(cooperativeId);
        return savingsLogRepository.findAllByCooperativeIdAndSavingsStatus(cooperativeId, savingsStatus).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, cooperativeId))
        );
    }

    @Override
    public BigDecimal calculateTotalCooperativeSavings(String cooperativeId) {
        BigDecimal totalCooperativeSavings = savingsLogRepository.calculateTotalCooperativeSavings(cooperativeId);
        return totalCooperativeSavings == null ? BigDecimal.ZERO : totalCooperativeSavings;
    }

    @Override
    public BigDecimal calculateTotalMemberSavings(String memberId) {
        BigDecimal totalMemberSavings = savingsLogRepository.calculateTotalMemberSavings(memberId);
        return totalMemberSavings == null ? BigDecimal.ZERO : totalMemberSavings;
    }


    public boolean checkIfAmountToSaveIsValid(String amountToSave) {
        if (amountToSave == null) {
            return false;
        }
        try {
            Integer d = Integer.parseInt(amountToSave);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
