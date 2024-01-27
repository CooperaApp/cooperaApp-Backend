package com.coopera.cooperaApp.services.member;

import com.coopera.cooperaApp.dtos.requests.PasswordResetRequest;
import com.coopera.cooperaApp.dtos.requests.RegisterMemberRequest;
import com.coopera.cooperaApp.dtos.response.MemberResponse;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Member;

import java.util.List;

public interface MemberService {

    MemberResponse registerMember (RegisterMemberRequest request) throws CooperaException;

    MemberResponse setMemberRoleToAdmin(String id) throws CooperaException;

    void deleteAll();
    String forgotMemberPassword(String email) throws CooperaException;
    String resetPassword(PasswordResetRequest passwordResetRequest) throws CooperaException;

    MemberResponse findById(String memberId) throws CooperaException;

    Member findMemberById(String memberId) throws CooperaException;

    List<MemberResponse> findAllMembersByCooperativeId(int page, int items);

    List<Member> findAllMembersWithoutPagination();


    Long getNumberOfMembersByCooperativeId(String id);



}
