package com.coopera.cooperaApp.services.admin;

import com.coopera.cooperaApp.dtos.requests.InvitationLinkRequest;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.services.cooperative.CooperativeService;

public interface AdminService {

    Object generateInvitationLink(InvitationLinkRequest recipient, CooperativeService cooperativeService) throws CooperaException;






}
