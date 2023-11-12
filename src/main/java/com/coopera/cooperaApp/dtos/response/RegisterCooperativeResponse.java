package com.coopera.cooperaApp.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterCooperativeResponse {
    private String id;
    private String name;
    private int numberOfMembers;
}
