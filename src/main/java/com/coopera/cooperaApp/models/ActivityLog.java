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
@ToString
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String memberName;
    private String activityName;
    private String cooperativeId;
    private String memberId;
    private BigDecimal amount;
    private LocalDateTime date;

    @PrePersist
    private void prePersist(){
        date = LocalDateTime.now();
    }
}


