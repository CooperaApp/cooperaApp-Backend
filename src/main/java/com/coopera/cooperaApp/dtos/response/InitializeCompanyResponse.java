package com.coopera.cooperaApp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class InitializeCompanyResponse {
    private String rcNumber;
    private String name;
    private String cooperativeId;
}
