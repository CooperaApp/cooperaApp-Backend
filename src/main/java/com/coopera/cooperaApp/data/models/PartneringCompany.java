package com.coopera.cooperaApp.data.models;

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
public class PartneringCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String name;
    private String rcNumber;
    @Enumerated(EnumType.STRING)
    private  PartneringCategory partneringCategory;
    private String address;
    private String number;
    private String email;
}
