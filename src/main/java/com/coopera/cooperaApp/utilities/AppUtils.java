package com.coopera.cooperaApp.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

public class AppUtils {
    public static final String MEMBER_INVITATION_HTML_TEMPLATE_LOCATION = "/memberInvitationTemplate.txt";
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

    public static String retrieveCooperativeId(){
        String cooperativeId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return cooperativeId.substring(1, cooperativeId.length() - 1);
    }

    public static String retrieveMemberId(){
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
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
