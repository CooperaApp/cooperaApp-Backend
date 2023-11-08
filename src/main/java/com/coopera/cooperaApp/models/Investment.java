package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.InvestmentCategory;
import com.coopera.cooperaApp.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Enumerated(EnumType.STRING)
    private InvestmentType investmentType;
    @Enumerated(EnumType.STRING)
    private InvestmentCategory investmentCategory;

}
