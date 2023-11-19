package com.coopera.cooperaApp.services;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.cooperative.CooperaCoperativeService;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CooperativeServiceTest {

    @Autowired
    private CooperativeService cooperativeService;

    @Autowired
    private  CooperaCoperativeService cooperaCoperativeService;
    private RegisterCooperativeResponse response;

    @BeforeEach
    public void setUp() throws CooperaException {
        cooperativeService.deleteAll();
        RegisterMemberRequest member = new RegisterMemberRequest();
        member.setEmail("speaktoyin@gmail.com");
        member.setPosition("President");
        member.setPassword("Tinuade1");
        member.setFirstName("Tinu");
        member.setLastName("Ade");
        member.setPhoneNumber("08138732503");
       var registrationRequest= getRegisterCooperativeRequest(member);
        response = cooperativeService.registerCooperative(registrationRequest);
    }

    @Test
    public void testThatCooperativeCanBeRegistered() {
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfMembers()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("REGNOS");
        
    }
    private static RegisterCooperativeRequest getRegisterCooperativeRequest(RegisterMemberRequest member) {
        RegisterCooperativeRequest registrationRequest = new RegisterCooperativeRequest();
        registrationRequest.setLogo("Work Hard , Save Hard");
    //    registrationRequest.setMemberRequest(member);
        registrationRequest.setName("REGNOS");
        registrationRequest.setAddress("312, herbert macaulay");
        registrationRequest.setRcNumber("179092004");
        registrationRequest.setCompanyName("Coopera");
        return registrationRequest;
    }

}
