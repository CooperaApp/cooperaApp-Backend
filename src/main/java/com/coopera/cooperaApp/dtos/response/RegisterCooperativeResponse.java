package com.coopera.cooperaApp.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RegisterCooperativeResponse {
    private String id;
    private String name;
    private Long numberOfMembers;
}
