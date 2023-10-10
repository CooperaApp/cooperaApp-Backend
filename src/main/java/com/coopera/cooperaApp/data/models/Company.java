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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String rcNumber;
    private String address;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cooperative cooperative;

}
