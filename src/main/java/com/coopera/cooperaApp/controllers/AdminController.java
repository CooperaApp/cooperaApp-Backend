package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @PostMapping("/generateLink")

    public ResponseEntity<ApiResponse<?>> generate(@RequestBody InvitationLinkRequest email){

        try {
            var response =   adminService.generateInvitationLink(email);
            System.out.println(email);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder().success(true).message((String) response).build());
        } catch (CooperaException e) {
            return ResponseEntity.badRequest().
                    body(ApiResponse.builder().message("Unable to send Mail").success(false).build());
        }
    }


}
