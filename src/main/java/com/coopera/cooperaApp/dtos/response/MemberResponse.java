package com.coopera.cooperaApp.dtos.response;
import com.coopera.cooperaApp.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MemberResponse {
private String name;
private List<Role> role;
private String id;
}
