package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterMemberRequest registerMemberRequest)  {
        try {
            var response =  memberService.registerMember(registerMemberRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().
                    message("Member successfuly Created").data(response).success(true).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/findAllMembersByCooperativeId")
    public ResponseEntity<ApiResponse<?>> findAllMembersByCooperativeId(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int items
    )  {
        var response =  memberService.findAllMembersByCooperativeId(page, items);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder().
                message("Member successfuly Created").data(response).success(true).build());
    }



}
