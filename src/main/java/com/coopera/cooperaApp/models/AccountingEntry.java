package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.SavingsMode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AccountingEntry {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double interestRate;
    private Long loanEligibilityRate;
    private BigDecimal minimumSavingsAmount;
    private SavingsMode savingsMode;

}
