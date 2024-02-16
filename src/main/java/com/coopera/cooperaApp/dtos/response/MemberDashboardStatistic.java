package com.coopera.cooperaApp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class MemberDashboardStatistic {
    private BigDecimal totalSavings;
    private BigDecimal totalLoans;
    private BigDecimal totalHiredPurchase;
    private BigDecimal withdrawals;
}
