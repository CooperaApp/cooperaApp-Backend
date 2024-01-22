package com.coopera.cooperaApp.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class CooperativeDashboardStatistic {
    private BigDecimal accountBalance;
    private BigDecimal totalSavings;
    private BigDecimal loanDisbursed;
    private BigDecimal loanRepaid;
}
