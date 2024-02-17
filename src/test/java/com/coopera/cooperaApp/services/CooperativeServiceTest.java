//package com.coopera.cooperaApp.services;
//
//import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
//import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
//import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
//import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
//import com.coopera.cooperaApp.exceptions.CooperaException;
//import com.coopera.cooperaApp.services.cooperative.CooperaCoperativeService;
//import com.coopera.cooperaApp.services.cooperative.CooperativeService;
//import com.coopera.cooperaApp.services.member.MemberService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class CooperativeServiceTest {
//
//    @Autowired
//    private CooperativeService cooperativeService;
//
//    @Autowired
//    private  CooperaCoperativeService cooperaCoperativeService;
//    @Autowired
//    private MemberService memberService;
//    private RegisterCooperativeResponse response;
//
//    @BeforeEach
//    public void setUp() throws CooperaException {
//
//        RegisterMemberRequest member = new RegisterMemberRequest();
//        member.setEmail("speaktoyin@gmail.com");
//        member.setPosition("President");
//        member.setPassword("Tinuade1");
//        member.setFirstName("Tinu");
//        member.setLastName("Ade");
//        member.setPhoneNumber("08138732503");
//       var registrationRequest= getRegisterCooperativeRequest();
//        response = cooperativeService.registerCooperative(registrationRequest, memberService);
//    }
//
//    @Test
//    public void testThatCooperativeCanBeRegistered() {
//        assertThat(response).isNotNull();
//        assertThat(response.getNumberOfMembers()).isEqualTo(1);
//        assertThat(response.getName()).isEqualTo("REGNOS");
//
//    }
//
//    @Test
//    public void testThatCooperativeCanBeFoundByMail(){
//       var response = cooperativeService.findCooperativeByMail("speaktoyin5@gmail.com");
//        assertThat(response).isNotNull();
//    }
//    @Test
//    public void testThatCooperativeCanForgetPassword() throws CooperaException {
//       var response=  cooperativeService.forgotPassword("speaktoyin5@gmail.com");
//        System.out.println(response);
//        assertThat(response).isNotNull();
//    }
//    @Test
//    public void testThatCooperativePasswordCanBeReset() throws CooperaException {
//        PasswordResetRequest request = new PasswordResetRequest();
//        request.setToken("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MDExMjUzODYsImNvb3BlcmF0aXZlSWQiOiJSRUcvMjAyMy8wMDEiLCJleHAiOjE3MDExMjU5ODZ9.eA5A67bQPQsvMa0LgRpvxIX0KhVfU4CiGFwUdm-pSBaxSjheA7L_-MmnOSjfwP6_njHMy52cvY4HToCB7pB4gw");
//        request.setNewPassword("password");
//        request.setConfirmPassword("password");
//        var response=  cooperativeService.resetPassword(request);
//        System.out.println(response);
//        assertThat(response).isNotNull();
//    }
//}