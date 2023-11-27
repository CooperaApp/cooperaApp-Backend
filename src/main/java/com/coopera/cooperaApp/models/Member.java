package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Member {

    private String id;
    private String firstName;
    private String lastName;
    private String department;
    private String companyId;
    private String photo;
    private String position;
    private String email;
  //  @Enumerated(EnumType.STRING)
    private List<Role> roles  = new ArrayList<>();
    private String password;
    private String phoneNumber;
  //  @OneToMany
 //   @JoinColumn(name = "savings_id")
  //  private List<Savings> savings;
 //   @OneToOne
    private Account account;
 //   @OneToOne
    private Loan loan;
    private BigDecimal balance;
}
