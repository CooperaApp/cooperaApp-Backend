package com.coopera.cooperaApp.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InvitationLinkRequest {

    private List<String> recipientEmail;



}
