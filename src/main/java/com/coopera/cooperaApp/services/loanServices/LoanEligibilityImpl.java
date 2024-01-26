package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.enums.EndorsementStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Endorsement;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.EndorsementRepository;
import com.coopera.cooperaApp.services.member.MemberService;
import com.coopera.cooperaApp.utilities.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LoanEligibilityImpl implements LoanEligibility{

    private final EndorsementRepository endorsementRepository;

    private final MemberService memberService;


    @Override
    public String endorseMember(String endorsementRequestId) throws CooperaException {
        String endorserId = AppUtils.retrieveMemberId();
        Member endorser = memberService.findMemberById(endorserId);
        if (checkIfEndorserIsEligible(endorser)){
        Endorsement foundEndorsement = endorsementRepository.findById(endorsementRequestId).
                orElseThrow(() -> new CooperaException("Not found"));
        foundEndorsement.setEndorsementStatus(EndorsementStatus.ACCEPT);
        endorsementRepository.save(foundEndorsement);
        }
        return "You have successfully endorsed this member";
    }

    @Override
    public String rejectMember(String endorsementRequestId) throws CooperaException {
        String endorserId = AppUtils.retrieveMemberId();
        Member endorser = memberService.findMemberById(endorserId);
        if (checkIfEndorserIsEligible(endorser)){
            Endorsement foundEndorsement = endorsementRepository.findById(endorsementRequestId).
                    orElseThrow(() -> new CooperaException("Not found"));
            foundEndorsement.setEndorsementStatus(EndorsementStatus.REJECT);
            endorsementRepository.save(foundEndorsement);
        }
        return "You have successfully rejected this member";
    }

    private boolean checkIfEndorserIsEligible(Member member){
        return member.getBalance().compareTo(AppUtils.balanceRequiredToEndorse) > 0;
    }

    @Override
    public Endorsement sendEndorsementRequest(String endorserId) throws CooperaException {
        String endorseeId = AppUtils.retrieveMemberId();
        Member requester = memberService.findMemberById(endorseeId);
        Member endorser = memberService.findMemberById(endorserId);
        log.info("THis is the IDs" + endorserId);
        Endorsement endorsement = Endorsement.builder().
                endorserEmail(endorser.getEmail()).
                requesterEmail(requester.getEmail()).
                build();
        //Notify by email
        return endorsementRepository.save(endorsement);
    }

    @Override
    public List<Endorsement> findAllPendingEndorsementRequest(int page, int items) throws CooperaException {
        String endorserId = AppUtils.retrieveMemberId();
        Member member = memberService.findMemberById(endorserId);
        System.out.println(member.getEmail() + "this is the member email");
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        Page<Endorsement> endorsements = endorsementRepository.findEndorsementByEndorserEmailAndEndorsementStatus(member.getEmail(), EndorsementStatus.PENDING, pageable);
        List<Endorsement> endorsementList = endorsements.getContent();
       return endorsementList.stream().toList();
    }

    @Override
    public List<Endorsement> findAllAcceptedEndorsementRequest(int page, int items) throws CooperaException {
        String endorserId = AppUtils.retrieveMemberId();
        Member member = memberService.findMemberById(endorserId);
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        Page<Endorsement> endorsements = endorsementRepository.findEndorsementByEndorserEmailAndEndorsementStatus(member.getEmail(), EndorsementStatus.ACCEPT, pageable);
        List<Endorsement> endorsementList = endorsements.getContent();
        return endorsementList.stream().toList();
    }

    @Override
    public List<Endorsement> findAllRejectedEndorsementRequest(int page, int items) throws CooperaException {
        String endorserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberService.findMemberById(endorserId);
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        Page<Endorsement> endorsements = endorsementRepository.findEndorsementByEndorserEmailAndEndorsementStatus(member.getEmail(), EndorsementStatus.REJECT, pageable);
        List<Endorsement> endorsementList = endorsements.getContent();
        return endorsementList.stream().toList();
    }
}
