package com.coopera.cooperaApp.services.member;

import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Member;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MemberService {

    MemberResponse registerMember (RegisterMemberRequest request) throws CooperaException;

    MemberResponse setMemberRoleToAdmin(String id) throws CooperaException;

    void deleteAll();

    MemberResponse findById(String memberId) throws CooperaException;

    List<Member> findAllMembers();
}
