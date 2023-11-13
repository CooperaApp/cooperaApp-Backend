package com.coopera.cooperaApp.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCompanyRequest {
    @NotBlank(message = "Company name is required.")
    @Size
    private String companyName;

    @NotBlank(message = "rcNumber is required.")
    private String rcNumber;

    @NotBlank(message = "Company address is required.")
    private String companyAddress;

    private RegisterCooperativeRequest cooperativeRequest;

    private RegisterMemberRequest memberRequest;

}



