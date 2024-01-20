package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.SAVINGS_DATA_FOUND;

@RestController
@RequestMapping("/api/v1/savings/")
@AllArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;
    private final MemberService memberService;
    private final CooperativeService cooperativeService;

    @PostMapping("save")
    public ResponseEntity<ApiResponse<?>> save(@RequestBody SaveRequest saveRequest) {
        try {
            var response = savingsService.saveToCooperative(saveRequest, memberService);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(response.getMessage())
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findById/{savingsId}")
    public ResponseEntity<ApiResponse<?>> findById(@PathVariable Integer savingsId) {
        try {
            SavingsLog response = savingsService.findById(savingsId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findByMemberId/{memberId}")
    public ResponseEntity<ApiResponse<?>> findByMemberId(@PathVariable String memberId) {
        try {
            List<SavingsLog> response = savingsService.findByMemberId(memberId, memberService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findByMemberIdAndStatus/{memberId}/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> findByMemberIdAndStatus(@PathVariable String memberId, @PathVariable SavingsStatus savingsStatus) {
        try {
            List<SavingsLog> response = savingsService.findByMemberIdAndStatus(memberId, savingsStatus, memberService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @GetMapping("findByCooperativeId/{cooperativeId}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeId(@PathVariable String cooperativeId) {
        try {
            List<SavingsLog> response = savingsService.findByCooperativeId(cooperativeId, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @GetMapping("findByCooperativeIdAndStatus/{cooperativeId}/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeIdAndStatus(@PathVariable String cooperativeId, @PathVariable SavingsStatus savingsStatus) {
        try {
            List<SavingsLog> response = savingsService.findByCooperativeIdAndStatus(cooperativeId, savingsStatus, cooperativeService);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }



    @PatchMapping("updateSavingsStatus/{savingsId}/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> updateSavingsStatus(@PathVariable String savingsId, @PathVariable SavingsStatus savingsStatus) {
        try {
            SavingsLog response = savingsService.updateSavingsStatus(savingsId, savingsStatus);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(response)
                    .success(true)
                    .build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

}
