
package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.ForgotPasswordRequest;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.CooperativeDashboardStatistic;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.requests.UpdateCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.dtos.response.CooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@RestController
@RequestMapping("/api/v1/cooperative")
@AllArgsConstructor
public class CooperativeController {
    private final CooperativeService cooperativeService;
    private final MemberService memberService;
    private final SavingsService savingsService;
    private final LoanService loanService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerCooperative(@RequestBody RegisterCooperativeRequest request) {
        try {
            var response = cooperativeService.registerCooperative(request, memberService);
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

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassWord(@RequestBody ForgotPasswordRequest request) {
        try {
            var response = cooperativeService.forgotPassword(request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(ACCOUNT_VERIFICATION_SENT)
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

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassWord(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            var response = cooperativeService.resetPassword(passwordResetRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(PASSWORD_RESET_SUCCESSFUL)
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

    @GetMapping("/getDashboardStatistics")
    public ResponseEntity<?> getDashboardStatistics() {
        CooperativeDashboardStatistic response = cooperativeService.getDashboardStatistics(savingsService, loanService);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .message(DATA_RETRIEVED)
                .success(true)
                .data(response)
                .build());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCooperativeInfo(@RequestBody UpdateCooperativeRequest updateRequest) throws Exception {
        System.out.println("Controller request: " + updateRequest);
        try {
            CooperativeResponse response = cooperativeService.updateCooperativeDetails(updateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(COOPERATIVE_UPDATE_SUCCESSFUL)
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

