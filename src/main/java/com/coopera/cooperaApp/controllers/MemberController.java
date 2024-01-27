package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.ForgotPasswordRequest;
import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.requests.SaveRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.coopera.cooperaApp.utilities.AppUtils.ACCOUNT_VERIFICATION_SENT;
import static com.coopera.cooperaApp.utilities.AppUtils.PASSWORD_RESET_SUCCESSFUL;

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
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassWord(@RequestBody ForgotPasswordRequest request){
        try {
            var response = memberService.forgotMemberPassword(request.getEmail());
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
    public ResponseEntity<?> resetPassWord(@RequestBody PasswordResetRequest passwordResetRequest){
        try {
            var response = memberService.resetPassword(passwordResetRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .message(PASSWORD_RESET_SUCCESSFUL)
                    .success
                            (true)
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
