package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.enums.EndorsementResponse;
import com.coopera.cooperaApp.enums.LoanStatus;
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
public class EndorsementRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String endorserEmail;
    private String requesterName;
    private String requesterPhoto;
    private String requesterEmail;
    private String requesterPhoneNumber;
    private String requesterDepartment;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private EndorsementResponse endorsementResponse;
    private LocalDateTime dateRequested;
    private LoanRequest loanRequest;

    @PrePersist
    void prePersist(){
        endorsementResponse = EndorsementResponse.PENDING;
        dateRequested = LocalDateTime.now();
    }

}
