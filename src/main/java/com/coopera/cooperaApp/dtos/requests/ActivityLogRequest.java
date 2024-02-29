package com.coopera.cooperaApp.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class ActivityLogRequest {

    private String activityName;
    private String memberEmail;
    private BigDecimal amount;
}
