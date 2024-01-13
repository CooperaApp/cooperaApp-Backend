package com.coopera.cooperaApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
public class SavingsLog {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String cooperativeName;

    private String cooperativeId;

    private BigDecimal amountSaved;

    private LocalDateTime timeSaved;

    private String memberName;

    private String memberId;

    public SavingsLog() {

    }
}
