package com.coopera.cooperaApp.services.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.dtos.response.SavingsResponse;
import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.repositories.SavingsLogRepository;
import com.coopera.cooperaApp.services.cooperative.CooperaCoperativeService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaMemberService implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CooperativeService cooperativeService;
    private final SavingsLogRepository savingsLogRepository;
    public static final String JWT_SECRET = "${jwt.secret}";

    public MemberResponse registerMember(RegisterMemberRequest request) throws CooperaException {
        Map<String, Claim> claims = extractClaimsFromToken(request.getToken());
        Claim memberId = claims.get("memberId");
        Map<String, Claim> claimMap = extractClaimsFromToken(request.getToken());
        Claim cooperativeId = claimMap.get("cooperativeId");
        checkIfMemberExistByEmail(request);
        Member newMember = initializeNewMember(request);
        var savedMember = memberRepository.save(newMember);
      if (savedMember.getId() == null) throw new CooperaException("Member Registration failed");
      Optional<Cooperative> optionalCooperative = cooperativeService.findByCooperativeById(cooperativeId.asString());
       Cooperative cooperative = optionalCooperative.orElseThrow(() -> new CooperaException(String.format("Cooperative with %s id not found",cooperativeId)));
      cooperative.getMembersId().add(memberId.asString());
      cooperativeService.save(cooperative);
      return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " "+ savedMember.getLastName()).build();
    }


    public MemberResponse setMemberRoleToAdmin(String id) throws CooperaException {
       var foundMember  = memberRepository.findById(id);
       foundMember.orElseThrow(()->new CooperaException("Could not find member with " + id ));
       foundMember.get().getRoles().add(Role.ADMIN);
       var savedMember = memberRepository.save(foundMember.get());
       return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " "+ savedMember.getLastName()).build();
    }

    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }

    @Override
    public MemberResponse findById(String memberId) throws CooperaException {
      var  foundMember = memberRepository.findById(memberId);
        if (foundMember.isEmpty()) {
            throw new CooperaException("Member with id " + memberId +" not found");
        }
        return MemberResponse.builder().id(foundMember.get().getId()).role(foundMember.get().getRoles()).name(foundMember.get().getFirstName() + " "+ foundMember.get().getLastName()).build();

    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public SavingsResponse saveToCooperative(SaveRequest saveRequest ) throws CooperaException {
        if (!checkIfAmountToSaveIsValid(saveRequest.getAmountToSave())) throw new CooperaException("Invalid Amount");
        BigDecimal amount = new BigDecimal(saveRequest.getAmountToSave());
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String memId =memberId.substring(1, memberId.length() - 1);
        String cooperativeId = extractCooperativeIdFromMemberId(memId);
        SavingsLog newSavingsLog = SavingsLog.builder().amountSaved(amount).
                timeSaved(LocalDateTime.now()).
                cooperativeName(extractCooperativeName(cooperativeId)).memberName(extractMemberName(memId)).
                memberId(memberId).cooperativeId(cooperativeId).build();
        savingsLogRepository.save(newSavingsLog);
        return SavingsResponse.builder().message("Saved Successfully").build();
    }

    private String extractCooperativeName(String id) throws CooperaException {
        var cooperative = cooperativeService.findByCooperativeById(id);
        Cooperative foundCooperative = cooperative.orElseThrow(() -> new CooperaException("Cooperative Not found"));
        return foundCooperative.getName();
    }

    private String extractMemberName(String id) throws CooperaException {
        var response = findById(id);
        return response.getName();
    }

    public boolean checkIfAmountToSaveIsValid(String amountToSave){
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

    private String extractCooperativeIdFromMemberId(String memberId){
        String[] newId = memberId.split("/");
        StringBuilder cooID = new StringBuilder();
        int indexOfLastElementInArray = 2;
        for (int i = 0; i < newId.length-1; i++) {
            if (i == indexOfLastElementInArray) cooID.append(newId[i]);
            else cooID.append(newId[i]).append("/");
        }
        return cooID.toString();
    }

    private Member initializeNewMember(RegisterMemberRequest request) {
        Map<String, Claim> claims = extractClaimsFromToken(request.getToken());
        Claim memberId = claims.get("memberId");
        Member newMember = new Member();
        newMember.setFirstName(request.getFirstName());
        newMember.setId(memberId.asString());
        newMember.setLastName(request.getLastName());
        newMember.setEmail(request.getEmail());
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setPassword(passwordEncoder.encode(request.getPassword()));
        newMember.setPosition(request.getPosition());
        newMember.setPhoneNumber(request.getPhoneNumber());
        newMember.getRoles().add(Role.MEMBER);
        return newMember;
    }

    private void checkIfMemberExistByEmail(RegisterMemberRequest request) throws CooperaException {
        Optional<Member> existingMember = memberRepository.findByEmail(request.getEmail());
        if (existingMember.isPresent()) {
            throw new CooperaException("Member with this email already exists");
        }
    }

    private Map<String, Claim> extractClaimsFromToken (String token){
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaims();
    }

    private DecodedJWT validateToken(String token){
        return JWT.require(Algorithm.HMAC512(JWT_SECRET.getBytes()))
                .build().verify(token);
    }
}
