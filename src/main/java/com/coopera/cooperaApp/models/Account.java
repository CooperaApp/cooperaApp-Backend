package com.coopera.cooperaApp.models;


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
public class Account {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String bankName;
    private String accountName;

}
