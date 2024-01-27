package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@ToString
public class Member {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;
    private String cooperativeId;
    private String firstName;
    private String lastName;
    private String department;
    private String companyId;
    private String photo;
    private String position;
    private String email;
   // @ElementCollection(targetClass = Role.class)
 //   @Enumerated(EnumType.STRING)
 //  @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();
    private String password;
    private String phoneNumber;
    private String loanEligibilityStatus;
    @OneToOne
    private Account account;
    @OneToOne
    private Loan loan;
    private BigDecimal balance;
}
