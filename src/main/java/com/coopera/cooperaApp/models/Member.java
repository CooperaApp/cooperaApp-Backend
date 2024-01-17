package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class Member {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String cooperativeId;
    private String firstName;
    private String lastName;
    private String department;
    private String companyId;
    private String photo;
    private String position;
    private String email;
    private List<Role> roles = new ArrayList<>();
    private String password;
    private String phoneNumber;
    @OneToOne
    private Account account;
    @OneToOne
    private Loan loan;
    private BigDecimal balance;
}
