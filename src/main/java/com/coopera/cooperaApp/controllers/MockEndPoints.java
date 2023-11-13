package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.security.JwtUtil;
import com.coopera.cooperaApp.services.mock.MockServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MockEndPoints {

    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private MockServices mockServices = new MockServices(jwtUtil, memberRepository);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "v1/admin/register-cooperative-mock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> mockRegister(@RequestParam("message") String message) {
        return ResponseEntity.status(HttpStatus.OK).body(mockServices.adminRegisterMock(message));
    }


    @RequestMapping(
            method = RequestMethod.POST,
            value = "v1/admin/test-token-mock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> testToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(mockServices.testTokenMock(token));
    }
}
