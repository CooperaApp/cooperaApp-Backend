package com.coopera.cooperaApp.services.loanServices;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.enums.EndorsementResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.EndorsementRequest;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.EndorsementRepository;
import com.coopera.cooperaApp.repositories.LoanRepository;
import com.coopera.cooperaApp.services.member.MemberService;
import com.coopera.cooperaApp.utilities.AppUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanEligibilityImpl implements LoanEligibility{

    private EndorsementRepository endorsementRepository;

    private MemberService memberService;


    @Override
    public Boolean endorseMember(String requestEmail, String endorsementRequestId) throws CooperaException {
        String endorserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member endorser = memberService.findMemberById(endorserId);
        if (checkIfEndorserIsEligible(endorser)){
        EndorsementRequest foundEndorsementRequest = endorsementRepository.findById(endorsementRequestId).
                orElseThrow(() -> new CooperaException("Not found"));
        foundEndorsementRequest.setEndorsementResponse(EndorsementResponse.ACCEPT);
        endorsementRepository.save(foundEndorsementRequest);
        }
        return null;
    }

    private boolean checkIfEndorserIsEligible(Member member){
        return member.getBalance().compareTo(AppUtils.balanceRequiredToEndorse) > 0;
    }

    @Override
    public String sendEndorsementRequest(LoanRequest loanRequest, String endorserEmail) throws CooperaException {
        String endorseeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member requester = memberService.findMemberById(endorseeId);
        Member endorser = memberService.findMemberById(endorserEmail);
        EndorsementRequest endorsementRequest = EndorsementRequest.builder().
                endorserEmail(endorser.getEmail()).
                loanRequest(loanRequest).
                requesterName(requester.getFirstName() + " " + requester.getLastName()).
                requesterEmail(requester.getEmail()).
                requesterPhoto(requester.getPhoto()).
                requesterDepartment(requester.getDepartment()).
                requesterPhoneNumber(requester.getPhoneNumber()).build();
        endorsementRepository.save(endorsementRequest);
        //Notify by email
        return "Endorsement Sent Successfully";
    }
}
