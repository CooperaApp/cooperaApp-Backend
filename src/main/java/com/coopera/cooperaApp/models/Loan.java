package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.LoanStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String memberId;
    private String memberName;
    private String cooperativeId;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateRequested;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateApproved;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateRejected;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;
    @OneToOne(cascade = CascadeType.ALL)
    private LoanDuration loanDuration;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Endorsement> endorsement = new ArrayList<>();
    private LoanStatus loanStatus;
    private BigDecimal repaymentAmount;
    private BigDecimal amount;

    @PrePersist
    void prePersist(){
        loanStatus = LoanStatus.PENDING;
        dateRequested = LocalDateTime.now();
    }
}
