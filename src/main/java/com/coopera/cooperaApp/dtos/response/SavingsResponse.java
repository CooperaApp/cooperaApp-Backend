package com.coopera.cooperaApp.dtos.response;

import com.coopera.cooperaApp.enums.SavingsStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
public class SavingsResponse {

    private Integer id;

    private BigDecimal amountSaved;

    private LocalDateTime timeSaved;

    private String memberName;

    private SavingsStatus savingsStatus;
}
