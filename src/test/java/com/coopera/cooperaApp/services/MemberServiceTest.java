package com.coopera.cooperaApp.services;

import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    private MemberResponse response;

    @BeforeEach
    public void setUp() throws CooperaException {
        memberService.deleteAll();
        RegisterMemberRequest member = new RegisterMemberRequest();
        member.setEmail("speaktoyin@gmail.com");
        member.setPosition("President");
        member.setPassword("Tinuade1");
        member.setFirstName("Tinu");
        member.setLastName("Ade");
        member.setPhoneNumber("08138732503");
        response = memberService.registerMember(member);
    }

    @Test
    public void testThatCustomerCanRegister() {
        assertThat(response).isNotNull();
        assertThat(response.getRole().size()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("Tinu Ade");

    }

    @Test
    public void testThatRoleCanBeSetToAdmin() throws CooperaException {
      response=  memberService.setMemberRoleToAdmin(response.getId());
        assertThat(response.getRole().size()).isEqualTo(2);
    }


}
