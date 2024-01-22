
package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.ForgotPasswordRequest;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cooperative")
@AllArgsConstructor
public class CooperativeController {
    private final CooperativeService cooperativeService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerCooperative(@RequestBody RegisterCooperativeRequest request) {
        try {
            var response = cooperativeService.registerCooperative(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message("Company registration successful")
                    .status(true)
                    .data(response)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .status(false)
                    .build());
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassWord(@RequestBody ForgotPasswordRequest request){
        try {
            var response = cooperativeService.forgotPassword(request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message("Account verification mail successful")
                    .status(true)
                    .data(response)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .status(false)
                    .build());
        }
    }
    @PostMapping
    public ResponseEntity<?> resetPassWord(@RequestBody PasswordResetRequest passwordResetRequest){
        try {
            var response = cooperativeService.resetPassword(passwordResetRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message("Password Reset successfully")
                    .status(true)
                    .data(response)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .status(false)
                    .build());
        }
    }
}

