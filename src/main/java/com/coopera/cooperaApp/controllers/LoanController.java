package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.*;

@RestController
@RequestMapping("/api/v1/loans/")
@AllArgsConstructor
public class LoanController {


    private final LoanService loanService;
    private final MemberService memberService;
    private final CooperativeService cooperativeService;

    @PostMapping("requestLoan")
    public ResponseEntity<ApiResponse<?>> save(@RequestBody LoanRequest loanRequest) {
        try {
            Loan response = loanService.requestLoan(loanRequest, memberService);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(LOAN_REQUEST_SUBMITTED)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @PatchMapping("updateLoanStatus/{loanId}/{loanStatus}")
    public ResponseEntity<ApiResponse<?>> updateLoanStatus(
            @PathVariable String loanId, @PathVariable LoanStatus loanStatus
    ) {
        try {
            Loan response = loanService.updateSavingsStatus(loanId, loanStatus, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_STATUS_UPDATED)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findById/{loanId}")
    public ResponseEntity<ApiResponse<?>> findById(@PathVariable String loanId) {
        try {
            Loan response = loanService.findById(loanId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByMemberId/{memberId}")
    public ResponseEntity<ApiResponse<?>> findByMemberId(@PathVariable String memberId) {
        try {
            List<Loan> response = loanService.findByMemberId(memberId, memberService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException | CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByMemberIdAndStatus/{memberId}/{loanStatus}")
    public ResponseEntity<ApiResponse<?>> findByMemberIdAndStatus(@PathVariable String memberId, @PathVariable LoanStatus loanStatus) {
        try {
            List<Loan> response = loanService.findByMemberIdAndStatus(memberId, loanStatus, memberService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException | CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByCooperativeId/{cooperativeId}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeId(@PathVariable String cooperativeId) {
        try {
            List<Loan> response = loanService.findByCooperativeId(cooperativeId, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByCooperativeIdAndStatus/{cooperativeId}/{loanStatus}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeIdAndStatus(@PathVariable String cooperativeId, @PathVariable LoanStatus loanStatus) {
        try {
            List<Loan> response = loanService.findByCooperativeIdAndStatus(cooperativeId, loanStatus, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (LoanException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
}
