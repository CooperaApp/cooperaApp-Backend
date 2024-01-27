package com.coopera.cooperaApp.controllers;

import com.coopera.cooperaApp.dtos.requests.ForgotPasswordRequest;
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

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void registerCooperativeTest() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
        RegisterCooperativeRequest registrationRequest = getRegisterCooperativeRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cooperative/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andDo(print());

    }

    private static RegisterCooperativeRequest getRegisterCooperativeRequest() {
        RegisterCooperativeRequest registrationRequest = new RegisterCooperativeRequest();
        registrationRequest.setLogo("Work Hard , Save Hard");
        registrationRequest.setEmail("speaktoyin5@gmail.com");
     //   registrationRequest.setMemberRequest(member);
        registrationRequest.setName("REGNOS");
        registrationRequest.setAddress("312, herbert macaulay");
        registrationRequest.setRcNumber("179092004");
        registrationRequest.setCompanyName("Coopera");
        return registrationRequest;
    }
    @Test
    public void testForgotPassword() throws Exception {
        ForgotPasswordRequest mail = new ForgotPasswordRequest();
        mail.setEmail("speaktoyin5@gmail.com");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cooperative/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(mail)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }
}
