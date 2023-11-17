package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CooperativeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerCooperative() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RegisterCooperativeRequest registrationRequest = getRegisterCooperativeRequest(getRegisterMemberRequest());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cooperative")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andDo(print());

    }

    private static RegisterCooperativeRequest getRegisterCooperativeRequest(RegisterMemberRequest member) {
        RegisterCooperativeRequest registrationRequest = new RegisterCooperativeRequest();
        registrationRequest.setLogo("Work Hard , Save Hard");
        registrationRequest.setMemberRequest(member);
        registrationRequest.setName("REGNOS");
        registrationRequest.setAddress("312, herbert macaulay");
        registrationRequest.setRcNumber("179092004");
        registrationRequest.setCompanyName("Coopera");
        return registrationRequest;
    }

    private static RegisterMemberRequest getRegisterMemberRequest() {
        RegisterMemberRequest member = new RegisterMemberRequest();
        member.setEmail("speaktoyin5@gmail.com");
        member.setPosition("President");
        member.setPassword("Tinuade1");
        member.setFirstName("Tinu");
        member.setLastName("Ade");
        member.setPhoneNumber("08138732503");
        return member;
    }
}
