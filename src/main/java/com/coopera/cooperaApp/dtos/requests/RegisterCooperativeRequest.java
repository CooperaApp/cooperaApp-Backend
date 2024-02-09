package com.coopera.cooperaApp.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegisterCooperativeRequest {

    @NotBlank(message = "Cooperative name is required.")
    private String name;



    @NotBlank(message = "Cooperative email is required.")
    @Email
    private String email;

    @NotBlank(message = "Company name is required.")
    @Size
    private String companyName;

    @NotBlank(message = "rcNumber is required.")
    private String rcNumber;


    private String password;


}
