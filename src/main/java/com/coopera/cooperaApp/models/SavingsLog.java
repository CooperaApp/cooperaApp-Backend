package com.coopera.cooperaApp.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document
@Builder
public class SavingsLog {
    @Id
    private String id;

    private String cooperativeName;

    private String cooperativeId;

    private BigDecimal amountSaved;

    private LocalDateTime timeSaved;

    private String memberName;

    private String memberId;

}
