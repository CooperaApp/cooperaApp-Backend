package com.coopera.cooperaApp.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

@Slf4j

public class AppUtils {
    public static final String VERIFY_ACCOUNT = """
            Hi %s,
            
            Verify your account and enjoy the best experience with Coopera.
            
            %s
            
            Click on the link above to verify your account.
           """;
    public static final String VERIFY_MEMBER_ACCOUNT = """
            Hi %s,
            
            Verify your account and enjoy the best experience.
            
            %s
            
            Click on the link above to verify your account.
           """;
    public static final String PASSWORD_RESET_FAILED="Password Reset Failed";
    public static final String PASSWORD_RESET_SUCCESSFUL = "Password reset has been successful";
    public static final String FRONTEND_URL = "http://localhost:5173/";
   public static final String COOPERATIVE_WITH_ID_NOT_FOUND = "Cooperative with ID %s not found";
    public static final String MEMBER_WITH_ID_NOT_FOUND = "Member with ID %s not found";
   public static final String COOPERATIVE_UPDATE_FAILED= "Cooperative update failed";
    public static final String COOPERATIVE_UPDATE_SUCCESSFUL= "Cooperative update successfully";
    public static final String ACCOUNT_VERIFICATION_SUBJECT = "Verify your account";
    public static final String INVALID_COOPERATIVE_EMAIL = "Cooperative with email %s does not exist";
    public static final String INVALID_MEMBER_EMAIL = "Member with email %s does not exist";
    public static final String ACCOUNT_VERIFICATION_SENT= "Account verification mail sent successfully";
   public static final String MEMBER_INVITATION_HTML_TEMPLATE_LOCATION = "/memberInvitationTemplate.txt";
    public static final String ACCOUNT_VERIFICATION_HTML_TEMPLATE_LOCATION = "/accountVerificationTemplate.txt";
    public static final String INVITATION_MAIL_SUBJECT = "Invitation to Join %s";
    public static final String SAVINGS_NOT_FOUND = "Savings with ID %s not found";
    public static final String SAVINGS_DATA_FOUND = "Savings data found";
    public static final String LOAN_DATA_FOUND = "Loan data found";
    public static final String LOAN_NOT_FOUND = "Loan with ID %s not found";
    public static final String LOAN_REQUEST_SUBMITTED = "Loan request submitted";
    public static final String LOAN_STATUS_UPDATED = "Loan status updated";
    public static final String DATA_RETRIEVED = "Data retrieved successfully";
    public static final String SAVINGS_POSTED = "Savings posted successfully";

    public static final String NOT_ELIGIBLE_FOR_LOAN  = "You are not Eligible for this loan";

    public static final BigDecimal balanceRequiredToEndorse = BigDecimal.valueOf(50_000L);
    public static final String INTEREST_CANNOT_BE_CALCULATED = "Interest cannot be calculated. Please, update the cooperative's interest rate";

    public static String retrieveCooperativeEmail(){
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return cooperativeId.substring(1, cooperativeId.length() - 1);
    }

    public static String retrieveMemberEmail(){
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println("let me just s");
        return memberId.substring(1, memberId.length() - 1);
    }
    public static String retrieveCooperativeIdForAMember(){
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String memId = memberId.substring(1, memberId.length() - 1);
        return extractCooperativeIdFromMemberId(memId);
    }



    public static String extractCooperativeIdFromMemberId(String memberId) {
        String[] newId = memberId.split("/");
        StringBuilder cooID = new StringBuilder();
        int indexOfLastElementInArray = 2;
        for (int i = 0; i < newId.length - 1; i++) {
            if (i == indexOfLastElementInArray) cooID.append(newId[i]);
            else cooID.append(newId[i]).append("/");
        }
        return cooID.toString();
    }

    public static Pageable buildPageRequest(int page, int items){
        if (page<=1) page=1;
        if (items<=0) items = 10;
        page-=1;
        return PageRequest.of(page, items);
    }
}
