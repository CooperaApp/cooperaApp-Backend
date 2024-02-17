package com.coopera.cooperaApp.dtos.requests;

import com.coopera.cooperaApp.models.AccountingEntry;
import com.coopera.cooperaApp.models.Company;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
public class UpdateCooperativeRequest {

    private String name;
    private String logo;
    private String companyName;
    private String address;
    private Double interestRate;
    private Long loanEligibilityRate;
}
