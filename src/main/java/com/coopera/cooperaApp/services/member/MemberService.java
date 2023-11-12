package com.coopera.cooperaApp.services.member;

import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface MemberService {

    MemberResponse registerMember (RegisterMemberRequest request) throws CooperaException;

    MemberResponse setMemberRoleToAdmin(String id) throws CooperaException;

    void deleteAll();

    MemberResponse findById(String memberId) throws CooperaException;
}
