package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.dtos.response.ApiResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.admin.AdminService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
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
    private CooperativeService cooperativeService;

    @PostMapping("/generateLink")
    public ResponseEntity<?> generate(@RequestBody InvitationLinkRequest requests) throws CooperaException {
        return new ResponseEntity<>(adminService.generateInvitationLink(requests, cooperativeService), HttpStatus.CREATED);

    }

    @PostMapping("/testing")
    public String test() {
        return "This is working";
    }


}
