
package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.CooperativeDashboardStatistic;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.coopera.cooperaApp.utilities.AppUtils.DATA_RETRIEVED;

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

    @GetMapping("/getDashboardStatistics")
    public ResponseEntity<?> getDashboardStatistics() {
        CooperativeDashboardStatistic response = cooperativeService.getDashboardStatistics(savingsService, loanService);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .message(DATA_RETRIEVED)
                .success(true)
                .data(response)
                .build());
    }

}

