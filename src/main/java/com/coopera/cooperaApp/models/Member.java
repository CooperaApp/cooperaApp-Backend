package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String firstName;
    private String lastName;
    private String department;
    private String companyId;
    private String photo;
    private String position;
    private String email;
    @Enumerated(EnumType.STRING)
    private List<Role> roles;
    private String password;
    private String phoneNumber;
    @OneToMany
    @JoinColumn(name = "savings_id")
    private List<Savings> savings;
    @OneToOne
    private Account account;
    @OneToOne
    private Loan loan;
    private BigDecimal balance;
}
