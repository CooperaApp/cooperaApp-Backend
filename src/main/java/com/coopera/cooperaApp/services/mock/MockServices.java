package com.coopera.cooperaApp.services.mock;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.dtos.responses.ApiResponse;
import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.models.Member;
import com.coopera.cooperaApp.repositories.MemberRepository;
import com.coopera.cooperaApp.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockServices {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public ApiResponse<?> adminRegisterMock(String message) {
        System.out.println("1");
        Member member = Member.builder().email("alayandezainab64@gmail.com").password("password").firstName("Coopera Cooperative").build();
        System.out.println("2");
        Member savedMember = memberRepository.save(member);
        System.out.println("3");
        String token = jwtUtil.generateAccessToken(savedMember, Role.ADMIN);
        System.out.println("4");
        return ApiResponse.builder().message(message).status(true).data(token).build();
    }

    public ApiResponse<?> testTokenMock(String token) {
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        decodedJWT.getClaim("");
        return ApiResponse.builder().message("Token Validated").status(true).data(decodedJWT).build();
    }
}
