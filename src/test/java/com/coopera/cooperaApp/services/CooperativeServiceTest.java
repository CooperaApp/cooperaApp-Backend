package com.coopera.cooperaApp.services;

import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.response.RegisterCooperativeResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
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
    private RegisterCooperativeResponse response;

    @BeforeEach
    public void setUp() throws CooperaException {
        cooperativeService.deleteAll();
        RegisterCooperativeRequest coopRequest = new RegisterCooperativeRequest();
        coopRequest.setLogo("Work Hard , Save Hard");
        coopRequest.setCooperativeName("REGNOS");
        response = cooperativeService.registerCooperative(coopRequest);
    }

    @Test
    public void testThatCustomerCanRegister() {
        assertThat(response).isNotNull();
        assertThat(response.getNumberOfMembers()).isEqualTo(0);
        assertThat(response.getName()).isEqualTo("REGNOS");

    }
    @Test
    public void testThatMembersCanBeAdded() throws CooperaException {
        response =cooperativeService.addMemberToCooperative(response.getId(), "6550052c226daa7af6deafd4");
        assertThat(response.getNumberOfMembers()).isEqualTo(1);
    }
}
