package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.LoanRequest;
import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.dtos.response.LoanResponse;
import com.coopera.cooperaApp.enums.LoanStatus;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.exceptions.LoanException;
import com.coopera.cooperaApp.models.Loan;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.loanServices.LoanEligibility;
import com.coopera.cooperaApp.services.loanServices.LoanService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final LoanEligibility loanEligibility;

    @PostMapping("requestLoan")
    public ResponseEntity<ApiResponse<?>> save(@RequestBody LoanRequest loanRequest) {
        try {
            Loan response = loanService.requestLoan(loanRequest, memberService);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(LOAN_REQUEST_SUBMITTED)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), LoanResponse.class))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @PatchMapping("approveLoanRequest/{loanId}")
    public ResponseEntity<ApiResponse<?>> approveLoanRequest(
            @PathVariable String loanId
    ) {
        try {
            Loan response = loanService.updateSavingsStatus(loanId, LoanStatus.ONGOING, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_STATUS_UPDATED)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), LoanResponse.class))
                    .success(true)
                    .build());
        } catch (LoanException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findById/{loanId}")
    public ResponseEntity<ApiResponse<?>> findById(@PathVariable String loanId) {
        try {
            Loan response = loanService.findById(loanId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), LoanResponse.class))
                    .success(true)
                    .build());
        } catch (LoanException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByMemberId")
    public ResponseEntity<ApiResponse<?>> findByMemberId() {
        try {
            List<Loan> response = loanService.findByMemberId(memberService);
            TypeReference<List<LoanResponse>> typeReference = new TypeReference<List<LoanResponse>>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (LoanException | CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByMemberIdAndStatus/{loanStatus}")
    public ResponseEntity<ApiResponse<?>> findByMemberIdAndStatus(@PathVariable LoanStatus loanStatus) {
        try {
            List<Loan> response = loanService.findByMemberIdAndStatus(loanStatus, memberService);
            TypeReference<List<LoanResponse>> typeReference = new TypeReference<List<LoanResponse>>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (LoanException | CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByCooperativeId")
    public ResponseEntity<ApiResponse<?>> findByCooperativeId() {
        try {
            List<Loan> response = loanService.findByCooperativeId(cooperativeService);
            TypeReference<List<LoanResponse>> typeReference = new TypeReference<List<LoanResponse>>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (LoanException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("findByCooperativeIdAndStatus/{loanStatus}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeIdAndStatus(@PathVariable LoanStatus loanStatus) {
        try {
            List<Loan> response = loanService.findByCooperativeIdAndStatus(loanStatus, cooperativeService);
            TypeReference<List<LoanResponse>> typeReference = new TypeReference<List<LoanResponse>>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(LOAN_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (LoanException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("endorseMember/{endorsementRequestId}")
    public ResponseEntity<ApiResponse<?>> endorseMember(@PathVariable String endorsementRequestId){
        try {
            var response =  loanEligibility.endorseMember(endorsementRequestId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message(response).data("").success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("rejectMember/{endorsementRequestId}")
    public ResponseEntity<ApiResponse<?>> rejectMember(@PathVariable String endorsementRequestId){
        try {
            var response =  loanEligibility.rejectMember(endorsementRequestId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message(response).data("").success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @PostMapping("findAllPendingEndorsementRequests/{page}/{items}")
    public ResponseEntity<ApiResponse<?>> findAllPendingEndorsementRequests(@PathVariable int page, @PathVariable int items){
        try {
            var response =  loanEligibility.findAllPendingEndorsementRequest(page, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message("").data(response).success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }



    @PostMapping("findAllAcceptedEndorsementRequests/{page}/{items}")
    public ResponseEntity<ApiResponse<?>> findAllAcceptedEndorsementRequests(@PathVariable int page, @PathVariable int items){
        try {
            var response =  loanEligibility.findAllAcceptedEndorsementRequest(page, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message("").data(response).success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @PostMapping("findAllRejectedEndorsementRequests/{page}/{items}")
    public ResponseEntity<ApiResponse<?>> findAlRejectedEndorsementRequests(@PathVariable int page, @PathVariable int items){
        try {
            var response =  loanEligibility.findAllRejectedEndorsementRequest(page, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message("").data(response).success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }





}
