package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.EndorsementStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Endorsement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String endorserEmail;
    private String requesterEmail;
    private EndorsementStatus endorsementStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateRequested;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;


    @PrePersist
    void prePersist(){
        endorsementStatus = EndorsementStatus.PENDING;
        dateRequested = LocalDateTime.now();
    }

}
