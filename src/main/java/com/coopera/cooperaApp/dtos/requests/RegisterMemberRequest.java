package com.coopera.cooperaApp.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMemberRequest {
    @NotBlank
    @Size(min = 2, message = "First name should have at least 2 letters.")
    private String firstName;

    @NotBlank
    @Size(min = 2, message = "Last name should have at least 2 letters.")
    private String lastName;


    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit.")
    private String password;

    @NotBlank
    private String phoneNumber;


    private String token;


}
