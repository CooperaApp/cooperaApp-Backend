package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.PartneringCategory;

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
public class PartneringCompany {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String cooperativeId;
    private String name;
    private String rcNumber;
    private PartneringCategory partneringCategory;
    private String address;
    private String number;
    private String email;
}
