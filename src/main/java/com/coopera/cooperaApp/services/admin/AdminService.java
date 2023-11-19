package com.coopera.cooperaApp.services.admin;

import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface AdminService {

    String generateInvitationLink(String recipient) throws CooperaException;




}
