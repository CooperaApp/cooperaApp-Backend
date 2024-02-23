package com.coopera.cooperaApp.services.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberDashboardStatistic;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.security.JwtUtil;
import com.coopera.cooperaApp.services.Mail.MailService;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.utilities.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class CooperaMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CooperativeService cooperativeService;
    public final JwtUtil jwtUtil;

    public MemberResponse registerMember(RegisterMemberRequest request) throws CooperaException {
        Map<String, Claim> claimMap = extractClaimsFromToken(request.getToken());
        Claim cooperativeId = claimMap.get("cooperativeId");
        Claim email = claimMap.get("memberEmail");
        checkIfMemberExistByEmail(email.asString());
        Member newMember = initializeNewMember(request,email.asString(), cooperativeId.asString());
        var savedMember = memberRepository.save(newMember);
        if (savedMember.getId() == null) throw new CooperaException("Member Registration failed");
        Optional<Cooperative> optionalCooperative = cooperativeService.findById(cooperativeId.asString());
        Cooperative cooperative = optionalCooperative.orElseThrow(() -> new CooperaException(String.format("Cooperative with %s id not found", cooperativeId)));
        cooperativeService.save(cooperative);
        log.info(memberRepository.findAll().size() + "this is all members");
        return MemberResponse.builder().
                id(savedMember.getId()).
                firstName(savedMember.getFirstName()).
                lastName(savedMember.getLastName()).
                email(savedMember.getEmail()).
                role(savedMember.getRoles()).
                cooperativeId(savedMember.getCooperativeId()).
                build();
    }


    public MemberResponse setMemberRoleToAdmin(String id) throws CooperaException {
        var foundMember = memberRepository.findById(id);
        foundMember.orElseThrow(() -> new CooperaException("Could not find member with " + id));
        foundMember.get().getRoles().add(Role.ADMIN);
        var savedMember = memberRepository.save(foundMember.get());
        return MemberResponse.builder().id(savedMember.getId()).role(savedMember.getRoles()).firstName(savedMember.getFirstName()).lastName(savedMember.getLastName()).build();
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
        return MemberResponse.builder().id(foundMember.get().getId()).cooperativeId(foundMember.get().getCooperativeId()).role(foundMember.get().getRoles()).firstName(foundMember.get().getFirstName()).lastName(foundMember.get().getLastName()).build();

    }

    @Override
    public Member findMemberById(String memberId) throws CooperaException {
        return memberRepository.findById(memberId).orElseThrow(() -> new CooperaException("Member Not Found"));
    }

    @Override
    public Member findMemberByMail(String email) throws CooperaException {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CooperaException("Member Not Found"));
    }

    @Override
    public List<MemberResponse> findAllMembersByCooperativeId(int page, int items) {
        String cooperativeId = retrieveCooperativeEmail();
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

    @Override
    public MemberDashboardStatistic getMemberDashboardStatistic(SavingsService savingsService, LoanService loanService) {
        String memberId = retrieveMemberEmail();
        BigDecimal totalMemberSavings = savingsService.calculateTotalMemberSavings(memberId);
        BigDecimal totalMemberLoans = loanService.calculateTotalObtainedByMember(memberId);

        MemberDashboardStatistic memberDashboardStatistic = new MemberDashboardStatistic();
        memberDashboardStatistic.setTotalSavings(totalMemberSavings);
        memberDashboardStatistic.setTotalLoans(totalMemberLoans);

        return memberDashboardStatistic;
    }

    private String extractCooperativeName(String id) throws CooperaException {
        var cooperative = cooperativeService.findById(id);
        Cooperative foundCooperative = cooperative.orElseThrow(() -> new CooperaException("Cooperative Not found"));
        return foundCooperative.getName();
    }
    private Member initializeNewMember(RegisterMemberRequest request,String email, String cooperativeId) {
        Map<String, Claim> claims = extractClaimsFromToken(request.getToken());
        Claim memberId = claims.get("memberId");
        Member newMember = new Member();
        newMember.setFirstName(request.getFirstName());
        newMember.setId(memberId.asString());
        newMember.setCooperativeId(cooperativeId);
        newMember.setLastName(request.getLastName());
        newMember.setEmail(email);
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setPhoneNumber(request.getPhoneNumber());
        newMember.setPassword(passwordEncoder.encode(request.getPassword()));
        newMember.getRoles().add(Role.MEMBER);
        return newMember;
    }

    private void checkIfMemberExistByEmail(String emailAddress) throws CooperaException {
        var existingMember =findAllMembersWithoutPagination();
       var member= existingMember.stream().filter(c->c.getEmail().equals(emailAddress)).findAny().orElseThrow(() -> new CooperaException("Member with this email already exists"));

    }

    private Map<String, Claim> extractClaimsFromToken(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaims();
    }

    private DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()))
                .build().verify(token);
    }
    @Override
    public String forgotMemberPassword(String email) throws CooperaException {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new CooperaException(String.format(INVALID_MEMBER_EMAIL, email));
        }
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject(ACCOUNT_VERIFICATION_SUBJECT);
        emailDetails.setRecipient(member.get().getEmail());
        emailDetails.setMsgBody(String.format(VERIFY_ACCOUNT, member.get().getFirstName(), generateLink(member.get().getId())));
        return mailService.sendEmail(emailDetails);
    }
    private  String generateLink(String memberId) {
        return FRONTEND_URL+"reset-password?token=" +
                JWT.create()
                        .withIssuedAt(Instant.now())
                        .withClaim("memberId", memberId)
                        .withExpiresAt(Instant.now().plusSeconds(600L))
                        .sign(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()));
    }

    public String resetPassword(PasswordResetRequest passwordResetRequest) throws CooperaException {
        if(!Objects.equals(passwordResetRequest.getNewPassword(), passwordResetRequest.getConfirmPassword())) throw  new CooperaException("Password do not match!");
        String token = passwordResetRequest.getToken();
        String newPassword = passwordResetRequest.getNewPassword();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()))
                .build().verify(token);
        if (decodedJWT == null) throw new CooperaException(PASSWORD_RESET_FAILED);
        Claim claim = decodedJWT.getClaim("memberId");
        String id = claim.asString();
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new CooperaException(String.format(MEMBER_WITH_ID_NOT_FOUND,id)));
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return PASSWORD_RESET_SUCCESSFUL;
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