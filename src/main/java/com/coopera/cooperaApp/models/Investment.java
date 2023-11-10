package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.InvestmentCategory;
import com.coopera.cooperaApp.enums.InvestmentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Investment {
    private String id;

    private InvestmentType investmentType;
    private InvestmentCategory investmentCategory;

}
