package com.coopera.cooperaApp.services.SavingsServices;

import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.SavingsResponse;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.repositories.SavingsLogRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.SAVINGS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {
    private final SavingsLogRepository savingsLogRepository;

    @Override
    public SavingsResponse saveToCooperative(SaveRequest saveRequest, MemberService memberService) throws CooperaException {
        if (!checkIfAmountToSaveIsValid(saveRequest.getAmountToSave())) throw new CooperaException("Invalid Amount");
        BigDecimal amount = new BigDecimal(saveRequest.getAmountToSave());
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String memId = memberId.substring(1, memberId.length() - 1);
        String cooperativeId = extractCooperativeIdFromMemberId(memId);
        SavingsLog newSavingsLog = SavingsLog.builder()
                .amountSaved(amount)
                .timeSaved(LocalDateTime.now())
                .memberName(extractMemberName(memId, memberService))
                .memberId(memberId)
                .cooperativeId(cooperativeId)
                .savingsStatus(SavingsStatus.PENDING)
                .build();
        savingsLogRepository.save(newSavingsLog);
        return SavingsResponse.builder().message("Saved Successfully").build();
    }

    @Override
    public SavingsLog findById(Integer savingsId) throws CooperaException {
        return savingsLogRepository.findById(savingsId).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, savingsId))
        );
    }

    @Override
    public List<SavingsLog> findByMemberId(String memberId, MemberService memberService) throws CooperaException {
        memberService.findById(memberId);
        return savingsLogRepository.findAllByMemberId(memberId).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, memberId))
        );
    }

    @Override
    public List<SavingsLog> findByCooperativeId(String cooperativeId, CooperativeService cooperativeService) throws CooperaException {
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
    public List<SavingsLog> findByMemberIdAndStatus(String memberId, SavingsStatus savingsStatus, MemberService memberService) throws CooperaException {
        memberService.findById(memberId);
        return savingsLogRepository.findAllByMemberIdAndSavingsStatus(memberId, savingsStatus).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, memberId))
        );
    }


    @Override
    public List<SavingsLog> findByCooperativeIdAndStatus(String cooperativeId, SavingsStatus savingsStatus, CooperativeService cooperativeService) throws CooperaException {
        cooperativeService.findById(cooperativeId);
        return savingsLogRepository.findAllByCooperativeIdAndSavingsStatus(cooperativeId, savingsStatus).orElseThrow(
                () -> new CooperaException(String.format(SAVINGS_NOT_FOUND, cooperativeId))
        );
    }

    @Override
    public BigDecimal calculateTotalCooperativeSavings(String cooperativeId) {
        return savingsLogRepository.calculateTotalCooperativeSavings(cooperativeId);
    }


    private String extractMemberName(String id, MemberService memberService) throws CooperaException {
        var response = memberService.findById(id);
        return response.getName();
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

    private String extractCooperativeIdFromMemberId(String memberId) {
        String[] newId = memberId.split("/");
        StringBuilder cooID = new StringBuilder();
        int indexOfLastElementInArray = 2;
        for (int i = 0; i < newId.length - 1; i++) {
            if (i == indexOfLastElementInArray) cooID.append(newId[i]);
            else cooID.append(newId[i]).append("/");
        }
        return cooID.toString();
    }
}
