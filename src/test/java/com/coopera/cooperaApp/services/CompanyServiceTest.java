package com.coopera.cooperaApp.services;

import com.coopera.cooperaApp.dtos.requests.RegisterCompanyRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterCooperativeRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.InitializeCompanyResponse;
import com.coopera.cooperaApp.services.company.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class CompanyServiceTest {
    @Autowired
    private CompanyService companyService;
    private InitializeCompanyResponse response;
    private RegisterCompanyRequest registrationRequest;

    @BeforeEach
    public void setUp() throws Exception {
        RegisterCompanyRequest request = new RegisterCompanyRequest();
        registrationRequest = getRegisterCompanyRequest();
        response = companyService.registerCompany(registrationRequest);
    }

    private static RegisterCompanyRequest getRegisterCompanyRequest() {
        RegisterCooperativeRequest coopRequest = new RegisterCooperativeRequest();
        coopRequest.setLogo("Work Hard , Save Hard");
        coopRequest.setCooperativeName("REGNOS");
        RegisterMemberRequest member = getMemberRequest();
        RegisterCompanyRequest registrationRequest = new RegisterCompanyRequest();
        registrationRequest.setCooperativeRequest(coopRequest);
        registrationRequest.setMemberRequest(member);
        registrationRequest.setCompanyAddress("312, herbert macaulay");
        registrationRequest.setRcNumber("179092004");
        registrationRequest.setCompanyName("Coopera");
        return registrationRequest;
    }

    private static RegisterMemberRequest getMemberRequest() {
        RegisterMemberRequest member = new RegisterMemberRequest();
        member.setEmail("speaktoyin5@gmail.com");
        member.setPosition("President");
        member.setPassword("Tinuade1");
        member.setFirstName("Tinu");
        member.setLastName("Ade");
        member.setPhoneNumber("08138732503");
        return member;
    }

    @Test
    public void testThatCustomerCanRegister() {
        assertThat(response).isNotNull();
        assertThat(response.getRcNumber()).isEqualTo(registrationRequest.getRcNumber());

    }
}
