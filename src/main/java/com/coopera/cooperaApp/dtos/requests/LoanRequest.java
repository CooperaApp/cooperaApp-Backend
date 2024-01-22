package com.coopera.cooperaApp.dtos.requests;

import com.coopera.cooperaApp.models.LoanDuration;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    @Id
    private String id;
    private String description;
    private LoanDuration loanDuration;
    private BigDecimal amount;
}
