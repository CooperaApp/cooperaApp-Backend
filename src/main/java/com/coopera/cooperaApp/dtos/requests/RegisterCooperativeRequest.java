package com.coopera.cooperaApp.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegisterCooperativeRequest {

    @NotBlank(message = "Cooperative name is required.")
    private String name;

    @NotBlank
    private String logo;

    @NotBlank(message = "Company name is required.")
    @Size
    private String companyName;

    @NotBlank(message = "rcNumber is required.")
    private String rcNumber;

    @NotBlank(message = "Company address is required.")
    private String address;

    private String password;


}
