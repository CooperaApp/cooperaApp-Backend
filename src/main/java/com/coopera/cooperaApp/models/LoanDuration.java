package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.DurationPeriodType;
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
public class LoanDuration {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Long period;
    private DurationPeriodType durationPeriodType;

}
