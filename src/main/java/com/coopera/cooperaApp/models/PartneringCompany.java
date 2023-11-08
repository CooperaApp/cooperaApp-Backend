package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.PartneringCategory;
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
    private  String id;
    private String name;
    private String rcNumber;
    @Enumerated(EnumType.STRING)
    private PartneringCategory partneringCategory;
    private String address;
    private String number;
    private String email;
}
