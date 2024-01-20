package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.InvestmentCategory;
import com.coopera.cooperaApp.enums.InvestmentType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Investment {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String cooperativeId;
    private InvestmentType investmentType;
    private InvestmentCategory investmentCategory;

}
