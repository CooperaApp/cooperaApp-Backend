package com.coopera.cooperaApp.dtos.response;

import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.models.Account;
import com.coopera.cooperaApp.models.Loan;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class MemberResponse {
    private List<Role> role;
    private String id;
    private String cooperativeId;
    private String firstName;
    private String lastName;
    private String department;
    private String photo;
    private String position;
    private String email;
    private String phoneNumber;
    private String loanEligibilityStatus;
}



