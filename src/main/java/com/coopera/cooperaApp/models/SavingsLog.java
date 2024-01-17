package com.coopera.cooperaApp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsLog {
    @Id
    private Integer id;

    private String cooperativeName;

    private String cooperativeId;

    private BigDecimal amountSaved;

    private LocalDateTime timeSaved;

    private String memberName;

    private String memberId;

}
