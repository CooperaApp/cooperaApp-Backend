package com.coopera.cooperaApp.dtos.response;

import com.coopera.cooperaApp.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class MemberResponse {
    private String name;
    private Set<Role> role;
    private String id;
    private String cooperativeId;
}
