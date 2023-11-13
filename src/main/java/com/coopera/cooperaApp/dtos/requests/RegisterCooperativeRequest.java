package com.coopera.cooperaApp.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegisterCooperativeRequest {

    @NotBlank
    private String cooperativeName;

    @NotBlank
    private String logo;

}
