package com.coopera.cooperaApp.services.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.security.JwtUtil;
import com.coopera.cooperaApp.security.SecurityUtils;
import com.coopera.cooperaApp.services.Mail.MailService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.coopera.cooperaApp.security.SecurityUtils.JWT_SECRET;

@AllArgsConstructor
@Service
public class CooperaAdminService implements AdminService{
    private final MemberService memberService;

    private final MailService mailService;

    public static final String JWT_SECRET = "${jwt.secret}";
    @Override
    public String generateInvitationLink(String recipient) throws CooperaException {
        String memberId = generateMemberId();
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();


        String link = "localhost:3000/membersLogin?token=" + JWT.create().withIssuedAt(Instant.now()).
                withClaim("memberId", memberId).
                withClaim("cooperativeId", cooperativeId).
                withExpiresAt(Instant.now().plusSeconds(864000L))
                .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipient);
        emailDetails.setMsgBody(link);
        emailDetails.setSubject("Please Login with the Link Within the Hour");
       String response =  mailService.sendEmail(emailDetails);

       if (response.equals("success")) return "Invitation Link send successfully";

       else throw new CooperaException(response);
    }

    private String generateMemberId(){
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        var currentSizeOfMembersPlusOne = memberService.findAllMembers().size() + 1;
        return cooperativeId + "/" + currentSizeOfMembersPlusOne;

    }
}
