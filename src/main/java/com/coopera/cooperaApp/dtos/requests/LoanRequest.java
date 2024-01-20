package com.coopera.cooperaApp.dtos.requests;

import com.coopera.cooperaApp.models.LoanDuration;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequest {
    private String memberId;
    private String description;

    private LoanDuration loanDuration;
    private BigDecimal amount;
}
