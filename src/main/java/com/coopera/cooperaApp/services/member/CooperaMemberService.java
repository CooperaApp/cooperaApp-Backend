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
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.repositories.SavingsLogRepository;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.utilities.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.coopera.cooperaApp.utilities.AppUtils.retrieveCooperativeId;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CooperativeService cooperativeService;
    public static final String JWT_SECRET = "${jwt.secret}";

    public MemberResponse registerMember(RegisterMemberRequest request) throws CooperaException {
        Map<String, Claim> claims = extractClaimsFromToken(request.getToken());
        Claim memberId = claims.get("memberId");
        Map<String, Claim> claimMap = extractClaimsFromToken(request.getToken());
        Claim cooperativeId = claimMap.get("cooperativeId");
        request.setCooperativeId(cooperativeId.asString());
        checkIfMemberExistByEmail(request.getEmail());
        Member newMember = initializeNewMember(request);
        var savedMember = memberRepository.save(newMember);
        if (savedMember.getId() == null) throw new CooperaException("Member Registration failed");
        Optional<Cooperative> optionalCooperative = cooperativeService.findById(cooperativeId.asString());
        Cooperative cooperative = optionalCooperative.orElseThrow(() -> new CooperaException(String.format("Cooperative with %s id not found", cooperativeId)));
        cooperativeService.save(cooperative);
        log.info(memberRepository.findAll().size() + "this is all members");
        return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " " + savedMember.getLastName()).build();
    }


    public MemberResponse setMemberRoleToAdmin(String id) throws CooperaException {
        var foundMember = memberRepository.findById(id);
        foundMember.orElseThrow(() -> new CooperaException("Could not find member with " + id));
        foundMember.get().getRoles().add(Role.ADMIN);
        var savedMember = memberRepository.save(foundMember.get());
        return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).name(savedMember.getFirstName() + " " + savedMember.getLastName()).build();
    }

    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }

    @Override
    public MemberResponse findById(String memberId) throws CooperaException {
        var foundMember = memberRepository.findById(memberId);
        if (foundMember.isEmpty()) {
            throw new CooperaException("Member with id " + memberId + " not found");
        }
        return MemberResponse.builder().id(foundMember.get().getId()).cooperativeId(foundMember.get().getCooperativeId()).role(foundMember.get().getRoles()).name(foundMember.get().getFirstName() + " " + foundMember.get().getLastName()).build();

    }

    @Override
    public Member findMemberById(String memberId) throws CooperaException {
        return memberRepository.findById(memberId).orElseThrow(() -> new CooperaException("Member Not Found"));
    }

    @Override
    public List<MemberResponse> findAllMembersByCooperativeId(int page, int items) {
        String cooperativeId = retrieveCooperativeId();
        Pageable pageable = AppUtils.buildPageRequest(page, items);
        System.out.println("Items::>> "+items);
        Page<Member> memberPage = memberRepository.findAllByCooperativeId(cooperativeId, pageable);
        List<Member> members = memberPage.getContent();
        return members.stream().map(CooperaMemberService::buildMemberResponse).toList(); }

    @Override
    public List<Member> findAllMembersWithoutPagination() {
        return memberRepository.findAll();
    }

    @Override
    public Long getNumberOfMembersByCooperativeId(String cooperativeId) {
        return memberRepository.countAllByCooperativeId(cooperativeId);
    }

    private String extractCooperativeName(String id) throws CooperaException {
        var cooperative = cooperativeService.findById(id);
        Cooperative foundCooperative = cooperative.orElseThrow(() -> new CooperaException("Cooperative Not found"));
        return foundCooperative.getName();
    }
    private Member initializeNewMember(RegisterMemberRequest request) {
        Map<String, Claim> claims = extractClaimsFromToken(request.getToken());
        Claim memberId = claims.get("memberId");
        Member newMember = new Member();
        newMember.setFirstName(request.getFirstName());
        newMember.setId(memberId.asString());
        newMember.setCooperativeId(request.getCooperativeId());
        newMember.setLastName(request.getLastName());
        newMember.setEmail(request.getEmail());
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setPassword(passwordEncoder.encode(request.getPassword()));
        newMember.setPosition(request.getPosition());
        newMember.setPhoneNumber(request.getPhoneNumber());
        newMember.getRoles().add(Role.MEMBER);
        return newMember;
    }

    private void checkIfMemberExistByEmail(String emailAddress) throws CooperaException {
        Optional<Member> existingMember = memberRepository.findByEmail(emailAddress);
        if (existingMember.isPresent()) {
            throw new CooperaException("Member with this email already exists");
        }
    }

    private Map<String, Claim> extractClaimsFromToken(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaims();
    }

    private DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC512(JWT_SECRET.getBytes()))
                .build().verify(token);
    }

    private static MemberResponse buildMemberResponse(Member member){
        return MemberResponse.builder().email(member.getEmail())
                .photo(member.getPhoto())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .department(member.getDepartment())
                .position(member.getPosition())
                .id(member.getId()).build();
    }

}
