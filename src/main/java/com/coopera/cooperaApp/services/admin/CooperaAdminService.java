package com.coopera.cooperaApp.services.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Cooperative;
import com.coopera.cooperaApp.security.JwtUtil;
import com.coopera.cooperaApp.services.Mail.MailService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.INVITATION_MAIL_SUBJECT;
import static com.coopera.cooperaApp.utilities.AppUtils.MEMBER_INVITATION_HTML_TEMPLATE_LOCATION;
import static com.coopera.cooperaApp.utilities.HtmlFileUtility.getFileTemplateFromClasspath;


@AllArgsConstructor
@Service
@Slf4j
public class CooperaAdminService implements AdminService{
    private final MemberService memberService;

    private final MailService mailService;
    private final JwtUtil jwtUtil;

    public static final String JWT_SECRET = "${jwt.secret}";
    @Override
    public Object generateInvitationLink(InvitationLinkRequest recipient, CooperativeService cooperativeService) throws CooperaException {
        String memberId = generateMemberId();
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String coopId = cooperativeId.substring(1, cooperativeId.length() - 1);
        List<String> requestList = recipient.getRecipientEmail();
        int successCount = 0;
        String link = generateInviteLink(memberId, coopId, jwtUtil.getSecret());
        successCount = sendInviteToRecipient(requestList, link, successCount, coopId, cooperativeService);
        return emailSenderResponse(successCount);
    }

    private static String generateInviteLink(String memberId, String cooperativeId, String secret) {
        return "localhost:3000/membersLogin?token=" + JWT.create().withIssuedAt(Instant.now()).
                withClaim("memberId", memberId).
                withClaim("cooperativeId", cooperativeId).
                withExpiresAt(Instant.now().plusSeconds(864000L))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    private int sendInviteToRecipient(List<String> requestList, String link, int successCount, String coopId, CooperativeService cooperativeService) throws CooperaException {
        System.out.println("Link::>> "+link);
        for (String recipientMail : requestList) {
            Cooperative cooperative = cooperativeService.findById(coopId).get();
            String template = getFileTemplateFromClasspath(MEMBER_INVITATION_HTML_TEMPLATE_LOCATION);
            String mailBody = String.format(template, cooperative.getName(), cooperative.getCompany().getCompanyName(), link);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(recipientMail);
            emailDetails.setMsgBody(mailBody);
            emailDetails.setSubject(String.format(INVITATION_MAIL_SUBJECT, cooperative.getName()));
            String response = mailService.mimeMessage(emailDetails);
            if (response.equals("success")) successCount++;
        }
        return successCount;
    }

    private Object emailSenderResponse(int successCount) throws CooperaException {
        if (successCount == 1) return "Invite Link successfully sent to recipient";
        else if (successCount > 1) return "Invite Link successfully sent to all recipients";
        else throw new CooperaException("Invite could not be sent");
    }

    private String generateMemberId(){
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String coopId = cooperativeId.substring(1, cooperativeId.length() - 1);
        var currentSizeOfMembersPlusOne = memberService.findAllMembersWithoutPagination().size() + 1;
        return coopId + "/" + currentSizeOfMembersPlusOne;
    }
}
