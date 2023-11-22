package com.coopera.cooperaApp.services.admin;

import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;

public interface AdminService {

    Object generateInvitationLink(InvitationLinkRequest recipient) throws CooperaException;




}
