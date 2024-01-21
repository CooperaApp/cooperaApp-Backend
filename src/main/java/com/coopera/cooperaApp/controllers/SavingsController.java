package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.dtos.response.SavingsCollection;
import com.coopera.cooperaApp.dtos.response.SavingsResponse;
import com.coopera.cooperaApp.enums.SavingsStatus;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.SavingsLog;
import com.coopera.cooperaApp.services.SavingsServices.SavingsService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import com.coopera.cooperaApp.services.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.coopera.cooperaApp.utilities.AppUtils.SAVINGS_DATA_FOUND;
import static com.coopera.cooperaApp.utilities.AppUtils.SAVINGS_POSTED;

@RestController
@RequestMapping("/api/v1/savings/")
@AllArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;
    private final MemberService memberService;
    private final CooperativeService cooperativeService;
    private final ObjectMapper objectMapper;

    @PostMapping("save")
    public ResponseEntity<ApiResponse<?>> save(@RequestBody SaveRequest saveRequest) {
        try {
            SavingsLog response = savingsService.saveToCooperative(saveRequest, memberService);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(SAVINGS_POSTED)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), SavingsResponse.class))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findById/{savingsId}")
    public ResponseEntity<ApiResponse<?>> findById(@PathVariable Integer savingsId) {
        try {
            SavingsLog response = savingsService.findById(savingsId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), SavingsResponse.class))
                    .success(true)
                    .build());
        } catch (CooperaException |JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findByMemberId")
    public ResponseEntity<ApiResponse<?>> findByMemberId() {
        try {
            List<SavingsLog> response = savingsService.findByMemberId(memberService);
            TypeReference<List<SavingsResponse>> typeReference = new TypeReference<>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("findByMemberIdAndStatus/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> findByMemberIdAndStatus(@PathVariable SavingsStatus savingsStatus) {
        try {
            List<SavingsLog> response = savingsService.findByMemberIdAndStatus(savingsStatus, memberService);
            TypeReference<List<SavingsResponse>> typeReference = new TypeReference<>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @GetMapping("findByCooperativeId")
    public ResponseEntity<ApiResponse<?>> findByCooperativeId() {
        try {
            List<SavingsLog> response = savingsService.findByCooperativeId(cooperativeService);
            TypeReference<List<SavingsResponse>> typeReference = new TypeReference<>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }


    @GetMapping("findByCooperativeIdAndStatus/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> findByCooperativeIdAndStatus(@PathVariable SavingsStatus savingsStatus) {
        try {
            List<SavingsLog> response = savingsService.findByCooperativeIdAndStatus(savingsStatus, cooperativeService);
            TypeReference<List<SavingsResponse>> typeReference = new TypeReference<>() {
            };
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), typeReference))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }



    @PatchMapping("updateSavingsStatus/{savingsId}/{savingsStatus}")
    public ResponseEntity<ApiResponse<?>> updateSavingsStatus(@PathVariable String savingsId, @PathVariable SavingsStatus savingsStatus) {
        try {
            SavingsLog response = savingsService.updateSavingsStatus(savingsId, savingsStatus);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .message(SAVINGS_DATA_FOUND)
                    .data(objectMapper.readValue(objectMapper.writeValueAsString(response), SavingsResponse.class))
                    .success(true)
                    .build());
        } catch (CooperaException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

}
