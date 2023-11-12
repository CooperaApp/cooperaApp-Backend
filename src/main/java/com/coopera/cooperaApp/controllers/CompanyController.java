package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerCompany(@RequestBody RegisterCompanyRequest request) {
        try {
            var response = companyService.registerCompany(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message("Company registration successful")
                    .success(true)
                    .data(response)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build());
        }
    }
}
